
#include <ComunicationManager.h>

BLEServer *pServer;
BLEService *pService;
BLECharacteristic *pHumidityCharacteristic;
BLECharacteristic *pTemperatureCharacteristic;
BLECharacteristic *pAltitudeCharacteristic;
BLECharacteristic *pLongitudeCharacteristic;
BLECharacteristic *pLatitudeCharacteristic;
BLECharacteristic *pPressureCharacteristic;
BLECharacteristic *pUVCharacteristic;

/**
 *  Esta función configura el ESP32 para que se inicie el ble
 *  Primero crea una dispositivo ble, luego un servidor, a continuación una caracteristica
 *  por sensor y por ultimo los hace visibles a los demas dispositivos
 * */
void bleSetUp()
{
    BLEDevice::init("Fauna Salvaje");
    pServer = BLEDevice::createServer();
    pService = pServer->createService(SERVICE_UUID);

    pHumidityCharacteristic = setupCharacteristic(HUMIDITY_UUID, pService);
    pTemperatureCharacteristic = setupCharacteristic(TEMPERATURE_UUID, pService);
    pPressureCharacteristic = setupCharacteristic(PRESSURE_UUID, pService);
    pUVCharacteristic = setupCharacteristic(UV_UUID, pService);
    pLatitudeCharacteristic = setupCharacteristic(LATITUDE_UUID, pService);
    pLongitudeCharacteristic = setupCharacteristic(LONGITUDE_UUID, pService);

    pService->start();
    pServer->getAdvertising()->addServiceUUID(SERVICE_UUID);
    pServer->getAdvertising()->start();
}

/**
 * Transforma los valores de los sensores a char* para luego actualizar los valores
 *  de una caracteristica concreta. Finalmente notifica del cambio si hay alguien escuchando.
 * */
void updateData(const char *sensorUUID, double newData, int numberOfDecimals)
{

    char x[20];
    if (numberOfDecimals > 10 || numberOfDecimals < 0)
        dtostrf(newData, 10, 10, x);
    else
        dtostrf(newData, 10, numberOfDecimals, x);

    pService->getCharacteristic(sensorUUID)->setValue(x);
    pService->getCharacteristic(sensorUUID)->notify();
}

/**
 * Configuración base de cada caracteritica. Se especifica un UUID concreto.
 * */
BLECharacteristic *setupCharacteristic(const char *specificUUID, BLEService *theService)
{
    BLECharacteristic *tempChar = theService->createCharacteristic(
        specificUUID,
        BLECharacteristic::PROPERTY_READ |
            BLECharacteristic::PROPERTY_NOTIFY);
    tempChar->setValue("e");
    return tempChar;
}
