#include "DHT.h"
#include "ArduinoJson.h"

#define DHTPIN 2     
#define DHTTYPE DHT11   

DHT dht(DHTPIN, DHTTYPE);

long previousMillis = 0;
unsigned long currentMillis = 0;
long interval = 250000; // READING INTERVAL

double t = 0;  // TEMPERATURE VAR
double h = 0;  // HUMIDITY VAR
String data;

StaticJsonDocument<200> doc;

void setup() { 
  Serial.begin(9600);
  
  dht.begin(); 
  delay(10000); // GIVE THE SENSOR SOME TIME TO START

  h = dht.readHumidity(); 
  t = dht.readTemperature();
  data = "";
}

void loop(){
  currentMillis = millis();
  if(currentMillis - previousMillis > interval) { // READ ONLY ONCE PER INTERVAL
    previousMillis = currentMillis;
    h = dht.readHumidity();
    t = dht.readTemperature();
  }

  doc["id_destination"] = 4;
  doc["luminosity"] = 0.0;
  doc["temperature"] = t;
  doc["humidity"] = h;
  
  serializeJson(doc, Serial);
  Serial.println();
  delay(50000); // WAIT BEFORE SENDING AGAIN
}
