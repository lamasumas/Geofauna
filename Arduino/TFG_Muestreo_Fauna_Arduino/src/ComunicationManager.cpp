
#include <ComunicationManager.h>
#include <SoftwareSerial.h>
#include <Arduino.h>

ComunicationManager::ComunicationManager(int tx, int rx, int extraPin) : mySerial(tx,rx)
{
    mySerial.begin(9600); 
    Serial.begin(9600);
    pinHelpingLed = extraPin;
    pinMode(pinHelpingLed, OUTPUT);

    

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
    Serial.println("Sensor index: " + String(sensorIndex));
    Serial.println("Termination symbol: " + String(FINISH_COMUNICATION));
    sendingData = true;
    if(sensorIndex == FINISH_COMUNICATION)
    {
      digitalWrite(pinHelpingLed, 0);
      Serial.println("Transmission finished");
      sendingData = false;
    } else {

      if(sensorIndex == LAT_POS)
        digitalWrite(pinHelpingLed, 1);

      Serial.println("Sending: " + String(sensorData[sensorIndex]));
      mySerial.println( String(sensorData[sensorIndex]));
    }
   
   
}
bool ComunicationManager::isSendingData(){
  return sendingData;
}

 


