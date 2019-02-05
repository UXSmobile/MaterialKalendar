package com.uxsmobile.materialkalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import com.uxsmobile.materialkalendar.app.random
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.data.KalendarMonthlyAggregation
import com.uxsmobile.materialkalendar.presentation.ui.MaterialKalendar
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarDayMonthYearDateFormatter
import kotlinx.android.synthetic.main.activity_basic_calendar.calendarView
import kotlinx.android.synthetic.main.activity_basic_calendar.dateTextView
import kotlinx.android.synthetic.main.activity_basic_calendar.disableNextOnMonthChangedTrigger
import kotlinx.android.synthetic.main.activity_basic_calendar.monthYearTextView
import kotlinx.android.synthetic.main.activity_basic_calendar.scrollCurrentDay
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    19/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class BasicCalendarTestActivity: AppCompatActivity() {

    private lateinit var startAggregationDate: KalendarDay
    private lateinit var incomesMap : LinkedHashMap<KalendarDay, Double>
    private lateinit var expensesMap : LinkedHashMap<KalendarDay, Double>
    private lateinit var predictionsMap : LinkedHashMap<KalendarDay, Double>

    private val aggregationData = object: KalendarMonthlyAggregation {
        override fun provideMonthAggregationDate(): KalendarDay {
            return startAggregationDate
        }

        override fun provideAggregationIncomesData(): Map<KalendarDay, Double> {
            return incomesMap
        }

        override fun provideAggregationExpensesData(): Map<KalendarDay, Double> {
            return expensesMap
        }

        override fun provideAggregationPredictionsData(): Map<KalendarDay, Double> {
            return predictionsMap
        }
    }

    private val monthYearFormatter = KalendarDayMonthYearDateFormatter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_basic_calendar)

        calendarView.setCalendarBounds(KalendarDay.from(2018, 1, 4), KalendarDay.from(2019, 12, 20))
        calendarView.setOnDateChangedListener(object : MaterialKalendar.OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialKalendar, date: KalendarDay, isDaySelected: Boolean) {
                dateTextView.text = widget.selectedDay?.date?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()))
            }
        })
        calendarView.setOnMonthChangedListener(object : MaterialKalendar.OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialKalendar, date: KalendarDay) {
                buildMonthlyAggregationData(date)
                widget.setMonthlyAggregationData(aggregationData)
                monthYearTextView.text = monthYearFormatter.format(date)
            }
        })

        dateTextView.text = calendarView.selectedDay?.date?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()))
        calendarView.selectedDay?.let { monthYearTextView.text = monthYearFormatter.format(it) }
        scrollCurrentDay.setOnClickListener { calendarView.scrollToDate(KalendarDay.today(), disableNextOnMonthChangedTrigger.isChecked) }
    }

    private fun buildMonthlyAggregationData(day: KalendarDay) {
        val incomesPairList = mutableListOf<Pair<KalendarDay, Double>>()
        (1..day.date.lengthOfMonth()).mapTo(incomesPairList) {Pair(KalendarDay(day.date.withDayOfMonth(it)), (0..1).random().toDouble())}

        val expensesPairList = mutableListOf<Pair<KalendarDay, Double>>()
        (1..day.date.lengthOfMonth()).mapTo(expensesPairList) {Pair(KalendarDay(day.date.withDayOfMonth(it)), (0..1).random().toDouble())}

        val predictionsPairList = mutableListOf<Pair<KalendarDay, Double>>()
        (1..day.date.lengthOfMonth()).mapTo(predictionsPairList) {Pair(KalendarDay(day.date.withDayOfMonth(it)), (0..1).random().toDouble())}

        startAggregationDate = day
        incomesMap = linkedMapOf(*(incomesPairList.toTypedArray()))
        expensesMap = linkedMapOf(*(expensesPairList.toTypedArray()))
        predictionsMap = linkedMapOf(*(predictionsPairList.toTypedArray()))
    }

}