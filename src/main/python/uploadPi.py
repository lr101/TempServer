# IMPORTANT: this file has to be in your devices home directory to work (~/)

import os
import time
import requests

host = "localhost:8080"
sleep = 0


def post_http(url, body):
    requests.post(url, json=body, timeout=2.50)


def get_http(url):
    return (requests.get(url, timeout=2.50)).json()


class Sensor:

    def __init__(self, _id, _path, _repetitions):
        self.id = _id
        self.path = _path
        self.repetitions = _repetitions
        self.values = []

    def get_avg(self):
        if len(self.values) == self.repetitions:
            return sum(self.values) / len(self.values)
        return -1

    def set_value(self, value):
        self.values.append(value)
        print(self.values)
        if len(self.values) == self.repetitions:
            url = "http://" + host + "/sensors/post/" + self.id
            json = {"row_id": 0, "entryValue": self.get_avg(), "date": None}
            post_http(url, json)
            self.values = []


def get_sensors():
    global sleep
    sleep = 0
    sensor_list = []
    try:
        for x in os.listdir("/sys/bus/w1/devices"):
            if (x.split("-")[0] == "28") or (x.split("-")[0] == "10"):  # check if name matches sensorID criteria
                sensor_id = x.split("-")[0] + x.split("-")[1]
                # POST
                url = "http://" + host + "/sensors/id/"
                json = {"id": 0, "sensorId": sensor_id, "sensorNick": "newSensor", "sensorType": {
                    "id": 0,
                    "sensorType": "Default",
                    "unit": "dUnit",
                    "repetitions": 10,
                    "sleepTime": 10
                }}
                post_http(url, json)
                # GET
                url = "http://" + host + "/sensors/id/" + sensor_id
                json = get_http(url)
                sensor = Sensor(json["sensorId"], x, json["sensorType"]["repetitions"])
                sensor_list.append(sensor)
                sleep += int(json["sensorType"]["sleepTime"])
    except Exception as e:
        print("Could not read directory: %s" % str(e))
        return []

    sleep = sleep / len(sensor_list) / 1000
    return sensor_list


def get_value(filename):
    try:
        file = open("/sys/bus/w1/devices/" + filename + "/w1_slave")
        content = file.read()
        file.close()
        return float('%6.2f' % (float((content.split("\n")[1].split(" ")[9])[2:]) / 1000))
    except Exception as e:
        print("Error while reading value: %s" % str(e))
        return -1


def loop():
    global sleep
    sensor_list = get_sensors()
    if len(sensor_list) > 0:
        while True:
            for sensor in sensor_list:
                sensor.set_value(get_value(sensor.path))
            time.sleep(sleep)


loop()
