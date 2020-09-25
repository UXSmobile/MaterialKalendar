package com.uxsmobile.materialkalendar.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import android.view.Gravity
import android.view.View
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarWeekDayDateFormatter
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
                          private val weekDay: DayOfWeek,
                          private var formatter: DateFormatter<DayOfWeek> = KalendarWeekDayDateFormatter()) : AppCompatTextView(context) {

    init {
        gravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        setTextColor(Color.WHITE)
    }

    fun setWeekDayFormatter(formatter: DateFormatter<DayOfWeek>?) {
        this.formatter = formatter ?: KalendarWeekDayDateFormatter()
        setDayOfWeek(weekDay)
    }

    private fun setDayOfWeek(weekDay: DayOfWeek) {
        text = formatter.format(weekDay)
    }

}