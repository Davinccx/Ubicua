#include <LiquidCrystal.h>
#include <Keypad.h>
#include <PubSubClient.h>
#include <WiFi.h>

#define ROW_NUM 4
#define COLUMN_NUM 4


const char *ssid = "MIWIFI_ESER";
const char *wifi_password = "SqM4FthK";

const char *mqtt_broker = "broker.emqx.io";// broker address
const char *suscribeTopic = "monedas/enviar"; // define topic 
const char *mqtt_username = "ubicua"; // username for authentication
const char *mqtt_password = "ubicua";// password for authentication
const int mqtt_port = 1883;

char key;

WiFiClient espClient;
PubSubClient client(espClient);

char* password = "#123";

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(22, 23, 5, 18, 19, 21);


char keys[ROW_NUM][COLUMN_NUM] = {
  { '1', '2', '3', 'A' },
  { '4', '5', '6', 'B' },
  { '7', '8', '9', 'C' },
  { '*', '0', '#', 'D' }
};

byte pin_rows[ROW_NUM] = { 16, 32, 14, 27 };  // GPIO19, GPIO18, GPIO5, GPIO17 connect to the row pins
byte pin_column[COLUMN_NUM] = { 26, 17, 13, 15 };

Keypad keypad = Keypad(makeKeymap(keys), pin_rows, pin_column, ROW_NUM, COLUMN_NUM);

int monedas = 0;


void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
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
 
 client.subscribe(suscribeTopic);

}

void callback(char *topic, byte *payload, unsigned int length) {
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
  Serial.print("Message:");
  
  for (int i = 0; i < length; i++) {
      Serial.print((char) payload[i]);
      
  }
  payload[1]=  '\0';
  int aNumber = atoi((char *)payload);
  monedas = monedas + aNumber;
  Serial.println();
  Serial.println("-----------------------");
  
}

void loop() {
  client.loop();
  lcd.setCursor(0, 0);
  lcd.print("- MODO DE USO  -");
  lcd.setCursor(0, 1);
  lcd.print(" 1.Buy   2.Admin");
  key = keypad.getKey();
  switch (key) {
    case '1':
      comprarFunction();
      break;
    case '2':
      adminFunction();
      break;
  }
  
}


void adminFunction() {
  char posiblePassword[4] = {};
  lcd.clear();
  lcd.print("Clave:");

  int length = 0;
  int posicion = 0;

  while (length < 4) {

    key = keypad.getKey();
    if (key) {

      lcd.setCursor(length, 1);
      lcd.print(key);
      posiblePassword[length] = key;

      if (key == password[length]) {

        posicion++;
        length++;

      } else {
        length++;
      }
    }
    
  }
  
  if (posicion == 4) {
    client.publish("teclado/enviar","true");
    lcd.clear();
    lcd.print("Bienvenido");
    lcd.setCursor(0, 1);
    lcd.print("Administrador");
    delay(2500);
  ;
  } else {
    client.publish("teclado/enviar","false");
    lcd.clear();
    lcd.print("Password");
    lcd.setCursor(0, 1);
    lcd.print("Incorrecta");
    delay(2500);
}
}

void comprarFunction(){
  bool vendido = false;
  while(!vendido){
  lcd.clear();
  lcd.print("Productos:");
  lcd.setCursor(0,1);
  lcd.print("1.Ron 2.Jagger");
  key = keypad.getKey();
  if(key=='1'){
    lcd.clear();
    lcd.print("Precio Ron:");
    lcd.setCursor(0,1);
    lcd.print("2 Monedas");
    delay(3000);
    lcd.clear();
    lcd.print("Introduce Coins:");
    lcd.setCursor(0,1);
    lcd.print("Total Coins:");
    lcd.setCursor(13,1);
    lcd.print(monedas);
    delay(2500);
    break;
    
  }
  else if(key=='2'){
    lcd.clear();
    lcd.print("Precio Jagger:");
    lcd.setCursor(0,1);
    lcd.print("1 Moneda");
    delay(3000);
    lcd.clear();
    lcd.print("Introduce Coins:");
    lcd.setCursor(0,1);
    lcd.print("Total Coins:");
    lcd.setCursor(13,1);
    lcd.print(monedas);
    delay(2500);
    break;

  }

  }

}
