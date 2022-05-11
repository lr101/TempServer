#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include <Arduino_JSON.h>
#include <WiFiClientSecure.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include "config.h"

//Define GPIO pin D6 as Data-pin
#define ONE_WIRE_BUS 12
#define DHTTYPE DHT11     // DHT 11
const String ID[] = {"28AJOIFJOIJ1", "28AJOIFJOIJ2"};

DHT dht(ONE_WIRE_BUS, DHTTYPE);
  

struct sensorStruct {
    int repetitions;
    String sensor_id;
    float* values;
    int numValue = 0;
    float* curValue;
};

int avgSleepTime;


// Number of temperature devices found
int numberOfDevices = 2;

struct sensorStruct *infos;


void setup() {
  /*
   * Setup WiFi-Connection
   **/
  Serial.begin(9600);
  delay(100);
  dht.begin();
 
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

  infos = new struct sensorStruct[numberOfDevices];
  struct sensorStruct* p = infos;
       
  /*
   * Setup Thermometers
   * Send Thermometer serial number to server to setup db
   **/
  for (int i = 0; i < numberOfDevices; i++) {
    String sensor_id = ID[i];
    /**
     * POST sensor serial numbers to webServer
     **/
     WiFiClient client;
     HTTPClient http;
    
    //Connect to WebServer:
    if(http.begin(client, ("http://" + host + "/sensors/id/").c_str())){
      // we are connected to the host!
      http.addHeader("Content-Type", "application/json");
        Serial.println(sensor_id);//Send the request
        String json = "{\"sensorId\":\"" + sensor_id + "\",\"sensorNick\":\"newSensor\" ,\"id\": -1,\"sensorType\": {"
        + "\"id\": 0, \"sensorType\": \"Default\", \"unit\": \"dUnit\", \"repetitions\": 10,\"sleepTime\": 10"
        + "}}";
        int httpCode = http.POST(json);    
        Serial.println(httpCode);
        if (httpCode > 0) { //Check the returning code
           http.end();
           //get Information of Sensor
           http.begin(client, ("http://" + host + "/sensors/id/" + sensor_id).c_str());
           int httpCode = http.GET();
           if (httpCode == HTTP_CODE_OK) {
              JSONVar myObject = JSON.parse(http.getString());
              Serial.println(myObject);
              infos[i].repetitions = (int) myObject["sensorType"]["repetitions"];
              avgSleepTime += (int) myObject["sensorType"]["sleepTime"];
              infos[i].sensor_id = sensor_id;
              infos[i].values = new float[infos[i].repetitions];
              infos[i].curValue = infos[i].values;
            }
            http.end();
         
       }
    } else {
      // connection failure
      http.end();
      Serial.println("Connection failure");
      exit(0);
    }
  }
   avgSleepTime /= numberOfDevices;
}

void loop() {
  // Loop through each device, print out temperature data
  for(int i=0;i<numberOfDevices; i++){
    // Search the wire for address
    if(i == 0){
      *(infos[i].curValue) = dht.readTemperature();
    } else if (i == 1){
      *(infos[i].curValue) = dht.readHumidity();
    }
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
    
      if (http.begin(client,"http://" + host + "/sensors/post/" + infos[i].sensor_id + "/")){
          // we are connected to the host!
          http.addHeader("Content-Type", "application/json");
          String json = "{\"row_id\":0 ,\"entryValue\":\"" + (String)getAvg(infos[i].values, infos[i].repetitions) + "\",\"date\": null }";
          Serial.println(json);
          int httpCode = http.POST(json);                                  //Send the request
        } else {
          http.end();
          Serial.println("Connection failure");
          exit(0);
        }
        http.end();   //Close connection
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
