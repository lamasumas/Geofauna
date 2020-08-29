
#include <Adafruit_BME280.h>
#include <HardwareSerial.h>
#include <TinyGPS++.h>
#include <ComunicationManager.h>
#include <Adafruit_Sensor.h>

//Constants
//Bluetooth transmission led
#define TRANSMISSION_LED 27
//Sea level pressure
#define SEALEVELPRESSURE_HPA 1013.25

//Sensors
//BME
Adafruit_BME280 bme;
//GPS variables
HardwareSerial serial_conection(1); //RX = 9 ,RT= 10,
TinyGPSPlus gps;
//Bluetooth
ComunicationManager comunicationManager = ComunicationManager();


// Functions
void loop();
void setup();
void updateGPS();
void updateBME();
void updateUV();
