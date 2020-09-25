package com.uxsmobile.materialkalendar

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarDayMonthDateFormatter
import kotlin.math.abs

class XAxisValueFormatter(private val barChartValues: Triple<LinkedHashMap<KalendarDay, Double>,
        LinkedHashMap<KalendarDay, Double>,
        LinkedHashMap<KalendarDay, Double>>
) : ValueFormatter() {

    private val dateMonthFormatter: DateFormatter<KalendarDay> = KalendarDayMonthDateFormatter()

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val xAxis = axis as? XAxis ?: return ""
        return if (abs(value.toInt()) <= xAxis.axisMaximum.minus(1)) {
            dateMonthFormatter.format(barChartValues.first.keys.toList()[abs(value.toInt())]).toString()
        } else {
            ""
        }
    }
}