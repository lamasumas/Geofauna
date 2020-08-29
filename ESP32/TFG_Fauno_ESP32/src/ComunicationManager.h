#include<BLEDevice.h>
#include<BLESecurity.h>
#include<WString.h>
#include<BLEUtils.h>
class ComunicationManager
{
private:
    double sensorData[7] = {};
    bool sendingData = false;
    void transmitInfo(int);
    
public:
    ComunicationManager();
    const char* SERVICE_UUID = "c953d4c6-5270-4bd9-83f4-b4d1b6a5b0da";
    const char* CHARACTERISITC_UUID= "be47856e-30c0-46db-85fc-cde641b51553";
    const int LAT_POS = 0;
    const int LON_POS = 1;
    const int ALT_POS = 2;
    const int HUM_POS = 3;
    const int UV_POS = 4;
    const int PRESS_POS = 5;
    const int TEMP_POS = 6;
    const int FINISH_COMUNICATION = 7;
    int pinHelpingLed;

    void updateData(int, double);
    void checkForImcoming();
    bool isSendingData();
    
};