#include <Arduino.h>
#include <main.h>

void setup() {

  Serial.begin(115200);
  
  //GPS
  serial_conection.begin(9600, SERIAL_8N1, 16,17);
  //BME208
  if (!bme.begin(0x76)) {
		Serial.println("Could not find a valid BME280 sensor, check wiring!");
		while (1);
	}
comunicationManager.checkForImcoming();
}

void loop() {
 
  //updateGPS();
  //updateBME();
  //updateUV();

  //checkForImcoming();
  //if(!comunicationManager.isSendingData())
 //   delay(100);
}

void updateGPS(){
  if(serial_conection.available()){
    gps.encode(serial_conection.read());
    }
  if(gps.location.isUpdated())
  {
    //Leyendo latitude 
   // comunicationManager.updateData(comunicationManager.LAT_POS, gps.location.lat());
    //Leyendo longitud
    //comunicationManager.updateData(comunicationManager.LON_POS, gps.location.lng());
    //Leyendo altiud manera de leer la altura
    //comunicationManager.updateData(comunicationManager.ALT_POS, gps.altitude.meters());
  }
}

void updateBME(){
 
  // BME208: Temperatura - Presion - Humedad

  //Leyendo temperatura en  ºC
 // comunicationManager.updateData(comunicationManager.TEMP_POS, bme.readTemperature());
  //Leyendo presion en hPa
 // comunicationManager.updateData(comunicationManager.PRESS_POS, bme.readPressure() / 100.0F);
  //Manera alternativa para leer altitud en m, pero muy imprecisa ya que la pression a nivel del mar
  //cambia dependiendo del dia y lugar, mejor usar gps
  //comunicationManager.updateData(comunicationManager.ALT_POS, bme.readAltitude(SEALEVELPRESSURE_HPA));
  //Read Hummidity in %
 // comunicationManager.updateData(comunicationManager.HUM_POS, bme.readHumidity());

}


void updateUV(){
  float rvVoltage = analogRead(32) ;
  rvVoltage = rvVoltage/ 4095 * 3.3;
  int switchStatement = rvVoltage * 1000;
  Serial.println(switchStatement);
  //Switch para determinar el indice UV
  /*
  switch (switchStatement)
  {
  case 0 ... 49:
   // comunicationManager.updateData(comunicationManager.UV_POS, 0);
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
    if (switchStatement >= 1079){
      comunicationManager.updateData(comunicationManager.UV_POS, 11);
    }else{
      comunicationManager.updateData(comunicationManager.UV_POS, 0);
    }
    break;
  }*/
}