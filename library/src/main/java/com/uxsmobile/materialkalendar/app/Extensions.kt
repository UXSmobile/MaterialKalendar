package com.uxsmobile.materialkalendar.app

import android.content.res.Resources
import com.uxsmobile.materialkalendar.presentation.ui.MaterialKalendar
import java.util.Random

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    19/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun IntRange.random() = Random().nextFloat()

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? =
        if (p1 != null && p2 != null) block(p1, p2) else null

fun MaterialKalendar.ShowingDateModes.shouldShowNonCurrentMonths(): Boolean {
    return this.value.and(MaterialKalendar.ShowingDateModes.NON_CURRENT_MODES.value) != 0
}

fun MaterialKalendar.ShowingDateModes.shouldShowOutOfCalendarRangeDates(): Boolean {
    return this.value.and(MaterialKalendar.ShowingDateModes.OUT_OF_CALENDAR_DATE_RANGE.value) != 0
}

fun MaterialKalendar.ShowingDateModes.shouldShowDefaultDates(): Boolean {
    return this.value.and(MaterialKalendar.ShowingDateModes.DEFAULT.value) != 0
}

fun MaterialKalendar.ShowingDateModes.shouldShowAllDates(): Boolean {
    return this.value.xor(MaterialKalendar.ShowingDateModes.ALL.value) == 0
}