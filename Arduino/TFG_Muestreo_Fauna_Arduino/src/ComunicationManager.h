

#include "SoftwareSerial.h"
class ComunicationManager
{
private:
    double sensorData[7] = {99, 99,2,3,4,5,6};
    SoftwareSerial mySerial;
    bool sendingData = false;
    void transmitInfo(int);
    
public:
    ComunicationManager(int, int, int);
    const int LAT_POS = 0;
    const int LON_POS = 1;
    const int ALT_POS = 2;
    const int HUM_POS = 3;
    const int UV_POS = 4;
    const int PRESS_POS = 5;
    const int TEMP_POS = 6;
    const int FINISH_COMUNICATION = 7;
    int pinHelpingLed;
    const int NO_DATA = 999;

    void updateData(int, double);
    void checkForImcoming();
    bool isSendingData();
    
};