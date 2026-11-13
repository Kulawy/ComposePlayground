package com.jakger.feature_ghcrange

import java.math.RoundingMode

fun Double?.toDisplayString(): String =
    this?.toBigDecimal()?.setScale(2, RoundingMode.HALF_UP)
        ?.stripTrailingZeros()?.toPlainString() ?: "?"

fun Double.toDisplayString(): String = this.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
    .stripTrailingZeros().toPlainString()