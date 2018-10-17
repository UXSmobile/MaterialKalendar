package com.uxsmobile.materialkalendar.ui

import android.content.Context
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
import com.uxsmobile.materialkalendar.data.KalendarDayViewData
import kotlinx.android.synthetic.main.view_calendar_day.view.dayMovementsBarChart
import kotlinx.android.synthetic.main.view_calendar_day.view.dayNumber

/**
 * @author   Daniel Manrique <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    16/10/2018.
 */
class KalendarDayView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {


    private val colorResources = listOf(R.color.bar_chart_incomes_type,
                                        R.color.bar_chart_expenses_type,
                                        R.color.bar_chart_expected_type)

    private var colorPalette = emptyList<Int>()

    init {
        View.inflate(context, R.layout.view_calendar_day, this)

        colorPalette = colorResources.map { ContextCompat.getColor(context, it) }

        dayNumber.text = 24.toString()
        setupBarChart()
    }

    fun setDayNumber(day: String, isToday: Boolean = false) {
        dayNumber.apply {
            text = day
            if (isToday) dayNumber.typeface = Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }
    }

    fun applyBarChartData(dataSet: KalendarDayViewData) {
        dayMovementsBarChart.apply {
            data = BarData().apply {
                addDataSet(BarDataSet(dataSet.barChartValues.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }, "").apply {
                    barWidth = .9f
                    colors = colorPalette
                    setDrawValues(false)
                })
            }
            animateY(1000, Easing.EasingOption.EaseOutBounce)
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