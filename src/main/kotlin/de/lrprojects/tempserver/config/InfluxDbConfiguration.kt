package de.lrprojects.tempserver.config

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InfluxDbConfiguration(private val influxProperties: InfluxProperties) {

    @Bean
    fun influxDBClient(): InfluxDBClient? {
        return if (influxProperties.enabled) {
            InfluxDBClientFactory.create(
                influxProperties.url,
                influxProperties.token.toCharArray(),
                influxProperties.org,
                influxProperties.bucket
            )
        } else {
            null
        }
    }
}