package uk.gov.justice.hmpps.referencedata

import com.opencsv.CSVReader
import java.io.Reader

class CheckCompatibility(releasedVersion: Reader, currentVersion: Reader) {
    private val errors: MutableList<String> = mutableListOf()
    private var vReleased: MutableList<List<String>>
    private var vCurrent: MutableList<List<String>>
    private var requiredHeaders: List<String>
    private var newHeaders: List<String>

    init {
        vReleased = readAll(releasedVersion)
        vCurrent = readAll(currentVersion)
        requiredHeaders = vReleased.removeFirst()
        newHeaders = vCurrent.removeFirst()
        errors.addAll(verifyHeaders())
        errors.addAll(verifyContent())
    }

    fun hasError(): Boolean {
        return errors.isNotEmpty()
    }

    fun errors(): List<String> {
        return errors
    }

    private fun verifyHeaders(): List<String> {
        val errors = arrayListOf<String>()

        val removedHeaders = requiredHeaders.subtract(newHeaders)
        for (h in removedHeaders) {
            errors.add("previously used column cannot be removed: $h")
        }

        return errors
    }

    private fun verifyContent(): List<String> {
        val errors = arrayListOf<String>()

        val releasedIds = vReleased.map { row -> row.first() }
        val currentIds = vCurrent.map { row -> row.first() }

        val removedIds = releasedIds.subtract(currentIds)
        for (removedId in removedIds) {
            errors.add("previously used row cannot be removed: row with ID `$removedId` is missing")
        }

        return errors
    }

    private fun readAll(r: Reader): MutableList<List<String>> {
        val csvr = CSVReader(r)
        val rows = mutableListOf<List<String>>()

        var l = csvr.readNext()
        while (l != null) {
            rows.add(l.toList())
            l = csvr.readNext()
        }

        return rows
    }
}
