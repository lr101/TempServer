package de.lrprojects.tempserver.service.impl

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import de.lrprojects.tempserver.config.InfluxProperties
import de.lrprojects.tempserver.entity.Entry
import de.lrprojects.tempserver.service.api.EntryService
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter

@Service
class EntryServiceImpl(
    private val influxDBClient: InfluxDBClient,
    private val influxProperties: InfluxProperties
): EntryService {

    override fun getEntries(sensorId: String, date1: Long?, date2: Long?, limit: Int?): List<Entry> {
        val query = StringBuilder("from(bucket: \"${influxProperties.bucket}\") |> range(start: -1h)")
        query.append(" |> filter(fn: (r) => r.sensorId == \"$sensorId\")")

        if (date1 != null) {
            query.append(" |> filter(fn: (r) => r._time >= ${formatTimestamp(date1)})")
        }
        if (date2 != null) {
            query.append(" |> filter(fn: (r) => r._time <= ${formatTimestamp(date2)})")
        }
        if (limit != null) {
            query.append(" |> limit(n: $limit)")
        }

        val fluxQuery = query.toString()
        val queryApi = influxDBClient.queryApi
        val tables = queryApi.query(fluxQuery)

        return tables.flatMap { table ->
            table.records.map {
                Entry(
                    timestamp = it.time?.toEpochMilli()?.let { it1 -> Timestamp(it1) },
                    value = it.value as Double,
                    sensorId = sensorId
                )
            }
        }
    }

    override fun createEntry(sensorId: String, entry: Entry) {
        val point = Point.measurement("entry")
            .addTag("sensorId", sensorId)
            .addField("value", entry.value)
            .time(entry.timestamp?.toInstant(), WritePrecision.NS)

        influxDBClient.writeApiBlocking.writePoint(point)
    }

        override fun deleteEntries(sensorId: String, date1: Long?, date2: Long?) {
        val startDate = date1?.let { formatTimestamp(it) } ?: "0"
        val stopDate = date2?.let { formatTimestamp(it) } ?: "now()"

        val deleteQuery = """
            from(bucket: "${influxProperties.bucket}")
              |> range(start: $startDate, stop: $stopDate)
              |> filter(fn: (r) => r.sensorId == "$sensorId")
              |> drop()
        """.trimIndent()

        influxDBClient.queryApi.query(deleteQuery)
    }

    private fun formatTimestamp(epochMillis: Long): String {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(epochMillis))
    }
}