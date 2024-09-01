package de.lrprojects.tempserver.controller

import de.lrprojects.tempserver.service.api.CategoryService
import de.lrprojects.tempserver.service.api.SensorService
import de.lrprojects.tempserver.service.api.TypeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ViewController(
    private val sensorService: SensorService,
    private val typeService: TypeService,
    private val categoryService: CategoryService
) {

    @GetMapping(BASE)
    fun index(model: Model): String {
        val sensors = sensorService.getAllSensors()
        model.addAttribute("sensors", sensors)
        return "index"
    }

    @GetMapping(DISPLAY)
    fun display(@RequestParam("sensor_id") sensorId: String, model: Model): String {
        val sensors = sensorService.getAllSensors()
        val sensor = sensorService.getSensorById(sensorId)
        model.addAttribute("sensors", sensors)
        model.addAttribute("thisSensor", sensor)
        return "display"
    }

    @GetMapping(CONFIG_LOCAL_SENSORS)
    fun configLocalSensors(model: Model): String {
        val sensors = sensorService.getAllSensors()
        model.addAttribute("sensors", sensors)
        return "config-local-sensors"
    }

    @GetMapping(CONFIG_SENSOR_CATEGORIES)
    fun configSensorCategories(model: Model): String {
        val categories = categoryService.getAllCategories()
        val sensors = sensorService.getAllSensors()
        model.addAttribute("categories", categories)
        model.addAttribute("sensors", sensors)
        return "config-sensor-categories"
    }

    @GetMapping(CONFIG_SENSOR_TYPES)
    fun configSensorTypes(model: Model): String {
        val types = typeService.getAllTypes()
        val sensors = sensorService.getAllSensors()
        model.addAttribute("types", types)
        model.addAttribute("sensors", sensors)
        return "config-sensor-types"
    }

    companion object {
        const val BASE = "/"
        const val DISPLAY = "/display"
        const val CONFIG_LOCAL_SENSORS = "/config/local-sensors"
        const val CONFIG_SENSOR_TYPES = "/config/sensor-types"
        const val CONFIG_SENSOR_CATEGORIES = "/config/sensor-categories"
    }
}
