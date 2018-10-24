package com.uxsmobile.materialkalendar.presentation.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.uxsmobile.library.R
import com.uxsmobile.materialkalendar.app.random
import com.uxsmobile.materialkalendar.app.shouldShowAllDates
import com.uxsmobile.materialkalendar.app.shouldShowDefaultDates
import com.uxsmobile.materialkalendar.app.shouldShowNonCurrentMonths
import com.uxsmobile.materialkalendar.app.shouldShowOutOfCalendarRangeDates
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.data.KalendarDayViewData
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarDayDateFormatter
import kotlinx.android.synthetic.main.view_calendar_day.view.dayMovementsBarChart
import kotlinx.android.synthetic.main.view_calendar_day.view.dayNumber

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    16/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class KalendarDayView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val colorResources = listOf(R.color.bar_chart_incomes_type,
                                        R.color.bar_chart_expenses_type,
                                        R.color.bar_chart_expected_type)

    lateinit var day: KalendarDay

    private var formatter: DateFormatter<KalendarDay> = KalendarDayDateFormatter()
    private var colorPalette = emptyList<Int>()

    init {
        View.inflate(context, R.layout.view_calendar_day, this)

        colorPalette = colorResources.map { ContextCompat.getColor(context, it) }

        setupBarChart()
    }

    constructor(context: Context, day: KalendarDay) : this(context) {
        setDayNumber(day)
    }

    fun setDayNumber(day: KalendarDay) {
        this.day = day

        dayNumber.apply {
            text = formatter.format(this@KalendarDayView.day)
            setCheckedDay(day.isToday())
        }
    }

    fun setDayFormatter(formatter: DateFormatter<KalendarDay>) {
        this.formatter = formatter
        setDayNumber(this.day)
    }

    fun setCheckedDay(checked: Boolean) {
        if (checked) dayNumber.typeface = Typeface.DEFAULT_BOLD else Typeface.DEFAULT
    }

    fun setupDayShowingMode(flagsMode: Int, inRange: Boolean, inMonth: Boolean) {
        var dayShouldBeEnabled = inMonth && inRange

        if (flagsMode.shouldShowAllDates()) {
            dayShouldBeEnabled = true
        } else {
            if (!inMonth && flagsMode.shouldShowNonCurrentMonths()) dayShouldBeEnabled = true

            if (!inRange && flagsMode.shouldShowOutOfCalendarRangeDates()) dayShouldBeEnabled = dayShouldBeEnabled or inMonth

            if (flagsMode.shouldShowDefaultDates()) dayShouldBeEnabled = dayShouldBeEnabled.or(inMonth && inRange)

            if ((!inMonth || (inMonth && !inRange)) && dayShouldBeEnabled) {
                dayNumber.setTextColor(Color.GRAY)
            }
        }

        visibility = if (dayShouldBeEnabled) {
            applyBarChartData(KalendarDayViewData((0..2).map { (0..1).random() }), !inMonth || (inMonth && !inRange))
            performBarChartVerticalAnimation()
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    fun applyBarChartData(dataSet: KalendarDayViewData, applyGrayScaleColorScheme: Boolean = false) {
        dayMovementsBarChart.apply {
            data = BarData().apply {
                addDataSet(BarDataSet(
                        dataSet.barChartValues.mapIndexed { index, value -> BarEntry(index.toFloat(), value) },
                        "").apply {
                    barWidth = .9f
                    colors = if (applyGrayScaleColorScheme) listOf(Color.GRAY, Color.GRAY, Color.GRAY )else colorPalette
                    setDrawValues(false)
                })
            }
        }
    }

    fun performBarChartVerticalAnimation() {
        dayMovementsBarChart.apply {
            animateY(700, Easing.EasingOption.EaseOutBounce)
        }
    }

    private fun setupBarChart() {
        dayMovementsBarChart.apply {
            setFitBars(true)
            setDrawBorders(false)
            setDrawGridBackground(false)
            setScaleEnabled(false)
            setTouchEnabled(false)
            isDragEnabled = false
            setPinchZoom(false)
            axisLeft.setDrawLabels(false)
            axisLeft.setDrawAxisLine(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.spaceTop = 0f
            axisLeft.axisMaximum = 1f
            axisLeft.granularity = .01f
            axisLeft.axisMinimum = 0f
            axisRight.setDrawLabels(false)
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(false)
            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            legend.isEnabled = false
            description.isEnabled = false
            setViewPortOffsets(0f, 0f, 0f, 0f)
        }
    }

}