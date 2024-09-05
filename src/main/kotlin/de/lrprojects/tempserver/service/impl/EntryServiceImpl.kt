package de.lrprojects.tempserver.service.impl

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.QueryApi
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import com.influxdb.exceptions.BadRequestException
import de.lrprojects.tempserver.config.InfluxProperties
import de.lrprojects.tempserver.entity.Entry
import de.lrprojects.tempserver.service.api.EntryService
import org.hibernate.query.sqm.tree.SqmNode.log
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class EntryServiceImpl(
    @Qualifier("influxDBClient") private val influxDBClient: InfluxDBClient,
    @Qualifier("influxDBSampledClient") private val influxDBSampledClient: InfluxDBClient,
    private val influxProperties: InfluxProperties
): EntryService {

    override fun getEntries(sensorId: String, date1: OffsetDateTime?, date2: OffsetDateTime?, limit: Int?, interval: Int?): List<Entry> {
        val toDate = date1?.toString() ?: "now()"
        val fromDate = date2?.toString() ?:  "0"

        val selectedBucket = selectBucket(date2)
        val bucketName = if (selectedBucket) influxProperties.bucket else influxProperties.bucketSampled
        val bucketQueryApi = if (selectedBucket) influxDBClient.queryApi else influxDBSampledClient.queryApi

        val query = if (limit != null) {
            getLimit(bucketName, sensorId, toDate, fromDate, limit)
        } else if (interval != null) {
            aggregatedMean(bucketName, sensorId, toDate, fromDate, interval)
        } else {
            getEntries(bucketName, sensorId, toDate, fromDate)
        }

        try {
            log.info("Run getEntries query: $query")
            val tables = bucketQueryApi.query(query)
            return tables.flatMap { table ->
                table.records.map {
                    Entry(
                        timestamp = OffsetDateTime.ofInstant(it.time, ZoneOffset.UTC),
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
            .time(entry.timestamp?.toInstant(), WritePrecision.S)

        influxDBClient.writeApiBlocking.writePoint(point)
    }

    override fun deleteEntries(sensorId: String, date1: OffsetDateTime?, date2: OffsetDateTime?) {
        val toDate = date1?.toString()?:  "now()"
        val fromDate = date2?.toString() ?: "0"

        val deleteQuery = """
            from(bucket: "${influxProperties.bucket}")
              |> range(start: $fromDate, stop: $toDate)
              |> filter(fn: (r) => r.sensorId == "$sensorId")
              |> drop()
        """.trimIndent()

        influxDBClient.queryApi.query(deleteQuery)
    }


    private fun selectBucket(date2: OffsetDateTime?): Boolean {
        return date2 != null && OffsetDateTime.now().minusDays(influxProperties.retentionPeriod.toLong()).isBefore(date2)
    }

    companion object {
        fun aggregatedMean(bucket: String, sensorId: String, toDate: String, fromDate: String, interval: Int) = """
            from(bucket: "$bucket")
                |> range(start: $fromDate, stop: $toDate)
                |> filter(fn: (r) => r["sensorId"] == "$sensorId")
                |> group(columns: ["sensorId", "_field"])
                |> aggregateWindow(every: ${interval}s, fn: mean, createEmpty: false)
                |> yield(name: "mean")
        """
        fun getEntries(bucket: String, sensorId: String, toDate: String, fromDate: String) = """
            from(bucket: "$bucket")
                |> range(start: $fromDate, stop: $toDate)
                |> filter(fn: (r) => r["sensorId"] == "$sensorId")
            """

        fun getLimit(bucket: String, sensorId: String, toDate: String, fromDate: String, limit: Int) = """
            from(bucket: "$bucket")
                |> range(start: $fromDate, stop: $toDate)
                |> filter(fn: (r) => r["sensorId"] == "$sensorId")
                |> limit(n: $limit)
            """
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}