package com.uxsmobile.materialkalendar.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate

/**
 * @author   Daniel Manrique <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    10/10/2018.
 */
@Parcelize
data class KalendarDay(val date: LocalDate) : Parcelable {

    companion object {

        @JvmStatic
        fun today() = KalendarDay(LocalDate.now())

        @JvmStatic
        fun from(year: Int, month: Int, day: Int) = KalendarDay(
                LocalDate.of(year, month, day))

    }

    fun isBefore(dateToCompare: KalendarDay) = date.isBefore(dateToCompare.date)

    fun isAfter(dateToCompare: KalendarDay) = date.isAfter(dateToCompare.date)

    fun isInDateRange(minDayRange: KalendarDay, maxDayRange: KalendarDay) : Boolean {
        return !minDayRange.isAfter(this) && !maxDayRange.isBefore(this)
    }

}