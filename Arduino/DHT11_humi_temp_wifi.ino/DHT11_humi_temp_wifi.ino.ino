#include "DHT.h"
#include "ArduinoJson.h"
#include <SPI.h>
#include <WiFi101.h>

#define DHTPIN 2     
#define DHTTYPE DHT11   

DHT dht(DHTPIN, DHTTYPE);

long previousMillis = 0;
unsigned long currentMillis = 0;
long interval = 250000; // READING INTERVAL

double t = 0;  // TEMPERATURE VAR
double h = 0;  // HUMIDITY VAR
String data;

char ssid[] = "DESKTOP";        // your network SSID (name)
char pass[] = "abcdefghij";    // your network password (use for WPA, or use as key for WEP)
int keyIndex = 0;            // your network key Index number (needed only for WEP)

int status = WL_IDLE_STATUS;
IPAddress server(192,168,137,1);  // numeric IP
WiFiClient client;

StaticJsonDocument<200> doc;

void setup() { 
  Serial.begin(9600);
  
  dht.begin(); 
  delay(10000); // GIVE THE SENSOR SOME TIME TO START

  h = dht.readHumidity(); 
  t = dht.readTemperature();
  data = "";

  // check for the presence of the shield:
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    // don't continue:
    while (true);
  }

  // attempt to connect to WiFi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);

    // wait 10 seconds for connection:
    delay(10000);
  }
  Serial.println("Connected to wifi");

  
}

void loop(){
  currentMillis = millis();
  if(currentMillis - previousMillis > interval) { // READ ONLY ONCE PER INTERVAL
    previousMillis = currentMillis;
    h = dht.readHumidity();
    t = dht.readTemperature();
  }

  doc["id_destination"] = 4;
  doc["temperature"] = t;
  doc["humidity"] = h;
  Serial.println("\nSending connection to server...");
  serializeJson(doc, data);
  // if you get a connection, report back via serial:
  if (client.connect(server, 9000)) {
    client.println("POST /weather/saveWeather HTTP/1.1");
    client.println("Content-Type: application/json");
    client.print("Content-Length: ");
    client.println(data.length());
    client.println("Host: 192.168.137.1:9000");
    client.println("Connection: close");
    client.println();
    client.println(data);
    while(client.available()){
      char c = client.read();
      Serial.write(c);
    }
  }

  if(client.connected()){
    client.stop();
  }
  //15 minutes
  delay(900000); // WAIT BEFORE SENDING AGAIN
}
