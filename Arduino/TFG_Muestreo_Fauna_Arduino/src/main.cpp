#include <Arduino.h>
#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <Adafruit_BME280.h>
#include <Adafruit_Sensor.h>
#include <SoftwareSerial.h>

//Definicion de pines
#define GPS_RX 8
#define GPS_RT 7

//Helping led
#define HELPING_LED 9

// Constantes
#define SEALEVELPRESSURE_HPA (1013.25)

#define CONECTED '1'


//UV
int sensorVoltage;
float sensorValue;
//GPS variables
//SoftwareSerial serial_conection(GPS_RX, GPS_RT);
//TinyGPSPlus gps;

// BME280
Adafruit_BME280 bme;

//Bluetooth
SoftwareSerial mySerial(2,3); //TX , RX
bool startSendingData = false;
int selector = 0;
char *sensorInfo[] = { "test 1", "test 2"};




void setup() {
  Serial.begin(9600);


  //
  

  //GPS
  //serial_conection.begin(9600);
  //Serial.println("GPS Start");
  
  //BME208
  /*if (!bme.begin(0x76)) {
		Serial.println("Could not find a valid BME280 sensor, check wiring!");
		while (1);
	}*/

mySerial.begin(9600);
pinMode(HELPING_LED, OUTPUT);
Serial.println("Start");

// Helping led


}


void loop() {
  char bluetoothData;
 
if (mySerial.available()){
  bluetoothData=mySerial.read();
  Serial.println(bluetoothData);
  if(bluetoothData == CONECTED )
  {
    Serial.println("DATA RECEIVED:");
    digitalWrite(HELPING_LED, 1);
    startSendingData = true;
  }
  if(startSendingData)
  {
    mySerial.flush();
    mySerial.write(sensorInfo[selector]);
  }
  selector+= 1;
  if(selector >=2)
    selector = 0;
}








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

  */
 
 // UV
//Serial.println(UVIndex());
  



}





/**
 * 
 * Code from chansheunglong
 * https://github.com/chansheunglong/GUVA-S12SD-lib/blob/master/GUVA_S12SD/GUVA_S12SD.h
 * 
 * */
int UVIndex() {
      int s12sd_sensorVoltage = analogRead(A0) ;
      Serial.print("test: " );
      Serial.println(s12sd_sensorVoltage);
      s12sd_sensorVoltage = s12sd_sensorVoltage/ 1024 * 3.3;
 
      if (s12sd_sensorVoltage * 1000 < 50) {
        return 0;
      } else if (s12sd_sensorVoltage * 1000 < 227) {
        return 1;
      } else if (s12sd_sensorVoltage * 1000 < 318) {
        return 2;
      } else if (s12sd_sensorVoltage * 1000 < 408) {
        return 3;
      } else if (s12sd_sensorVoltage * 1000 < 503) {
        return 4;
      } else if (s12sd_sensorVoltage * 1000 < 606) {
        return 5;
      } else if (s12sd_sensorVoltage * 1000 < 696) {
        return 6;
      } else if (s12sd_sensorVoltage * 1000 < 795) {
        return 7;
      } else if (s12sd_sensorVoltage * 1000 < 881) {
        return 8;
      } else if (s12sd_sensorVoltage * 1000 < 976) {
        return 9;
      } else if (s12sd_sensorVoltage * 1000 < 1079) {
        return 10;
      } else {
        return 11;
      }
}
