package de.lrprojects.tempserver.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "influxdb")
class InfluxProperties {

    lateinit var url: String
    lateinit var token: String
    lateinit var bucket: String
    lateinit var bucketSampled: String
    lateinit var org: String
    var enabled: Boolean = false
    var retentionPeriod: Int = 7
}
