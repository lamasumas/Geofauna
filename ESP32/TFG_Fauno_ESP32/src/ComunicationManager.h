#include<BLEDevice.h>
#include<BLESecurity.h>
#include<BLEUtils.h>
#include<Arduino.h>
   #define SERVICE_UUID "c953d4c6-5270-4bd9-83f4-b4d1b6a5b0da"
    #define HUMIDITY_UUID  "be478561-30c0-46db-85fc-cde641b51553"
    #define TEMPERATURE_UUID  "be478562-30c0-46db-85fc-cde641b51553"
    #define PRESSURE_UUID "be478563-30c0-46db-85fc-cde641b51553"
    #define ALTITUDE_UUID "be478564-30c0-46db-85fc-cde641b51553"
    #define LONGITUDE_UUID "be478565-30c0-46db-85fc-cde641b51553"
    #define LATITUDE_UUID "be478566-30c0-46db-85fc-cde641b51553"
    #define UV_UUID "be478560-30c7-46db-85fc-cde641b51553"


    void bleSetUp();
    BLECharacteristic* setupCharacteristic(const char*, BLEService*);
    void updateData(const char*, double);
 