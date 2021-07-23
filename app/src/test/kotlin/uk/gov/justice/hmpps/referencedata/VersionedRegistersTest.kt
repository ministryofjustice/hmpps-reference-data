package uk.gov.justice.hmpps.referencedata

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VersionedRegistersTest {
    private val firstRepoCommitWithRegister: String = "b8478787640aebddc15d9b80dd3ea6cde6541b44"
    private val commitWithTestFixtures: String = "0c2513d4eea6375442b1d8c1f4b03e5e96579d32"

    @Test
    fun retrievesPreviousVersionsOfRegisters() {
        val registers = VersionedRegisters.fromPreviousCommit(firstRepoCommitWithRegister)
        assertTrue(registers.isNotEmpty())

        val filenames = registers.map(Register::path)
        assertEquals(listOf("probation-regions.csv"), filenames)

        val content = registers.first().previousContent.trim().split("\n")
        assertEquals("id,name", content.first())
        assertEquals("L,\"Greater Manchester\"", content.last())
    }

    @Test
    fun retrievesCurrentLocalVersionsOfRegisters() {
        val registers = VersionedRegisters.fromPreviousCommit(commitWithTestFixtures)
        assertTrue(registers.isNotEmpty())

        // this test needs to change if this file is changed or moved elsewhere
        val regions = registers.find { r -> r.path == "registers/probation-regions-v0.csv" }
        assertNotNull(regions)

        val currentContent = regions.currentContent
        assertNotNull(currentContent)

        val content = currentContent.trim().split("\n")
        assertEquals("id,tom_23032021_id,name", content.first())
    }

    @Test
    fun doesNotRetrieveNonRegisterFiles() {
        val registers = VersionedRegisters.fromPreviousCommit(firstRepoCommitWithRegister)
        assertTrue(registers.isNotEmpty())

        val filenames = registers.map(Register::path)
        assertFalse(filenames.contains("README.md"))
    }

    @Test
    fun doesNotRetrieveTestRegisters() {
        val registers = VersionedRegisters.fromPreviousCommit(commitWithTestFixtures)
        assertTrue(registers.isNotEmpty())

        val filenames = registers.map(Register::path)
        assertFalse(filenames.any { path -> path.contains("new_column_v1.csv") })
    }
}
