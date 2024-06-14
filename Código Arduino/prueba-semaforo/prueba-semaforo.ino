#include <PubSubClient.h>
#include <WiFi.h>
#include <ArduinoJson.h> // Librería para manejar JSON

const char *ssid = "iPhone de David";
const char *wifi_password = "Ubicua2024";

const char *mqtt_broker = "172.20.10.7";  // broker address// define topic
const char *mqtt_username = "";              // username for authentication
const char *mqtt_password = "";              // password for authentication
const char *P1_topic_estado = "Ubipark/Parking1/P1/Estado";  // Tópico para publicar el estado
const char *P1_topic_reserva = "Ubipark/Parking1/P1/Reserva";  // Tópico para recibir reservas

WiFiClient espClient;
PubSubClient client(espClient);

const int mqtt_port = 1883;
const int P1_ROJO = 19;
const int P1_AMARILLO = 22;
const int P1_VERDE = 21;

// Definimos los pines del ESP32 a los que conectaremos el Trig y Echo del HC-SR04
#define P1_TRIG_PIN 5
#define P1_ECHO_PIN 18

bool P1_reserved = false; // Variable para almacenar si hay una reserva
const char* id_parking = "4";
const char* id_plaza = "1";

void setup() {
  // Iniciamos la comunicación serie a 115200 baudios
  Serial.begin(9600);
  
  // Configuramos los pines de los LEDs como salida
  pinMode(P1_ROJO, OUTPUT);
  pinMode(P1_AMARILLO, OUTPUT);
  pinMode(P1_VERDE, OUTPUT);
  
  // Configuramos los pines del Trig y Echo
  pinMode(P1_TRIG_PIN, OUTPUT);
  pinMode(P1_ECHO_PIN, INPUT);
  WiFi.begin(ssid, wifi_password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");
  //connecting to a mqtt broker
  client.setServer(mqtt_broker, mqtt_port);
  client.setCallback(callback);
  while (!client.connected()) {
    String client_id = "Arduino";
    client_id += String(WiFi.macAddress());
    Serial.printf("The client %s connects to the public mqtt broker\n", client_id.c_str());
    if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {

      Serial.println("Public emqx mqtt broker connected");
      client.subscribe(P1_topic_reserva); 

    } else {
      Serial.print("failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }
}

void callback(char *topic, byte *payload, unsigned int length) {
  String reserve;
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
  Serial.print("Message:");
  
  // Imprime el payload recibido
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  Serial.println("-----------------------");

  // Convierte el payload en una cadena
  for (int j = 0; j < length; j++) {
    reserve += (char) payload[j];
  }
  int reserva = reserve.toInt();
  

  // Compara el tópico recibido
  if (String(topic) == "Ubipark/Parking1/P1/Reserva") {
    Serial.println(reserva);
    // Compara el valor de reserva
    if(reserva == 1){
      P1_reserved = true;
      return;
    }
    else if(reserva == 0){
      P1_reserved = false;
      return;
    }
  }
}



long CalcularDistancia(int SensorEcho, int SensorTriger) {
  // Generamos un pulso de 10 microsegundos en el pin Trig
  digitalWrite(SensorTriger, LOW);
  delayMicroseconds(2);
  digitalWrite(SensorTriger, HIGH);
  delayMicroseconds(10);
  digitalWrite(SensorTriger, LOW);

  // Medimos el tiempo que tarda en llegar el eco
  long duration = pulseIn(SensorEcho, HIGH);

  // Convertimos la duración en distancia (en centímetros)
  long distance = duration * 0.034 / 2;

  return distance;
}

void loop() {
  client.loop();

  // Crear un documento JSON
  StaticJsonDocument<200> doc;
  char jsonBuffer[256];

  if (P1_reserved) {
    if (CalcularDistancia(P1_ECHO_PIN, P1_TRIG_PIN) < 10) {
      digitalWrite(P1_ROJO, HIGH);
      digitalWrite(P1_VERDE, LOW);
      digitalWrite(P1_AMARILLO, HIGH);

      // Preparar el mensaje JSON
      doc["id_parking"] = id_parking;
      doc["id_plaza"] = id_plaza;
      doc["estado"] = 1;

      serializeJson(doc, jsonBuffer);
      client.publish(P1_topic_estado, jsonBuffer);
    } else {
      digitalWrite(P1_ROJO, LOW);
      digitalWrite(P1_VERDE, LOW);
      digitalWrite(P1_AMARILLO, HIGH);

      // Preparar el mensaje JSON
      doc["id_parking"] = id_parking;
      doc["id_plaza"] = id_plaza;
      doc["estado"] = 0;

      serializeJson(doc, jsonBuffer);
      client.publish(P1_topic_estado, jsonBuffer);
    }
  } else {
    if (CalcularDistancia(P1_ECHO_PIN, P1_TRIG_PIN) < 10) {
      digitalWrite(P1_ROJO, HIGH);
      digitalWrite(P1_VERDE, LOW);
      digitalWrite(P1_AMARILLO, LOW);

      // Preparar el mensaje JSON
      doc["id_parking"] = id_parking;
      doc["id_plaza"] = id_plaza;
      doc["estado"] = 1;

      serializeJson(doc, jsonBuffer);
      client.publish(P1_topic_estado, jsonBuffer);
    } else {
      digitalWrite(P1_ROJO, LOW);
      digitalWrite(P1_VERDE, HIGH);
      digitalWrite(P1_AMARILLO, LOW);

      // Preparar el mensaje JSON
      doc["id_parking"] = id_parking;
      doc["id_plaza"] = id_plaza;
      doc["estado"] = 0;

      serializeJson(doc, jsonBuffer);
      client.publish(P1_topic_estado, jsonBuffer);
    }
  }

  // Esperamos cinco segundos antes de la siguiente medición
  delay(10000);
}


