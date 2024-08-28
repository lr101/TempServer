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
    INFLUXDB_ORG=<org>
   ```
   Fill in your own values for connecting to existing postgresdb or influxdb instances. Create your own influxdb token via the influx ui.
2. Copy the `docker-compose.yml` file into the same folder
3. Run the docker compose file (remove db or influxdb services if not needed):
   ```shell
   docker compose up -d
   ```

## Dev 

Ardiono IDE ESP8266 Library: http://arduino.esp8266.com/stable/package_esp8266com_index.json

Tile CSS: https://1linelayouts.glitch.me/