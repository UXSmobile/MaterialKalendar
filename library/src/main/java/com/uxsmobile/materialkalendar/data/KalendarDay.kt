package com.uxsmobile.materialkalendar.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    10/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
@Parcelize
data class KalendarDay(val date: LocalDate) : Parcelable {

    companion object {

        @JvmStatic
        fun today() = KalendarDay(LocalDate.now())

        @JvmStatic
        fun from(year: Int, month: Int = 1, day: Int = 1) = KalendarDay(
                LocalDate.of(year, month, day))

        @JvmStatic
        fun from(date: LocalDate) = from(date.year, date.monthValue, date.dayOfMonth)

    }

    fun isBefore(dateToCompare: KalendarDay) = date.isBefore(dateToCompare.date)

    fun isAfter(dateToCompare: KalendarDay) = date.isAfter(dateToCompare.date)

    fun isInDateRange(minDayRange: KalendarDay, maxDayRange: KalendarDay) : Boolean {
        return !minDayRange.isAfter(this) && !maxDayRange.isBefore(this)
    }

    fun isToday(): Boolean {
        return this == today()
    }

}