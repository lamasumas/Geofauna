

#include "SoftwareSerial.h"
class ComunicationManager
{
private:
    double sensorData[8] = {0,0,0,0,0,0,0};
    SoftwareSerial mySerial;
public:
    ComunicationManager(int, int);
    const int LAT_POS = 0;
    const int LON_REQ = 1;
    const int ALT_REQ = 2;
    const int HUM_REQ = 3;
    const int UV_REQ = 4;
    const int PRESS_REQ = 5;
    const int TEMP_SENDING = 6;


    void updateData(int, double);
    void transmitInfo(int);
    void checkForImcoming();
    
};