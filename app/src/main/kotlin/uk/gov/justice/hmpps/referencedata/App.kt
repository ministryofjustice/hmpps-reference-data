package uk.gov.justice.hmpps.referencedata

import java.io.FileReader
import java.io.StringReader
import kotlin.system.exitProcess

data class Result(val register: Register, val compatibility: Compatibility) {
    fun hasErrors(): Boolean {
        return compatibility.errors.isNotEmpty()
    }
}

fun main(vararg args: String) {
    val ref = args.firstOrNull() ?: "origin/main"
    val registers = VersionedRegisters.fromPreviousCommit(ref)
    val results = registers.map { register ->
        val previousVersion = StringReader(register.content)
        val currentVersion = FileReader(register.path)
        Result(register, CheckCompatibility(previousVersion, currentVersion).check())
    }

    println("🔍 Checking if register files are backwards compatible with '$ref'...")
    results.forEach(::printResult)

    if (results.any(Result::hasErrors)) {
        exitProcess(1)
    }
}

fun printResult(r: Result) {
    print("${r.register.path}:")
    val errors = r.compatibility.errors
    if (errors.isEmpty()) {
        println(" ✅ pass")
    } else {
        println()
        errors.forEach { println("  ❗ error: $it") }
    }
}
