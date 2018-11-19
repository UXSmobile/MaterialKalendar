package com.uxsmobile.materialkalendar

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.jakewharton.threetenabp.AndroidThreeTen
import com.uxsmobile.materialkalendar.app.random
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarDayMonthDateFormatter
import kotlinx.android.synthetic.main.activity_basic_monthly_barchart.monthlyBarChart


/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    30/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class BasicMonthlyBarChartTestActivity : AppCompatActivity() {

    private val colorResources = listOf(com.uxsmobile.library.R.color.bar_chart_incomes_type,
                                        com.uxsmobile.library.R.color.bar_chart_expenses_type,
                                        com.uxsmobile.library.R.color.bar_chart_expected_type)
    private val dateMonthFormatter: DateFormatter<KalendarDay> = KalendarDayMonthDateFormatter()


    private lateinit var dataSetsBarChart: Triple<LinkedHashMap<KalendarDay, Double>, LinkedHashMap<KalendarDay, Double>, LinkedHashMap<KalendarDay, Double>>
    private lateinit var colorPalette: List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_basic_monthly_barchart)

        setupBarChart()

        colorPalette = colorResources.map { ContextCompat.getColor(applicationContext, it) }

        dataSetsBarChart = buildMonthlyAggregationData(KalendarDay(KalendarDay.today().date.withDayOfMonth(1)))

        applyBarChartData(dataSetsBarChart)
    }

    private fun setupBarChart() {
        monthlyBarChart.apply {
            setDrawBorders(false)
            setDrawGridBackground(false)
            //            setScaleEnabled(false)
            //            setTouchEnabled(false)
            //            isDragEnabled = false
            //            setPinchZoom(false)
            isScrollContainer = true
            //            //axisLeft.setDrawLabels(false)
            axisLeft.setDrawAxisLine(false)
            axisLeft.setDrawGridLines(false)
            axisLeft.xOffset = 16f
            axisLeft.textColor = Color.WHITE
            //            axisLeft.spaceTop = 0f
            //            axisLeft.axisMaximum = 1f
            axisLeft.granularity = 1000f
            axisLeft.axisMinimum = 0f
            axisRight.setDrawLabels(false)
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(false)
            //            xAxis.setDrawLabels(false)
            xAxis.setDrawAxisLine(false)
            xAxis.setDrawGridLines(false)
            xAxis.isGranularityEnabled = true
            xAxis.granularity = 1f
            xAxis.textColor = Color.WHITE
            //            legend.isEnabled = false
            description.isEnabled = false
            //            setViewPortOffsets(0f, 0f, 0f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }
    }

    private fun applyBarChartData(barChartValues: Triple<LinkedHashMap<KalendarDay, Double>, LinkedHashMap<KalendarDay, Double>, LinkedHashMap<KalendarDay, Double>>) {
        monthlyBarChart.apply {
            data = BarData().apply {
                barChartValues.toList().mapIndexed { indexDataSet, list ->
                    addDataSet(BarDataSet(
                            list.values.mapIndexed { index, value -> BarEntry(index.toFloat(), value.toFloat()) },
                            "").apply {
                        barWidth = .15f
                        colors = listOf(colorPalette[indexDataSet])
                        isHighlightEnabled = false
                        setDrawValues(false)
                    })
                }
            }

            val barSpace = 0.05f
            val groupSpace = 0.4f
            val groupCount = 3

            monthlyBarChart.apply {
                xAxis.axisMinimum = 0f
                xAxis.axisMaximum = barChartValues.first.keys.toList().size.toFloat() - 1
                xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                    if (Math.abs(value.toInt()) <= xAxis.axisMaximum) {
                        dateMonthFormatter.format(barChartValues.first.keys.toList()[Math.abs(value.toInt())]).toString()
                    } else {
                        ""
                    }
                }
                setVisibleXRangeMaximum(0f + barData.getGroupWidth(groupSpace, barSpace) * groupCount)
                moveViewToX(0f)
                groupBars(0f, groupSpace, barSpace)
                xAxis.setCenterAxisLabels(true)

                legend.apply {
                    formSize = 10f; // set the size of the legend forms/shapes
                    form = Legend.LegendForm.CIRCLE; // set what type of form/shape should be used
                    position = Legend.LegendPosition.BELOW_CHART_CENTER;
                    textSize = 10f
                    textColor = Color.WHITE
                    xEntrySpace = 15f

                    val legendEntries: MutableList<LegendEntry> = mutableListOf()
                    val legendTitle = arrayListOf("Incomes", "Expenses", "Predictions")

                    (0 until groupCount).mapIndexedTo(legendEntries) { index, item -> LegendEntry().apply {
                        formColor = colorPalette[index]
                        label = legendTitle[index]
                    }}

                    setCustom(legendEntries)
                }
            }
            visibility = View.VISIBLE
            animateY(500, Easing.EasingOption.EaseOutBounce)
        }
    }

    private fun buildMonthlyAggregationData(
            day: KalendarDay): Triple<LinkedHashMap<KalendarDay, Double>, LinkedHashMap<KalendarDay, Double>, LinkedHashMap<KalendarDay, Double>> {
        val incomesPairList = mutableListOf<Pair<KalendarDay, Double>>()
        (1..day.date.lengthOfMonth()).mapTo(incomesPairList) {
            Pair(KalendarDay(day.date.withDayOfMonth(it)), (0..10000).random().toDouble())
        }

        val expensesPairList = mutableListOf<Pair<KalendarDay, Double>>()
        (1..day.date.lengthOfMonth()).mapTo(expensesPairList) {
            Pair(KalendarDay(day.date.withDayOfMonth(it)), (0..10000).random().toDouble())
        }

        val predictionsPairList = mutableListOf<Pair<KalendarDay, Double>>()
        (1..day.date.lengthOfMonth()).mapTo(predictionsPairList) {
            Pair(KalendarDay(day.date.withDayOfMonth(it)), (0..10000).random().toDouble())
        }

        return Triple(linkedMapOf(*(incomesPairList.toTypedArray())),
                      linkedMapOf(*(expensesPairList.toTypedArray())),
                      linkedMapOf(*(predictionsPairList.toTypedArray())))
    }

}