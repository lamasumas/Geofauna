#include <Arduino.h>
#include <Wire.h>
#include <SPI.h>
#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <Adafruit_BME280.h>
#include <Adafruit_Sensor.h>


//Definicion de pines
#define GPS_RX 8
#define GPS_RT 7

// Constantes
#define SEALEVELPRESSURE_HPA (1013.25)

//GPS variables
//SoftwareSerial serial_conection(GPS_RX, GPS_RT);
//TinyGPSPlus gps;

// BME280
Adafruit_BME280 bme;

void setup() {
  Serial.begin(9600);

  //GPS
  //serial_conection.begin(9600);
  //Serial.println("GPS Start");
  
  //BME208
  if (!bme.begin(0x76)) {
		Serial.println("Could not find a valid BME280 sensor, check wiring!");
		while (1);
	}
  
}

void loop() {
 //GPS
 /*
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
 */

  // BME208: Temperatura - Presion - Humedad
  /*
  Serial.print("Temperature = ");
	Serial.print(bme.readTemperature());
	Serial.println("*C");

	Serial.print("Pressure = ");
	Serial.print(bme.readPressure() / 100.0F);
	Serial.println("hPa");

	Serial.print("Approx. Altitude = ");
	Serial.print(bme.readAltitude(SEALEVELPRESSURE_HPA));
	Serial.println("m");

	Serial.print("Humidity = ");
	Serial.print(bme.readHumidity());
	Serial.println("%");

	Serial.println();
	delay(5000);
  */
 Serial.println("Works new rule in udev");

}