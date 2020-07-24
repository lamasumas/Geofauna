
#include <ComunicationManager.h>
#include <SoftwareSerial.h>
#include <Arduino.h>

ComunicationManager::ComunicationManager(int tx, int rx) : mySerial(tx, rx)
{
    mySerial.begin(9600); 
    Serial.begin(9600);
    

}

void ComunicationManager::checkForImcoming(){
    if(mySerial.available()){
        int dataRecieved = mySerial.read() - '0';
        Serial.println("Recieved: " + dataRecieved);
        transmitInfo( dataRecieved);
    }

}

void ComunicationManager::updateData(int sensorIndex, double newData ){
    sensorData[sensorIndex] = newData;
}

void ComunicationManager::transmitInfo(int sensorIndex){
    mySerial.flush();
    Serial.println("Sending: " + String(sensorData[sensorIndex]));
    mySerial.println( String(sensorData[sensorIndex]));
}

 /*
 Antigo codigo en loop
 
 char bluetoothData;
 
if (mySerial.available()){
  bluetoothData=mySerial.read();
  if(bluetoothData == START_SENDING && !startSendingData )
  {
    Serial.println("DATA RECEIVED:");
    digitalWrite(HELPING_LED, 1);
    startSendingData = true;
  }
  
  if(selector >=2)
    selector = 0;

  if(selector == 0){
    mySerial.flush();
    mySerial.write("test");
    Serial.println("test");
    selector++;
  }
  else {
    mySerial.flush();
    mySerial.write("test2");
    Serial.println("test2");
    selector++;
    
  }*/




