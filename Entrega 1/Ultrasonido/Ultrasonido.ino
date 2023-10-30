#include <PubSubClient.h>
#include <WiFi.h>
#include "pitches.h"

#define BUZZER_PIN 32

const char *ssid = "MIWIFI_ESER";
const char *password = "SqM4FthK";

const char *mqtt_broker = "broker.emqx.io";// broker address
const char *topic = "monedas/enviar"; // define topic 
const char *mqtt_username = "ubicua"; // username for authentication
const char *mqtt_password = "ubicua";// password for authentication
const int mqtt_port = 1883;

const int Trigger = 14;   //Pin digital 2 para el Trigger del sensor
const int Echo = 12;

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
  pinMode(Trigger, OUTPUT); //pin como salida
  pinMode(Echo, INPUT);  //pin como entrada
  digitalWrite(Trigger, LOW);

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
     String client_id = "esp32-client-";
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
 
 client.subscribe("teclado/enviar");

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

  if(String (topic) == "teclado/enviar")
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
  long t; //timepo que demora en llegar el eco
  long d; //distancia en centimetros

  digitalWrite(Trigger, HIGH);
  delayMicroseconds(10);          //Enviamos un pulso de 10us
  digitalWrite(Trigger, LOW);
  
  t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
  d = t/59;             //escalamos el tiempo a una distancia en cm
  
  if(d<10){

      Serial.println("Ha caido una moneda");
      client.publish(topic,"1");

  }

  delay(200);


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