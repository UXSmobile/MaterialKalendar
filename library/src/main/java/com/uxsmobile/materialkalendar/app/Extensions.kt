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

fun Int.shouldShowDefaultDates(): Boolean {
    return this.and(MaterialKalendar.SHOWING_MODE_DEFAULT) != 0
}

fun Int.shouldShowNonCurrentMonths(): Boolean {
    return this.and(MaterialKalendar.SHOWING_MODE_NON_CURRENT_MONTHS) != 0
}

fun Int.shouldShowOutOfCalendarRangeDates(): Boolean {
    return this.and(MaterialKalendar.SHOWING_MODE_OUT_OF_CALENDAR_DATE_RANGE) != 0
}

fun Int.shouldShowAllDates(): Boolean {
    return this.xor(MaterialKalendar.SHOWING_MODE_ALL) == 0
}