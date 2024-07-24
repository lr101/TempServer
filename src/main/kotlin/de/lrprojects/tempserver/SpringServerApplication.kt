package de.lrprojects.tempserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TempServerApplication
fun main(args: Array<String>) {
    runApplication<TempServerApplication>(args = args)
}