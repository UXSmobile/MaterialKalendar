package com.uxsmobile.materialkalendar.ui.common.dateRange

import com.uxsmobile.materialkalendar.data.KalendarDay
import org.threeten.bp.Period

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    18/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class MonthlyDateRange(minDateRange: KalendarDay,
                       private val maxDateRange: KalendarDay) : DateRangeIndex {

    private var minDateInFirstMonthToShow: KalendarDay = KalendarDay.from(minDateRange.date.year,
                                                                          minDateRange.date.monthValue,
                                                                          1)
    override val count: Int
        get() = indexOf(maxDateRange) + 1

    override fun indexOf(day: KalendarDay): Int {
        return Period.between(minDateInFirstMonthToShow.date.withDayOfMonth(1),
                              day.date.withDayOfMonth(1)).toTotalMonths().toInt()
    }

    override fun getItem(position: Int): KalendarDay {
        return KalendarDay.from(minDateInFirstMonthToShow.date.plusMonths(position.toLong()))
    }

}