# TempServer

This services allows saving, configuring and displaying of sensor values. The sensor type, name and its categories are saved in a postgresdb,
the values of the different sensors are saved inside a influxdb.

The website uses plain html, css and javascript and can be accessed on port 8080. 

## Prod Setup

This service is run via docker:

1. Create `.env` file in a folder:
    ```
    POSTGRES_USER=<db_user>
    POSTGRES_PASSWORD=<db_password>
    DB_URL=jdbc:postgresql://db:5432/tempserver
    INFLUXDB_URL=http://indluxdb:8086
    INFLUXDB_TOKEN=<influx_token>
    INFLUXDB_BUCKET=tempserver
    INFLUXDB_DOWNSAMPLED_BUCKET=tempserver_sampled
    INFLUXDB_ORG=<org>
    INFLUXDB_RETENTION_PERIOD=7 # in days
   
    # optional
    MAX_THREADS=5
    MAX_CONNECTIONS=200
   ```
   Fill in your own values for connecting to existing postgresdb or influxdb instances. Create your own influxdb token via the influx ui.
2. Copy the `docker-compose.yml` file into the same folder
3. Run the docker compose file (remove db or influxdb services if not needed):
   ```shell
   docker compose up -d
   ```
   
### Influxdb

- Set up a bucket (ex. tempserver) and a bucket for down-sampling (ex. tempserver_sampled)
- Create an access token to allow access to read and write to these buckets
- Create an influx task to down-sample and run it every 24h to aggregate the last day: 
   ```
   option task = {name: "Aggregate Tempserver 24h", every: 24h}

   data =
   from(bucket: "tempserver")
   |> range(start: -1d, stop: now())
   |> filter(fn: (r) => r._measurement == "entry")
   
   data
   |> aggregateWindow(fn: mean, every: 1h)
   |> to(bucket: "tempserver_sampled", org: "lr-projects")

  ```
- Setup the tempserver retention policy to delete values after a specific time period (to keep data size small)
- Make sure the `INFLUX_RETENTION_PERIOD` value in your .env is the same (or smaller) to have available data 

## Arduino

1. Driver installation **(specifically for ESP8266 by AZ-Delivery)**: https://www.arduined.eu/ch340-windows-10-driver-download/ 
2. Connect your Arduino
3. Add Device Library under *preferences -> Additional Boards managt by URLs*: http://arduino.esp8266.com/stable/package_esp8266com_index.json
4. Select the Board under: *Board -> esp8266 -> NodeMCU 1.0 (ESP-12E Module)*
5. Select your COM-Port (should show up when installed the driver correctly and a usb cable capable of transferring data is used)
6. Install dependencies (look at the *#includes* and search for them)
7. Rename the `config_template.h` file to `config.h` and fill in your WiFi information
8. Upload and test