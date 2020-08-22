

#include <SoftwareSerial.h>
class ComunicationManager
{
private:
    double sensorData[7] = {};
    bool sendingData = false;
    SoftwareSerial mySerial;
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

    void updateData(int, double);
    void checkForImcoming();
    bool isSendingData();
    
};