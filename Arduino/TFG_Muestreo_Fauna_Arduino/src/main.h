
#include <Adafruit_BME280.h>
#include <SoftwareSerial.h>
#include <TinyGPS++.h>
#include <ComunicationManager.h>
#include <Adafruit_Sensor.h>

//Constants
//Bluetooth transmission led
#define TRANSMISSION_LED 5
//Sea level pressure
#define SEALEVELPRESSURE_HPA (1013.25)

//Sensors
//BME
Adafruit_BME280 bme;
//GPS variables
SoftwareSerial serial_conection(10, 9); //RX = 9 ,RT= 10,
TinyGPSPlus gps;
//Bluetooth
ComunicationManager comunicationManager(2,3, TRANSMISSION_LED); // TX, RX, Transmission led


// Functions
void loop();
void setup();
void updateGPS();
void updateBME();
void updateUV();



