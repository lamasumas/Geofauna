#include <Arduino.h>
#include <main.h>



void setup() {

  Serial.begin(9600);
  //GPS
  serial_conection.begin(9600);
  Serial.println("GPS Start");
  
  //BME208
  /*if (!bme.begin(0x76)) {
		Serial.println("Could not find a valid BME280 sensor, check wiring!");
		while (1);
	}*/

//mySerial.begin(9600);
}


void loop() {
 
  //comunicationManager.checkForImcoming();
  updateGPS();
  //updateBME();
  //updateUV();
}

void updateGPS(){
  if(serial_conection.available()){
    gps.encode(serial_conection.read());
    }
  if(gps.location.isUpdated())
  {
    Serial.println("Latitude:");
    Serial.println(gps.location.lat(), 6);
    comunicationManager.updateData(comunicationManager.LAT_POS, gps.location.lat());
    
    Serial.println("Longitude:");
    Serial.println(gps.location.lng(), 6);
    comunicationManager.updateData(comunicationManager.LON_POS, gps.location.lng());
  }
}
void updateBME(){
 
  // BME208: Temperatura - Presion - Humedad
  
  //Read Temperature in ºC
  comunicationManager.updateData(comunicationManager.TEMP_POS, bme.readTemperature());
  //Read Pressure in hPa
  comunicationManager.updateData(comunicationManager.PRESS_POS, bme.readTemperature() / 100.0F);
  //Read Altitude in m
  comunicationManager.updateData(comunicationManager.ALT_POS, bme.readAltitude(SEALEVELPRESSURE_HPA));
  //Read Hummidity in %
  comunicationManager.updateData(comunicationManager.HUM_POS, bme.readHumidity());

}



/**
 * Based in code from chansheunglong
 * https://github.com/chansheunglong/GUVA-S12SD-lib/blob/master/GUVA_S12SD/GUVA_S12SD.h
 * 
 * */
void updateUV(){
  int s12sd_sensorVoltage = analogRead(A0) ;
  s12sd_sensorVoltage = s12sd_sensorVoltage/ 1024 * 3.3;
  switch (s12sd_sensorVoltage * 1000)
  {
  case 0 ... 49:
    comunicationManager.updateData(comunicationManager.UV_POS, 0);
    break;
  case 50 ... 226:
    comunicationManager.updateData(comunicationManager.UV_POS, 1);
    break;
  case 227 ... 317:
    comunicationManager.updateData(comunicationManager.UV_POS, 2);
    break;
  case 318 ... 407:
    comunicationManager.updateData(comunicationManager.UV_POS, 3);
    break;
  case 408 ... 502:
    comunicationManager.updateData(comunicationManager.UV_POS, 4);
    break;
  case 503 ... 605:
    comunicationManager.updateData(comunicationManager.UV_POS, 5);
    break;
  case 606 ...695:
    comunicationManager.updateData(comunicationManager.UV_POS, 6);
    break;
  case 696 ... 794:
    comunicationManager.updateData(comunicationManager.UV_POS, 7);
    break;
  case 795 ... 880:
    comunicationManager.updateData(comunicationManager.UV_POS, 8);
    break;
  case 881 ... 975:
    comunicationManager.updateData(comunicationManager.UV_POS, 9);
    break;
  case 976 ... 1078:
    comunicationManager.updateData(comunicationManager.UV_POS, 10);
    break;
  default:
    if (s12sd_sensorVoltage * 1000 >= 1079){
      comunicationManager.updateData(comunicationManager.UV_POS, 11);
    }else{
      comunicationManager.updateData(comunicationManager.UV_POS, 0);
    }
    break;
  }
  
}