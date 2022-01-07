package uk.gov.justice.hmpps.referencedata

import com.opencsv.CSVReader
import java.io.StringReader

data class Problems(val errors: List<String>)

class CheckCompatibility(private val releasedVersion: String, private val currentVersion: String?) {
    fun check(): Problems {
        if (currentVersion == null) {
            return Problems(listOf("no longer exists"))
        }

        val vReleased = readAll(releasedVersion)
        val vCurrent = readAll(currentVersion)
        val releasedHeaders = vReleased.removeFirst()
        val currentHeaders = vCurrent.removeFirst()

        val errors = mutableListOf<String>()
        errors.addAll(verifyHeaders(releasedHeaders, currentHeaders))
        errors.addAll(verifyContent(vReleased.map { it.first() }, vCurrent.map { it.first() }))
        return Problems(errors)
    }

    private fun verifyHeaders(requiredHeaders: List<String>, newHeaders: List<String>): List<String> {
        val errors = arrayListOf<String>()

        val removedHeaders = requiredHeaders.subtract(newHeaders)
        for (h in removedHeaders) {
            errors.add("previously used column cannot be removed: $h")
        }

        return errors
    }

    private fun verifyContent(releasedIds: List<String>, currentIds: List<String>): List<String> {
        val errors = arrayListOf<String>()

        val removedIds = releasedIds.subtract(currentIds)
        for (removedId in removedIds) {
            errors.add("previously used row cannot be removed: row with ID `$removedId` is missing")
        }

        return errors
    }

    private fun readAll(s: String?): MutableList<List<String>> {
        if (s.isNullOrBlank()) {
            return mutableListOf()
        }

        val csvr = CSVReader(StringReader(s))
        val rows = mutableListOf<List<String>>()

        var l = csvr.readNext()
        while (l != null) {
            rows.add(l.toList())
            l = csvr.readNext()
        }

        return rows
    }
}
