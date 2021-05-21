package uk.gov.justice.hmpps.referencedata

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VersionedRegistersTest {
    private val firstRepoCommitWithRegister: String = "b8478787640aebddc15d9b80dd3ea6cde6541b44"
    private val commitWithTestFixtures: String = "2cd90c5ddcf897ab8ac3173ea2c4fd1aa490888b"

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
        val regions = registers.find { r -> r.path == "probation-regions-v0.csv" }
        assertNotNull(regions)

        val currentContent = regions.currentContent
        assertNotNull(currentContent)

        val content = currentContent.trim().split("\n")
        assertEquals("id,name", content.first())
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
