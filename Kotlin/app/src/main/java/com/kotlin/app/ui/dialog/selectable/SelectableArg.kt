package com.kotlin.app.ui.dialog.selectable

data class SelectableArg(
        val key: String,
        val title: String?,
        val isSearchable: Boolean,
        val adapter: SelectableAdapter<*>
)