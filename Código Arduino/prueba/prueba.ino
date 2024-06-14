#include <Keypad.h>
#include <LiquidCrystal.h>
#include <PubSubClient.h>
#include <WiFi.h>
#include <ArduinoJson.h> // Librería para manejar JSON

const byte ROW_NUM = 4; // four rows
const byte COLUMN_NUM = 4; // four columns
const char *ssid = "iPhone de David";
const char *wifi_password = "Ubicua2024";
const char *mqtt_broker = "172.20.10.7";  // broker address// define topic
const char *mqtt_username = "";              // username for authentication
const char *mqtt_password = "";              // password for authentication
const int mqtt_port = 1883;
const char *Parking_maquina_entrada = "Ubipark/Parking1/Maquina/Entrada";  
const char *Parking_maquina_salida = "Ubipark/Parking1/Maquina/Salida";  
const char *Parking_maquina_respuesta = "Ubipark/Parking1/Maquina/Respuesta";
const char *Parking_maquina_tarjeta = "Ubipark/Parking1/Maquina/Tarjeta";    

WiFiClient espClient;
PubSubClient client(espClient);

// Configuración de los pines de la LCD a los pines del ESP32
LiquidCrystal lcd(22, 23, 5, 18, 19, 21);

char keys[ROW_NUM][COLUMN_NUM] = {
  { '1', '2', '3', 'A' },
  { '4', '5', '6', 'B' },
  { '7', '8', '9', 'C' },
  { '*', '0', '#', 'D' }
};

byte pin_rows[ROW_NUM] = { 16, 32, 14, 27 };       // Pines de las filas conectados al ESP32
byte pin_column[COLUMN_NUM] = { 26, 17, 13, 15 };  // Pines de las columnas conectados al ESP32

Keypad keypad = Keypad(makeKeymap(keys), pin_rows, pin_column, ROW_NUM, COLUMN_NUM);

String matricula = ""; // Para almacenar la matrícula
int tiempoEnParking = 0; // Para almacenar el tiempo en el parking
bool esperandoRespuesta = false;
bool esperandoTarjeta = false;
unsigned long tiempoSinRespuesta = 0;
const char* id_parking = "4";

void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);  // Inicializa la pantalla LCD con 16 columnas y 2 filas
  lcd.clear();       // Limpia cualquier cosa que pueda estar en la pantalla

  WiFi.begin(ssid, wifi_password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");

  // Conectando al broker MQTT
  client.setServer(mqtt_broker, mqtt_port);
  client.setCallback(callback);
  while (!client.connected()) {
    String client_id = "Arduino";
    client_id += String(WiFi.macAddress());
    Serial.printf("The client %s connects to the public mqtt broker\n", client_id.c_str());
    if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
      Serial.println("Public emqx mqtt broker connected");
      client.subscribe(Parking_maquina_respuesta);
      client.subscribe(Parking_maquina_tarjeta);
    } else {
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }
}

void loop() {
  client.loop();
  if (!esperandoRespuesta) {
    displayMenu(); // Muestra el menú
  }

  char key = keypad.getKey(); // Lee la tecla presionada

  if (key && !esperandoRespuesta) { // Si se presionó una tecla y no se está esperando una respuesta
    Serial.println(key); // Imprime la tecla en el monitor serial
    lcd.clear(); // Limpia la pantalla LCD

    if (key == '1') {
      handleEntradaParking(); // Maneja la opción de Entrada Parking
    } else if (key == '2') {
      handleSalidaParking(); // Maneja la opción de Salida Parking
    } else {
      lcd.setCursor(0, 0); // Establece el cursor en la columna 0, fila 0
      lcd.print("Opcion invalida");
      delay(2000); // Espera 2 segundos para mostrar el mensaje
    }
  }
}

void displayMenu() {
  lcd.setCursor(0, 0); // Establece el cursor en la columna 0, fila 0
  lcd.print("1: Entrada");
  lcd.setCursor(0, 1); // Establece el cursor en la columna 0, fila 1
  lcd.print("2: Salida");
}

void handleEntradaParking() {
  lcd.clear(); // Limpia la pantalla LCD
  lcd.setCursor(0, 0); // Establece el cursor en la columna 0, fila 0
  lcd.print("Introduce mat:");
  lcd.setCursor(0, 1); // Establece el cursor en la columna 0, fila 1
  matricula = ""; // Limpia la variable de matrícula

  while (true) {
    char key = keypad.getKey(); // Lee la tecla presionada
    if (key) { // Si se presionó una tecla
      Serial.println(key); // Imprime la tecla en el monitor serial
      if (key == '#') { // Si se presionó la tecla de confirmación (por ejemplo '#')
        if (isMatriculaValida(matricula)) {
          break; // Sale del bucle
        } else {
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Matricula invalida");
          delay(2000);
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Introduce mat:");
          lcd.setCursor(0, 1);
          lcd.print(matricula);
        }
      } else if (key == '*') { // Si se presionó la tecla de borrar (por ejemplo '*')
        if (matricula.length() > 0) {
          matricula.remove(matricula.length() - 1); // Borra el último carácter
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Introduce mat:");
          lcd.setCursor(0, 1);
          lcd.print(matricula);
        }
      } else if (key == 'D') { // Si se presionó la tecla para regresar al menú
        lcd.clear();
        displayMenu();
        return; // Sal del bucle y retorna al menú
      } else {
        matricula += key; // Añade la tecla presionada a la matrícula
        lcd.setCursor(0, 1); // Establece el cursor en la columna 0, fila 1
        lcd.print(matricula); // Muestra la matrícula en la LCD
      }
    }
  }

  // Aquí puedes agregar el código que maneja la matrícula ingresada
  Serial.print("Matricula introducida: ");
  Serial.println(matricula);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Matricula guardada");
  StaticJsonDocument<200> doc;
  doc["id_parking"] = id_parking;
  doc["matricula"] = matricula;

  char jsonBuffer[256];
  serializeJson(doc, jsonBuffer);

  // Publicar el mensaje JSON
  client.publish(Parking_maquina_entrada, jsonBuffer);
  delay(2000);
  lcd.clear();
}

