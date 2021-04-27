package uk.gov.justice.hmpps.referencedata

import java.io.File
import java.util.concurrent.TimeUnit

data class Register(val path: String, val content: String)

class VersionedRegisters {
    companion object {
        fun fromPreviousCommit(shaOrRef: String): List<Register> {
            val p = ProcessBuilder("git", "ls-tree", "-r", "--full-tree", "--name-only", shaOrRef).start()
            p.waitFor(10, TimeUnit.SECONDS)

            val registries = mutableListOf<Register>()
            for (path in p.inputStream.bufferedReader().lines()) {
                if (!path.endsWith(".csv", true)) {
                    continue
                }
                if (path.contains("test/resources/")) {
                    continue
                }
                registries.add(Register(path, checkoutFile(shaOrRef, path)))
            }

            return registries
        }

        private fun checkoutFile(shaOrRef: String, path: String): String {
            val tempfile = File.createTempFile("reference-data", "")
            tempfile.deleteOnExit()

            val p = ProcessBuilder("git", "show", "$shaOrRef:$path")
                .redirectOutput(tempfile)
                .start()
            p.waitFor(10, TimeUnit.SECONDS)

            return tempfile.readText()
        }
    }
}
