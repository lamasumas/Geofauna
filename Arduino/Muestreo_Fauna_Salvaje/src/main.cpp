#include <Arduino.h>
#include <Wire.h>
#include <SPI.h>
#include <TinyGPS++.h>
#include <SoftwareSerial.h>

//Definicion de pines
#define GPS_RX 8
#define GPS_RT 7

//GPS variables
SoftwareSerial serial_conection(GPS_RX, GPS_RT);
TinyGPSPlus gps;

void setup() {
  Serial.begin(9600);

  //GPS
  serial_conection.begin(9600);
  Serial.println("GPS Start");
  
  
}

void loop() {
 
  //GPS
  while (serial_conection.available())
  {
    gps.encode(serial_conection.read());
  }

  if(gps.location.isUpdated())
  {
    Serial.println("Latitude:");
    Serial.println(gps.location.lat(), 6);
    Serial.println("Longitude:");
    Serial.println(gps.location.lng(), 6);
    Serial.println("Altitude Feet:");
    Serial.println(gps.altitude.feet());
    Serial.println("");
  }
    

}