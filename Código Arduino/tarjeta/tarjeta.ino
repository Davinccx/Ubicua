#include <SPI.h>
#include <MFRC522.h>
#include <PubSubClient.h>
#include <WiFi.h>
#include <ArduinoJson.h>  // Librería para manejar JSON

const char *ssid = "iPhone de David";
const char *wifi_password = "Ubicua2024";
const char *mqtt_broker = "172.20.10.7";  // broker address
const char *mqtt_username = "";              // username for authentication
const char *mqtt_password = "";              // password for authentication
const char *Parking_maquina_tarjeta = "Ubipark/Parking1/Maquina/Tarjeta";  
const char *Parking_maquina_pago = "Ubipark/Parking1/Maquina/Pago";  
#define SS_PIN 5    // SDA pin connected to GPIO 5
#define RST_PIN 22  // RST pin connected to GPIO 22
WiFiClient espClient;
PubSubClient client(espClient);

const int mqtt_port = 1883;
MFRC522 mfrc522(SS_PIN, RST_PIN);  // Create MFRC522 instance
int importe = 0;  // Variable to store the received amount
String matricula = "";  // Variable to store the received license plate
bool esperar_tarjeta = false;  // Flag to indicate whether to wait for card

void setup() {
  Serial.begin(9600);  // Initialize serial communications with the PC
  SPI.begin();           // Initialize SPI bus
  mfrc522.PCD_Init();    // Initialize MFRC522
  Serial.println("Place your card near the reader...");
  Serial.println();
  WiFi.begin(ssid, wifi_password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.println("Connected to the WiFi network");
  // Connecting to the MQTT broker
  client.setServer(mqtt_broker, mqtt_port);
  client.setCallback(callback);
  while (!client.connected()) {
    String client_id = "Arduino";
    client_id += String(WiFi.macAddress());
    Serial.printf("The client %s connects to the public MQTT broker\n", client_id.c_str());
    if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
      Serial.println("Public EMQX MQTT broker connected");
      client.subscribe(Parking_maquina_tarjeta); 
    } else {
      Serial.print("Failed with state ");
      Serial.print(client.state());
      delay(2000);
    }
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  if (strcmp(topic, Parking_maquina_tarjeta) == 0) {
    payload[length] = '\0';  // Asegura que el payload es una cadena de caracteres válida
    String message = String((char*)payload);

    // Deserializar el mensaje JSON
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, message);
    if (error) {
      Serial.print("deserializeJson() failed: ");
      Serial.println(error.c_str());
      return;
    }
    
    // Obtener los valores del JSON
    if (!doc.containsKey("importe") || !doc.containsKey("matricula")) {
      return;
    }

    importe = doc["importe"];
    matricula = doc["matricula"].as<String>();

    // Verificar si la matrícula es null y el importe es 0
    if (matricula == "null" && importe == 0) {
      Serial.println("Received null matricula and importe 0, ignoring the message.");
      return;
    }
    
    esperar_tarjeta = true;

    Serial.print("Received amount: ");
    Serial.println(importe);
    Serial.print("Received license plate: ");
    Serial.println(matricula);
  }
}


void loop() {
  client.loop();

  // Only proceed with card reading if waiting for card
  if (esperar_tarjeta) {
    unsigned long start_time = millis();
    while (millis() - start_time < 10000) {  // Wait for 10 seconds
      if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
        Serial.print("UID tag: ");
        String content = "";
        for (byte i = 0; i < mfrc522.uid.size; i++) {
          Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
          Serial.print(mfrc522.uid.uidByte[i], HEX);
          content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
          content.concat(String(mfrc522.uid.uidByte[i], HEX));
        }
        Serial.println();
        Serial.print("Message: ");
        content.toUpperCase();
        Serial.println(content);

        // Crear documento JSON para la publicación del importe y la matrícula
        StaticJsonDocument<200> doc;
        doc["importe"] = importe;
        doc["matricula"] = matricula;
        
        char jsonBuffer[256];
        serializeJson(doc, jsonBuffer);
        
        // Publicar el mensaje JSON
        client.publish(Parking_maquina_pago, jsonBuffer);
        Serial.print("Published amount and license plate: ");
        Serial.println(jsonBuffer);

        // Publish '1' to the tarjeta topic
        client.publish(Parking_maquina_tarjeta, "1");
        Serial.println("Published '1' to the tarjeta topic");

        // Halt PICC
        mfrc522.PICC_HaltA();
        
        // Reset the flag
        esperar_tarjeta = false;
        break;  // Exit the while loop after reading the card
      }
    }
    // If 10 seconds pass without reading a card, reset the flag
    esperar_tarjeta = false;
  }
}
