package com.uxsmobile.materialkalendar.presentation.ui.common.formatter

import org.threeten.bp.DayOfWeek

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    19/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class ArrayKalendarWeekDayDateFormatter(private val weekDayLabels: Array<CharSequence>?) : DateFormatter<DayOfWeek> {

    init {
        weekDayLabels?.let {
            if (it.size != 7) {
                throw IllegalArgumentException("Array must contain exactly 7 elements")
            }
        } ?: throw IllegalArgumentException("Cannot be null")

    }

    override fun format(date: DayOfWeek): CharSequence = weekDayLabels?.get(date.value - 1) ?: ""

}