package com.example.kotlin.ui.selectable

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
data class SelectableArg(
        val key: String,
        val title: String?,
        val isSearchable: Boolean,
        val adapter: SelectableAdapter<*>
)