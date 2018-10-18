package com.uxsmobile.materialkalendar.ui

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.view.Gravity
import android.view.View
import com.uxsmobile.materialkalendar.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.ui.common.formatter.KalendarWeekDayDateFormatter
import org.threeten.bp.DayOfWeek

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    17/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
@SuppressLint("ViewConstructor")
class KalendarWeekDayView(context: Context,
                          private var weekDay: DayOfWeek,
                          private var formatter: DateFormatter<DayOfWeek> = KalendarWeekDayDateFormatter()) : AppCompatTextView(context) {

    init {
        gravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    fun setWeekDayFormatter(formatter: DateFormatter<DayOfWeek>?) {
        this.formatter = formatter ?: KalendarWeekDayDateFormatter()
        setDayOfWeek(weekDay)
    }

    fun setDayOfWeek(weekDay: DayOfWeek) {
        this.weekDay = weekDay
        text = formatter.format(weekDay)
    }

}