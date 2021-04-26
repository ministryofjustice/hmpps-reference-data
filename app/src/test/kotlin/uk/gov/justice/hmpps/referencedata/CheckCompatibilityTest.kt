package uk.gov.justice.hmpps.referencedata

import java.io.FileReader
import java.io.Reader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CheckCompatibilityTest {
    @Test
    fun addingNewColumnIsAllowed() {
        val compatibility = checkCompatibility("/new_column_v1.csv", "/new_column_v2.csv")
        assertFalse(compatibility.hasError())
    }

    @Test
    fun addingNewRowIsAllowed() {
        val compatibility = checkCompatibility("/new_row_v1.csv", "/new_row_v2.csv")
        assertFalse(compatibility.hasError())
    }

    @Test
    fun changingAContentFieldIsAllowed() {
        val compatibility = checkCompatibility("/change_content_v1.csv", "/change_content_v2.csv")
        assertFalse(compatibility.hasError())
    }

    @Test
    fun removingAColumnIsDisallowed() {
        val compatibility = checkCompatibility("/rename_column_v1.csv", "/rename_column_v2.csv")
        assertEquals(
            listOf("previously used column cannot be removed: valid_untll"),
            compatibility.errors()
        )
        assertTrue(compatibility.hasError())
    }

    @Test
    fun removingARowIsDisallowed() {
        val compatibility = checkCompatibility("/remove_row_v1.csv", "/remove_row_v2.csv")
        assertEquals(
            listOf("previously used row cannot be removed: row with ID `A` is missing"),
            compatibility.errors()
        )
        assertTrue(compatibility.hasError())
    }

    @Test
    fun changingAPrimaryKeyIsDisallowed() {
        val compatibility = checkCompatibility("/change_primary_key_v1.csv", "/change_primary_key_v2.csv")
        assertEquals(
            listOf("previously used row cannot be removed: row with ID `A` is missing"),
            compatibility.errors()
        )
        assertTrue(compatibility.hasError())
    }

    private fun checkCompatibility(before: String, after: String): CheckCompatibility =
        CheckCompatibility(fixture(before), fixture(after))

    private fun fixture(filename: String): Reader = FileReader(javaClass.getResource(filename).file)
}
