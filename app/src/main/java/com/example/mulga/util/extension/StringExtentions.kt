package com.ilm.mulga.util.extension

import java.text.NumberFormat
import java.util.Locale

fun String.withCommas(): String {
    val number = this.toLongOrNull() ?: return this
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(number)
}
