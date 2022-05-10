package com.example.SpringServer.Controller;

import com.example.SpringServer.Entities.Entry;
import com.example.SpringServer.Entities.JDBC;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class RestControllerEntries {
    JDBC jdbc = new JDBC();

    //working with no params
    @GetMapping("/sensors/{sensorId}")
    public ArrayList<Entry> getEntries(@PathVariable("sensorId") String sensorId, @RequestParam(required = false) Long date1, @RequestParam(required = false) Long date2, @RequestParam(required = false) Integer limit) {
        ArrayList<Entry> rt;
        Timestamp time2 = (date2 == null ? null : new Timestamp(date2));
        Timestamp time1 = (date1 == null ? null : new Timestamp(date1));
        try {
            if (time1 == null && time2 == null) {
                rt = jdbc.selectAllEntries(sensorId);
            } else if (time1 != null && time2 != null) {
                rt = jdbc.selectEntriesBetween(sensorId, time1, time2);
            } else if (time1 != null) {
                rt = jdbc.selectEntriesDate1(sensorId, time1);
            } else {
                if (limit != null) {
                    rt = jdbc.selectFirst(sensorId, time2, limit);
                } else {
                    rt = jdbc.selectEntriesDate2(sensorId, time2);
                }

            }
            return rt;
        }catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    //working
    @PostMapping("sensors/post/{sensorId}/")
    public void postEntry(@PathVariable("sensorId") String sensorId, @RequestBody Entry entry) {
        jdbc.insertEntry(entry, sensorId);
    }

    //working with no params
    @DeleteMapping("sensors/delete/{sensorId}")
    public void deleteEntry (@PathVariable("sensorId") String sensorId, @RequestParam(required = false) Long date1, @RequestParam(required = false) Long date2) {
        Timestamp time2 = (date2 == null ? null : new Timestamp(date2));
        Timestamp time1 = (date1 == null ? null : new Timestamp(date1));
        if (date1 == null && date2 == null) {
            jdbc.deleteAllEntryTable(sensorId);
        } else if (date1 != null && date2 != null) {
            jdbc.deleteEntryBetweenTable(sensorId, time1, time2);
        } else if (date1 != null) {
            jdbc.deleteEntryDate1(sensorId, time1);
        } else {
            jdbc.deleteEntryDate2(sensorId, time2);
        }
    }

    @GetMapping("display/graph")
    public Map<String, Object> getDisplayGraph(@RequestParam String sensor_id, @RequestParam Long date1, @RequestParam Long date2, @RequestParam int interval) {
        Timestamp time2 = (date2 == null ? null : new Timestamp(date2));
        Timestamp time1 = (date1 == null ? null : new Timestamp(date1));
        return jdbc.getDisplayData(sensor_id, interval, time1, time2);
    }
}
