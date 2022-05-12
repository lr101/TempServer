package com.example.SpringServer.Controller;

import com.example.SpringServer.Entities.Category;
import com.example.SpringServer.Entities.Id;
import com.example.SpringServer.Entities.Type;
import com.example.SpringServer.repository.CategoryRepository;
import com.example.SpringServer.repository.SensorRepository;
import com.example.SpringServer.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class ViewController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    SensorRepository sensorRepo;

    @Autowired
    TypeRepository typeRepo;

    @GetMapping("/")
    public String index (Model model) {
        ArrayList<Id> sensors = (ArrayList<Id>) sensorRepo.findAll();
        model.addAttribute("sensors",sensors);
        return "index";
    }

    @GetMapping("/display")
    public String display (@RequestParam String sensor_id, Model model) {
        ArrayList<Id> sensors = (ArrayList<Id>) sensorRepo.findAll();
        Id sensor = sensorRepo.findById(sensor_id);
        model.addAttribute("sensors",sensors);
        model.addAttribute("thisSensor", sensor);
        return "display";
    }

    @GetMapping("/config/local-sensors")
    public String configLocalSensors(Model model) {
        ArrayList<Id> sensors = (ArrayList<Id>) sensorRepo.findAll();
        model.addAttribute("sensors", sensors);
        return "config-local-sensors";
    }

    @GetMapping("/config/sensor-types")
    public String configSensorTypes(Model model) {
        ArrayList<Type> types = (ArrayList<Type>) typeRepo.findAll();
        ArrayList<Id> sensors = (ArrayList<Id>) sensorRepo.findAll();
        model.addAttribute("types", types);
        model.addAttribute("sensors", sensors);
        return "config-sensor-types";
    }

    @GetMapping("/config/sensor-categories")
    public String configSensorCategories(Model model) {
        ArrayList<Category> types = (ArrayList<Category>) categoryRepository.findAll();
        ArrayList<Id> sensors = (ArrayList<Id>) sensorRepo.findAll();
        model.addAttribute("categories", types);
        model.addAttribute("sensors", sensors);
        return "config-sensor-categories";
    }

}
