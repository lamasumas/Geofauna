
#include<ComunicationManager.h>



    BLEServer *pServer;
    BLEService *pService;
    BLECharacteristic *pHumidityCharacteristic;
    BLECharacteristic *pTemperatureCharacteristic;
    BLECharacteristic *pAltitudeCharacteristic;
    BLECharacteristic *pLongitudeCharacteristic;
    BLECharacteristic *pLatitudeCharacteristic;
    BLECharacteristic *pPressureCharacteristic;
    BLECharacteristic *pUVCharacteristic;

void bleSetUp(){
    BLEDevice::init("Pokedex");
    pServer = BLEDevice::createServer();
    pService = pServer->createService(SERVICE_UUID);
    pHumidityCharacteristic = setupCharacteristic( HUMIDITY_UUID, pService);
    pTemperatureCharacteristic = setupCharacteristic( TEMPERATURE_UUID, pService);
    pPressureCharacteristic = setupCharacteristic( PRESSURE_UUID, pService);
    pUVCharacteristic = setupCharacteristic( UV_UUID, pService);
    pLatitudeCharacteristic = setupCharacteristic( LATITUDE_UUID, pService);
    pLongitudeCharacteristic = setupCharacteristic( LONGITUDE_UUID, pService);
    
    pService->start();
    BLEAdvertising *pAdvertising = pServer->getAdvertising();

    pAdvertising->start();


}

void updateData( const char* sensorUUID, double newData ){

char x[30];
dtostrf(newData,10,20, x);
pService->getCharacteristic(sensorUUID)->setValue(x);
}


BLECharacteristic* setupCharacteristic( const char* specificUUID, BLEService* theService){
    BLECharacteristic* tempChar = theService->createCharacteristic(
                                        specificUUID,
                                        BLECharacteristic::PROPERTY_READ  | 
                                        BLECharacteristic::PROPERTY_NOTIFY
                                        );
    tempChar->setValue("e");
    return tempChar;
}

 


