package app.visualoffice.domain

/** Catégorie fonctionnelle d'un fichier, déduite de son extension. */
enum class FileKind(val extensions: Set<String>) {
    TEXT(setOf("txt")),
    MARKDOWN(setOf("md", "markdown")),
    DOCUMENT(setOf("docx", "odt")),
    SPREADSHEET(setOf("csv", "xlsx", "ods")),
    PRESENTATION(setOf("pptx", "odp")),
    PDF(setOf("pdf")),
    CODE(setOf("html", "css", "js", "ts", "py", "java", "kt", "c", "cpp", "h", "json", "xml", "php", "sql")),
    OTHER(emptySet());

    companion object {
        private fun extensionOf(name: String): String = name.substringAfterLast('.', "").lowercase()

        fun fromName(name: String): FileKind {
            val ext = extensionOf(name)
            return entries.firstOrNull { ext in it.extensions } ?: OTHER
        }

        /** Vrai si le contenu peut être affiché tel quel comme texte brut. */
        fun isTextPreviewable(name: String): Boolean {
            val kind = fromName(name)
            return kind == TEXT || kind == MARKDOWN || kind == CODE || extensionOf(name) == "csv"
        }
    }
}
