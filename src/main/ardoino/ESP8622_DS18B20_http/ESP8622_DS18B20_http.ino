#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <Arduino_JSON.h>
#include <WiFiClientSecure.h>
#include <WiFiUdp.h>
#include "config.h"
//Define GPIO pin D2 as Data-pin
#define ONE_WIRE_BUS 4

// define time libary
#define NTP_OFFSET   0      // In seconds
#define DAYLIGHT_OFFSET 0    // In miliseconds
#define NTP_ADDRESS  "europe.pool.ntp.org"

String convertSensorID(DeviceAddress);

struct sensorStruct {
    int repetitions;
    String sensor_id;
    float* values;
    int numValue = 0;
    float* curValue;
};

int avgSleepTime = 200;

// Setup a oneWire instance to communicate with any OneWire devices 
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

// Number of temperature devices found
int numberOfDevices;

//store a found device address
DeviceAddress tempDeviceAddress; 
struct sensorStruct *infos;


void setup() {
  /*
   * Setup WiFi-Connection
   **/
  Serial.begin(9600);
  delay(100);
 
  //Connect to WiFi verbinden
  Serial.print("Connect to Wifi: "); 
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  //trying to connect
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  //Wifi connected
  Serial.println("");
  Serial.println("WiFi connected");
 

  // Print the IP address
  Serial.print("IP Address: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/");
  
  // start time client
  configTime(NTP_OFFSET, DAYLIGHT_OFFSET, NTP_ADDRESS);    
  while (!time(nullptr)) {
    Serial.println("Waiting for time");
    delay(1000);
  } 
  
  /*
   * Setup Thermometers
   * Send Thermometer serial number to server to setup db
   **/
   
  // Start up the library
  sensors.begin();
  
  // Grab a count of devices on the wire
  numberOfDevices = sensors.getDeviceCount();
  infos = new struct sensorStruct[numberOfDevices];
  struct sensorStruct* p = infos;

  //Send to Server
   for(int i=0;i<numberOfDevices; i++){
    
    if(sensors.getAddress(tempDeviceAddress, i)){
      String sensor_id = convertSensorID(tempDeviceAddress);
      /**
       * POST sensor serial numbers to webServer
       **/
       WiFiClient client;
       HTTPClient http;
      
      //Connect to WebServer:
      if(http.begin(client, ("http://" + host + "/rest/v1/sensors").c_str())){
        // we are connected to the host!
        http.addHeader("Content-Type", "application/json");
        Serial.println(sensor_id);//Send the request
        String json = "{\"sensorId\":\"" + sensor_id + "\",\"sensorNick\":\"newSensor\",\"sensorType\": \"Thermometer\"}";
        int httpCode = http.POST(json);
        infos[i].repetitions = 10;
        infos[i].sensor_id = sensor_id;
        infos[i].values = new float[10];
        infos[i].curValue = infos[i].values;    
        
        if (httpCode > 0) { //Check the returning code
           http.end();
           //get Information of Sensor
           //http.begin(client, ("http://" + host + "/sensors/id/" + sensor_id).c_str());
           //int httpCode = http.GET();
           //if (httpCode == HTTP_CODE_OK) {
           //   JSONVar myObject = JSON.parse(http.getString());
           //   infos[i].repetitions = (int) myObject["sensorType"]["repetitions"];
           //   avgSleepTime += (int) myObject["sensorType"]["sleepTime"];
           //   infos[i].sensor_id = sensor_id;
           //   infos[i].values = new float[infos[i].repetitions];
           //   infos[i].curValue = infos[i].values;
           // }
           // http.end();
         }
      } else {
        // connection failure
        http.end();
        Serial.println("Connection failure");
        exit(0);
      }
    }
   }
   avgSleepTime /= numberOfDevices;
}

void loop() {
  // put your main code here, to run repeatedly:
  /**
   * Read Temperatur:
   **/
  sensors.requestTemperatures(); // Send the command to get temperatures
  
  
  // Loop through each device, print out temperature data
  for(int i=0;i<numberOfDevices; i++){
    // Search the wire for address
    if(sensors.getAddress(tempDeviceAddress, i)){
      *(infos[i].curValue) = sensors.getTempC(tempDeviceAddress);
      (infos[i].curValue)++;
      (infos[i].numValue)++;
      
      /*
       * Send temp to server via http POST
       **/
      if(infos[i].numValue == infos[i].repetitions) {
        infos[i].numValue = 0;
        infos[i].curValue = infos[i].values;
        
        WiFiClient client;
        HTTPClient http;
      
        //Connect to WebServer:
        if (http.begin(client,"http://" + host + "/rest/v1/sensors/" + infos[i].sensor_id +  "/entry")){
          // we are connected to the host!
          http.addHeader("Content-Type", "application/json");
          String json = "{\"value\":\"" + (String)getAvg(infos[i].values, infos[i].repetitions) + "\",\"timestamp\": \"" +  getISOTime() +"\" }";
          Serial.println(json);
          int httpCode = http.POST(json);                                  //Send the request
        } else {
          Serial.println("Connection failure");
          http.end();
          exit(0);
        }
        http.end();   //Close connection
      }
    } else {
      exit(0);
    }
  } 
  delay(avgSleepTime);
}

float getAvg(float* p, int repetitions) {
    float tempC = 0;
    for (int y=0; y<repetitions;y++, p++) {
       tempC += *p;
    }
    return tempC / repetitions;
  }

// function to print a device address
String convertSensorID(DeviceAddress deviceAddress) {
  String adress;
  for (uint8_t i = 0; i < 8; i++)
  {
    if (deviceAddress[i] < 0x10) {
      adress += "0";
    }
    adress += decToHexa(deviceAddress[i]);
  }
 
  return adress;
}


//function to calculate sensor serial number from dec to hex
String decToHexa(int n)
{
    // char array to store hexadecimal number
    char hexaDeciNum[100];
 
    // counter for hexadecimal number array
    int i = 0;
    while (n != 0) {
        // temporary variable to store remainder
        int temp = 0;
 
        // storing remainder in temp variable.
        temp = n % 16;
 
        // check if temp < 10
        if (temp < 10) {
            hexaDeciNum[i] = temp + 48;
            i++;
        }
        else {
            hexaDeciNum[i] = temp + 55;
            i++;
        }
 
        n = n / 16;
    }
    String hex;
    // printing hexadecimal number array in reverse order
    for (int j = i - 1; j >= 0; j--)
        hex += hexaDeciNum[j];

    return hex;
}

String getISOTime() {
  time_t now;
  struct tm timeinfo;
  char buffer[25];

  time(&now);
  gmtime_r(&now, &timeinfo); // Convert to UTC time
  strftime(buffer, sizeof(buffer), "%Y-%m-%dT%H:%M:%SZ", &timeinfo);

  return String(buffer);
}
