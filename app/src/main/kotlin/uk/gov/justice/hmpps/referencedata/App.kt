package uk.gov.justice.hmpps.referencedata

import kotlin.system.exitProcess

data class Result(val register: Register, val problems: Problems) {
    fun hasErrors(): Boolean {
        return problems.errors.isNotEmpty()
    }
}

fun main(vararg args: String) {
    val ref = args.firstOrNull() ?: "origin/main"
    val registers = VersionedRegisters.fromPreviousCommit(ref)
    val results = registers.map { register ->
        val compatibility = CheckCompatibility(register.previousContent, register.currentContent).check()
        Result(register, compatibility)
    }

    println("🔍 Checking if register files are backwards compatible with '$ref'...")
    results.forEach(::printResult)

    if (results.any(Result::hasErrors)) {
        exitProcess(1)
    }
}

fun printResult(r: Result) {
    print("${r.register.path}:")
    val errors = r.problems.errors
    if (errors.isEmpty()) {
        println(" ✅ pass")
    } else {
        println()
        errors.forEach { println("  ❗ error: $it") }
    }
}
