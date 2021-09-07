package sample.ui.selectable

data class SelectableArg(
        val key: String,
        val title: String?,
        val isSearchable: Boolean,
        val adapter: SelectableAdapter<*>,
)