
#include <ComunicationManager.h>
#include <SoftwareSerial.h>
#include <Arduino.h>

ComunicationManager::ComunicationManager(int tx, int rx, int extraPin): mySerial(2,3){
  mySerial.begin(9600); 
  Serial.begin(9600);
  pinHelpingLed = extraPin;
  pinMode(pinHelpingLed, OUTPUT);
}

void ComunicationManager::checkForImcoming(){
  //Serial.println(mySerial.available());
    if(mySerial.available()){
      int dataRecieved = mySerial.read() ;
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
    if(sensorIndex == FINISH_COMUNICATION)
    {
      digitalWrite(pinHelpingLed, LOW);
      Serial.println("Transmission finished");
      sendingData = false;
    } else {
      sendingData = true;
      if(sensorIndex == LAT_POS)
        digitalWrite(pinHelpingLed, HIGH);

      if(sensorData[sensorIndex] == NULL)
        mySerial.println('e');
      else{  
        Serial.println("Sending: " + String(sensorData[sensorIndex]));
        mySerial.println( String(sensorData[sensorIndex]));
      }
    }
   
   
}
bool ComunicationManager::isSendingData(){
  return sendingData;
}

 