bool isMatriculaValida(String matricula) {
  if (matricula.length() != 7) {
    return false; // La matrícula debe tener 7 caracteres
  }
  for (int i = 0; i < 4; i++) {
    if (!isDigit(matricula[i])) {
      return false; // Los primeros 4 caracteres deben ser dígitos
    }
  }
  for (int i = 4; i < 7; i++) {
    if (!isAlpha(matricula[i])) {
      return false; // Los últimos 3 caracteres deben ser letras
    }
  }
  return true; // La matrícula es válida
}

void handleSalidaParking() {
  lcd.clear(); // Limpia la pantalla LCD
  lcd.setCursor(0, 0); // Establece el cursor en la columna 0, fila 0
  lcd.print("Introduce mat:");
  lcd.setCursor(0, 1); // Establece el cursor en la columna 0, fila 1
  matricula = ""; // Limpia la variable de matrícula

  while (true) {
    char key = keypad.getKey(); // Lee la tecla presionada
    if (key) { // Si se presionó una tecla
      Serial.println(key); // Imprime la tecla en el monitor serial
      if (key == '#') { // Si se presionó la tecla de confirmación (por ejemplo '#')
        if (isMatriculaValida(matricula)) {
          StaticJsonDocument<200> doc;
          doc["id_parking"] = id_parking;
          doc["matricula"] = matricula;

          char jsonBuffer[256];
          serializeJson(doc, jsonBuffer);

          // Publicar el mensaje JSON
          client.publish(Parking_maquina_salida, jsonBuffer);
          esperandoRespuesta = true; // Esperar la respuesta del servidor
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Comprobando");
          lcd.setCursor(0, 1);
          lcd.print("Matricula");
          break; // Sale del bucle
        } else {
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Matricula invalida");
          delay(2000);
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Introduce mat:");
          lcd.setCursor(0, 1);
          lcd.print(matricula);
        }
      } else if (key == '*') { // Si se presionó la tecla de borrar (por ejemplo '*')
        if (matricula.length() > 0) {
          matricula.remove(matricula.length() - 1); // Borra el último carácter
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Introduce mat:");
          lcd.setCursor(0, 1);
          lcd.print(matricula);
        }
      } else if (key == 'D') { // Si se presionó la tecla para regresar al menú
        lcd.clear();
        displayMenu();
        return; // Sal del bucle y retorna al menú
      } else {
        matricula += key; // Añade la tecla presionada a la matrícula
        lcd.setCursor(0, 1); // Establece el cursor en la columna 0, fila 1
        lcd.print(matricula); // Muestra la matrícula en la LCD
      }
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  payload[length] = '\0'; // Asegura que el payload es una cadena de caracteres válida
  String message = String((char*)payload);

  Serial.print("Mensaje recibido [");
  Serial.print(topic);
  Serial.print("]: ");
  Serial.println(message);

  if (String(topic) == Parking_maquina_respuesta) {
    if (message == "0") {
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Matricula invalida");
      delay(2000);
      lcd.clear();
      esperandoRespuesta = false; // Permite al usuario intentar de nuevo
    } else {
      tiempoEnParking = message.toInt();
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Tiempo: ");
      lcd.print(tiempoEnParking);
      lcd.print(" secs");
      
      int importe = calcularImporte(tiempoEnParking);

      // Crear documento JSON para la publicación del importe
      StaticJsonDocument<200> doc;
      doc["matricula"] = matricula;
      doc["importe"] = importe;
      char jsonBuffer[256];
      serializeJson(doc, jsonBuffer);
      
      // Publicar el mensaje JSON con el importe y la matrícula
      client.publish(Parking_maquina_tarjeta, jsonBuffer);

      lcd.setCursor(0, 1);
      lcd.print("Importe: $");
      lcd.print(importe);
      delay(5000);
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Esperando Pago");
      esperandoTarjeta = true;
      tiempoSinRespuesta = millis(); // Inicia el contador de tiempo
    }
  } else if (String(topic) == Parking_maquina_tarjeta) {
    delay(11000);
    if (message == "1") {  // Corrige el operador de comparación
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Pagado");
      lcd.setCursor(0, 1);
      lcd.print("Buen viaje");
      esperandoTarjeta = false;
      delay(5000); // Espera para que el usuario vea el mensaje "Pagado"
      lcd.clear();
      esperandoRespuesta = false;
    } else {
      lcd.clear();
      lcd.setCursor(0, 0);
      lcd.print("Error en el pago");
      delay(2000);
      esperandoRespuesta = false;
      esperandoTarjeta = false;
    }
  }  
}

int calcularImporte(int tiempo) {
  int tarifaPorSegundo = 1; // Puedes ajustar la tarifa por segundo según tus necesidades
  return tiempo * tarifaPorSegundo;
}
