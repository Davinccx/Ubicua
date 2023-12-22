#include <PubSubClient.h>
#include <WiFi.h>
#include "pitches.h"

#define BUZZER_PIN 27
#define PIN_1 32

bool tl_1;

const char *ssid = "MIWIFI_ESER";
const char *password = "SqM4FthK";

const char *mqtt_broker = "broker.emqx.io";// broker address
const char *topic = "monedas/enviar"; // define topic 
const char *mqtt_username = ""; // username for authentication
const char *mqtt_password = "";// password for authentication
const int mqtt_port = 1883;

WiFiClient espClient;
PubSubClient client(espClient);

int correctMelody[] = {
  NOTE_C4, NOTE_G3, NOTE_G3, NOTE_A3, NOTE_G3, 0, NOTE_B3, NOTE_C4
};

int wrongMelody[] = {
  NOTE_A3, NOTE_E3,NOTE_C3
};


int noteDurations[] = {
  4, 8, 8, 4, 4, 4, 4, 4
};

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);//iniciailzamos la comunicación
  pinMode(PIN_1,INPUT);

  WiFi.begin(ssid, password);
 while (WiFi.status() != WL_CONNECTED) {
     delay(500);
     Serial.println("Connecting to WiFi..");
 }
 Serial.println("Connected to the WiFi network");
 //connecting to a mqtt broker
 client.setServer(mqtt_broker, mqtt_port);
 client.setCallback(callback);
 while (!client.connected()) {
     String client_id = "Monedas";
     client_id += String(WiFi.macAddress());
     Serial.printf("The client %s connects to the public mqtt broker\n", client_id.c_str());
     if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
         Serial.println("Public emqx mqtt broker connected");
     } else {
         Serial.print("failed with state ");
         Serial.print(client.state());
         delay(2000);
     }
 }
 
  client.subscribe("maquina/autentificacion");

}

void callback(char *topic, byte *payload, unsigned int length) {

  String autentificacion;
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
  Serial.print("Message:");
  for (int i = 0; i < length; i++) {
      Serial.print((char) payload[i]);
  }
  Serial.println();
  Serial.println("-----------------------");

  for (int j = 0; j < length; j++) {
      
      autentificacion += (char) payload[j];
  }

  if(String (topic) == "maquina/autentificacion")
  {

    if(autentificacion == "true"){

          succesfulLogin();

    }else{

          failedLogin();

    }

  }


}

void loop() {
  client.loop();
  int valor = digitalRead(PIN_1);
  if(valor ==1 && valor == HIGH){

      Serial.println("Ha caido una moneda");
      client.publish(topic,"1");
      delay(1000);
  }
  
  


}

void succesfulLogin(){

    for (int thisNote = 0; thisNote < 8; thisNote++) {
        int noteDuration = 1000 / noteDurations[thisNote];
        tone(BUZZER_PIN, correctMelody[thisNote], noteDuration);
        int pauseBetweenNotes = noteDuration * 1.30;
        delay(pauseBetweenNotes);
        noTone(BUZZER_PIN);
  }

}

void failedLogin(){

    for (int thisNote = 0; thisNote < 3; thisNote++) {
      int noteDuration = 200;
      tone(BUZZER_PIN, wrongMelody[thisNote], noteDuration);
      int pauseBetweenNotes = noteDuration * 1.30;
      delay(noteDuration+50);
      noTone(BUZZER_PIN);
  }


}