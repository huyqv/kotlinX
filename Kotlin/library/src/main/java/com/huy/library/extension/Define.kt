package com.huy.library.extension

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/04
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class Language constructor(val id: String, val country: String, val flag: Int)

fun defineLanguage() = listOf(
        Language("ar", "العربية", -1),  // Arabic
        Language("zh", "中文", -1),   // Chinese
        Language("en", "English", -1),// English
        Language("es", "Español", -1),// Spanish
        Language("fr", "Français", -1),// French
        Language("de", "Deutsch", -1),// German
        Language("id", "Indonesian", -1),// Indonesian
        Language("ja", "にほんご", -1),// Japanese
        Language("ko", "한국어", -1),// Korean
        Language("ru", "русский", -1),// Russian
        Language("th", "ไทย", -1),// 	Thai
        Language("tr", "Türkçe", -1),// Turkish
        Language("vi", "Tiếng Việt", -1)// Vietnamese
)

fun defineColors() = listOf("#000000", "#424242", "#636363", "#9C9C94", "#CEC6CE", "#EFEFEF", "#F7F7F7",
        "#FFFFFF", "#FF0000", "#FF9C00", "#FFFF00", "#00FF00", "#00FFFF", "#0000FF", "#9C00FF",
        "#FF00FF", "#F7C6CE", "#FFE7CE", "#FFEFC6", "#D6EFD6", "#CEDEE7", "#CEE7F7", "#D6D6E7",
        "#E7D6DE", "#E79C9C", "#FFC69C", "#FFE79C", "#B5D6A5", "#A5C6CE", "#9CC6EF", "#B5A5D6",
        "#D6A5BD", "#E76363", "#F7AD6B", "#FFD663", "#94BD7B", "#73A5AD", "#6BADDE", "#8C7BC6",
        "#C67BA5", "#CE0000", "#E79439", "#EFC631", "#6BA54A", "#4A7B8C", "#3984C6", "#634AA5",
        "#A54A7B", "#9C0000", "#B56308", "#BD9400", "#397B21", "#104A5A", "#085294", "#311873",
        "#731842", "#630000", "#7B3900", "#846300", "#295218", "#083139", "#003163", "#21104A",
        "#4A1031")
