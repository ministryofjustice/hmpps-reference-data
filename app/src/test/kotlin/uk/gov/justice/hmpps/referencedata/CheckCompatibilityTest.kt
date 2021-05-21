package uk.gov.justice.hmpps.referencedata

import java.io.FileReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckCompatibilityTest {
    @Test
    fun addingNewColumnIsAllowed() {
        val compatibility = checkCompatibility("/new_column_v1.csv", "/new_column_v2.csv")
        assertTrue(compatibility.errors.isEmpty())
    }

    @Test
    fun addingNewRowIsAllowed() {
        val compatibility = checkCompatibility("/new_row_v1.csv", "/new_row_v2.csv")
        assertTrue(compatibility.errors.isEmpty())
    }

    @Test
    fun changingAContentFieldIsAllowed() {
        val compatibility = checkCompatibility("/change_content_v1.csv", "/change_content_v2.csv")
        assertTrue(compatibility.errors.isEmpty())
    }

    @Test
    fun removingAFileIsDisallowed() {
        val compatibility = CheckCompatibility(fixture("/no_longer_exists_v1.csv"), null).check()
        assertEquals(
            listOf("no longer exists"),
            compatibility.errors
        )
    }

    @Test
    fun removingAColumnIsDisallowed() {
        val compatibility = checkCompatibility("/rename_column_v1.csv", "/rename_column_v2.csv")
        assertEquals(
            listOf("previously used column cannot be removed: valid_untll"),
            compatibility.errors
        )
    }

    @Test
    fun removingARowIsDisallowed() {
        val compatibility = checkCompatibility("/remove_row_v1.csv", "/remove_row_v2.csv")
        assertEquals(
            listOf("previously used row cannot be removed: row with ID `A` is missing"),
            compatibility.errors
        )
    }

    @Test
    fun changingAPrimaryKeyIsDisallowed() {
        val compatibility = checkCompatibility("/change_primary_key_v1.csv", "/change_primary_key_v2.csv")
        assertEquals(
            listOf("previously used row cannot be removed: row with ID `A` is missing"),
            compatibility.errors
        )
    }

    private fun checkCompatibility(before: String, after: String): Compatibility =
        CheckCompatibility(fixture(before), fixture(after)).check()

    private fun fixture(filename: String): String = FileReader(javaClass.getResource(filename).file).readText()
}
