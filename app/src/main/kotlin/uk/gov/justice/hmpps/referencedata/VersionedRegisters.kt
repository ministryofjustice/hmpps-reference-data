package uk.gov.justice.hmpps.referencedata

import java.io.File
import java.io.FileNotFoundException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

data class Register(val path: String, val previousContent: String, val currentContent: String?)

class VersionedRegisters {
    companion object {
        fun fromPreviousCommit(shaOrRef: String): List<Register> {
            val r = ProcessBuilder("git", "rev-parse", "--show-toplevel").start()
            r.waitFor(10, TimeUnit.SECONDS)
            val root = r.inputStream.bufferedReader().readText().trim()

            val c = ProcessBuilder("git", "ls-tree", "-r", "--full-tree", "--name-only", shaOrRef)
            val p = c.start()
            p.waitFor(10, TimeUnit.SECONDS)
            if (p.exitValue() != 0) {
                throw RuntimeException("Command ${c.command()} failed with exit status ${p.exitValue()}")
            }

            val registries = mutableListOf<Register>()
            for (path in p.inputStream.bufferedReader().lines()) {
                if (!path.endsWith(".csv", true)) {
                    continue
                }
                if (path.contains("test/resources/")) {
                    continue
                }
                registries.add(Register(path, checkoutFile(shaOrRef, path), readFile(root, path)))
            }

            return registries
        }

        private fun readFile(root: String, path: String): String? {
            return try {
                File(root, path).readText()
            } catch (e: FileNotFoundException) {
                null
            }
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
