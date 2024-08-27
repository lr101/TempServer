#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <Arduino_JSON.h>
#include <WiFiClientSecure.h>
#include "config.h"

//Define GPIO pin D2 as Data-pin
#define ONE_WIRE_BUS 4

String convertSensorID(DeviceAddress);

struct sensorStruct {
    int repetitions;
    String sensor_id;
    float* values;
    int numValue = 0;
    float* curValue;
};

int avgSleepTime;

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
  ping(host.c_str());
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
      sensor_id = "s" + sensor_id;
      /**
       * POST sensor serial numbers to webServer
       **/
      WiFiClientSecure client; //WiFiClientSecure
      client.setInsecure();
      client.connect(host.c_str(), 443);
      HTTPClient http;
      
      
      //Connect to WebServer:
      if(http.begin(client, (host + "/sensors/id/").c_str())){
        // we are connected to the host!
        http.addHeader("Content-Type", "application/json");
        Serial.println(sensor_id);//Send the request
        String json = "{\"sensorId\":\"" + sensor_id + "\",\"sensorNick\":\"newSensor\" ,\"id\": -1,\"sensorType\": {"
        + "\"id\": 0, \"sensorType\": \"Default\", \"unit\": \"dUnit\", \"repetitions\": 10,\"sleepTime\": 10"
        + "}}";
        int httpCode = http.POST(json);     
        
        if (httpCode > 0) { //Check the returning code
           http.end();
           //get Information of Sensor
           http.begin(client, (host + "/sensors/id/" + sensor_id).c_str());
           int httpCode = http.GET();
           if (httpCode == HTTP_CODE_OK) {
              JSONVar myObject = JSON.parse(http.getString());
              infos[i].repetitions = (int) myObject["sensorType"]["repetitions"];
              avgSleepTime += (int) myObject["sensorType"]["sleepTime"];
              infos[i].sensor_id = sensor_id;
              infos[i].values = new float[infos[i].repetitions];
              infos[i].curValue = infos[i].values;
              Serial.println("Repetitions: " );
              Serial.println(infos[i].repetitions);
              Serial.println("Average sleep Time: ");
              Serial.println(avgSleepTime);
              Serial.println("New value every (in seconds): ");
              Serial.println((avgSleepTime * infos[i].repetitions + (infos[i].repetitions * numberOfDevices * 400)) / 1000); // 450ms is the average time to read a sensor value
            }
            http.end();
         }
      } else {
        // connection failure
        Serial.println("Connection failure");
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
        
        WiFiClientSecure client; 
        client.setInsecure();
        client.connect(host.c_str(), 80);
        HTTPClient http;
       
      
        //Connect to WebServer:
        if (http.begin(client, host + "/sensors/post/" + infos[i].sensor_id + "/")){
          // we are connected to the host!
          http.addHeader("Content-Type", "application/json");
          String json = "{\"row_id\":0 ,\"entryValue\":\"" + (String)getAvg(infos[i].values, infos[i].repetitions) + "\",\"date\": null }";
          Serial.println(json);
          int httpCode = http.POST(json);
        } else {Serial.println("Connection failure");}
        http.end();   //Close connection
      }
    }
  } 
  delay(avgSleepTime);
  // ping(host.c_str());
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

void ping(const char* pinHost) {
  
  WiFiClientSecure client2; //WiFiClientSecure
  client2.setInsecure();
  client2.connect(pinHost, 443);
  HTTPClient http;
  Serial.println(pinHost);
  http.begin(client2, pinHost);
  http.GET();
  http.end();
  Serial.println("done");
  client2.stop();
}
