package com.example.SpringServer.Schedular;
import com.example.SpringServer.Entities.Entry;
import com.example.SpringServer.Entities.Id;
import com.example.SpringServer.Entities.JDBC;
import com.example.SpringServer.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Component
@EnableScheduling
public class DailyExecution {

    JDBC jdbc = new JDBC();
    private final static int DAYS = 7;

    @Autowired
    SensorRepository sensorRepo;

    @Scheduled(cron="0 0 0 * * *")
    public void moveToBackup() throws SQLException {
        ArrayList<Id> sensors = (ArrayList<Id>) sensorRepo.findAll();
        for (Id sensor : sensors) {
            ArrayList<Entry> entries = jdbc.selectEntriesDate1(sensor.getSensorId(), new Timestamp(new Date().getTime() - (long)DAYS*1000*60*60*24));
            System.out.println(entries.size());
            jdbc.insertEntryBatch(entries, sensor.getSensorId());
            jdbc.deleteEntryDate1(sensor.getSensorId(), new Timestamp(new Date().getTime() - (long)DAYS*1000*60*60*24));
        }
    }

}