package de.lrprojects.tempserver.service.impl

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import com.influxdb.exceptions.BadRequestException
import de.lrprojects.tempserver.config.InfluxProperties
import de.lrprojects.tempserver.entity.Entry
import de.lrprojects.tempserver.service.api.EntryService
import org.jetbrains.kotlin.preloading.ProfilingInstrumenterExample.e
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Service
class EntryServiceImpl(
    private val influxDBClient: InfluxDBClient,
    private val influxProperties: InfluxProperties
): EntryService {

    override fun getEntries(sensorId: String, date1: OffsetDateTime?, date2: OffsetDateTime?, limit: Int?, interval: Int?): List<Entry> {
        val startDate = date1?.toString() ?: "0"
        val stopDate = date2?.toString() ?: "now()"
        val query = if (limit != null) {
            getLimit(influxProperties.bucket, sensorId, startDate, stopDate, limit)
        } else if (interval != null) {
            aggregatedMean(influxProperties.bucket, sensorId, startDate, stopDate, interval)
        } else {
            getEntries(influxProperties.bucket, sensorId, startDate, stopDate)
        }

        val queryApi = influxDBClient.queryApi
        try {
            val tables = queryApi.query(query)
            return tables.flatMap { table ->
                table.records.map {
                    Entry(
                        timestamp = it.time?.toEpochMilli()?.let { it1 -> Timestamp(it1) },
                        value = it.value as Double,
                    )
                }
            }
        } catch (e: BadRequestException) {
            return emptyList()
        }
    }

    override fun createEntry(sensorId: String, entry: Entry) {
        val point = Point.measurement("entry")
            .addTag("sensorId", sensorId)
            .addField("value", entry.value)
            .time(entry.timestamp?.toInstant(), WritePrecision.NS)

        influxDBClient.writeApiBlocking.writePoint(point)
    }

        override fun deleteEntries(sensorId: String, date1: OffsetDateTime?, date2: OffsetDateTime?) {
        val startDate = date1?.toString()?: "0"
        val stopDate = date2?.toString() ?: "now()"

        val deleteQuery = """
            from(bucket: "${influxProperties.bucket}")
              |> range(start: $startDate, stop: $stopDate)
              |> filter(fn: (r) => r.sensorId == "$sensorId")
              |> drop()
        """.trimIndent()

        influxDBClient.queryApi.query(deleteQuery)
    }

    companion object {
        fun aggregatedMean(bucket: String, sensorId: String, date1: String, date2: String, interval: Int) = """
            from(bucket: "$bucket")
                |> range(start: $date1, stop: $date2)
                |> filter(fn: (r) => r["sensorId"] == "$sensorId")
                |> group(columns: ["sensorId", "_field"])
                |> aggregateWindow(every: $interval, fn: mean, createEmpty: false)
                |> yield(name: "mean")
        """
        fun getEntries(bucket: String, sensorId: String, date1: String, date2: String) = """
            from(bucket: "$bucket")
                |> range(start: $date1, stop: $date2)
                |> filter(fn: (r) => r["sensorId"] == "$sensorId")
            """

        fun getLimit(bucket: String, sensorId: String, date1: String, date2: String, limit: Int) = """
            from(bucket: "$bucket")
                |> range(start: $date1, stop: $date2)
                |> filter(fn: (r) => r["sensorId"] == "$sensorId")
                |> limit(n: $limit)
            """

    }
}