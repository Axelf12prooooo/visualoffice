package app.visualoffice.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FileKindTest {
    @Test
    fun detectsKindFromExtensionIgnoringCase() {
        assertEquals(FileKind.DOCUMENT, FileKind.fromName("Rapport.DOCX"))
        assertEquals(FileKind.SPREADSHEET, FileKind.fromName("budget.csv"))
        assertEquals(FileKind.CODE, FileKind.fromName("main.kt"))
        assertEquals(FileKind.PDF, FileKind.fromName("facture.pdf"))
    }

    @Test
    fun unknownOrMissingExtensionIsOther() {
        assertEquals(FileKind.OTHER, FileKind.fromName("Makefile"))
        assertEquals(FileKind.OTHER, FileKind.fromName(".gitignore"))
        assertEquals(FileKind.OTHER, FileKind.fromName("archive.zip"))
    }

    @Test
    fun textPreviewOnlyForPlainTextFormats() {
        assertTrue(FileKind.isTextPreviewable("notes.md"))
        assertTrue(FileKind.isTextPreviewable("data.csv"))
        assertTrue(FileKind.isTextPreviewable("index.html"))
        assertFalse(FileKind.isTextPreviewable("rapport.docx"))
        assertFalse(FileKind.isTextPreviewable("tableur.xlsx"))
    }
}
