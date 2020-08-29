
#include<ComunicationManager.h>
#include<Arduino.h>
ComunicationManager::ComunicationManager(){
Serial.begin(115200);
Serial.println("Starting BLE work!");
BLEDevice::init("Pokedex");
BLEServer *pServer = BLEDevice::createServer();
BLEService *pService = pServer->createService(SERVICE_UUID);
BLECharacteristic *pCharacteristic = pService->createCharacteristic(
                                     CHARACTERISITC_UUID,
                                     BLECharacteristic::PROPERTY_READ |
                                     BLECharacteristic::PROPERTY_WRITE
                                     );
pCharacteristic -> setValue("Hello ble world");
pService->start();
BLEAdvertising *pAdvertising = pServer->getAdvertising();
pAdvertising->start();

}

void ComunicationManager::checkForImcoming(){

}

void ComunicationManager::updateData(int sensorIndex, double newData ){
}

void ComunicationManager::transmitInfo(int sensorIndex){   
   
}
bool ComunicationManager::isSendingData(){
    return sendingData;
}

 


