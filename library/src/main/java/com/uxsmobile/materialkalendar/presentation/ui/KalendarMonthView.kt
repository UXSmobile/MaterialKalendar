package com.uxsmobile.materialkalendar.presentation.ui

import android.annotation.SuppressLint
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.data.KalendarDayViewData
import com.uxsmobile.materialkalendar.data.KalendarMonthlyAggregation
import com.uxsmobile.materialkalendar.presentation.ui.pager.KalendarPagerView
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    18/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
@SuppressLint("ViewConstructor")
internal class KalendarMonthView(materialKalendarView: MaterialKalendar,
                        val firstDayToShow: KalendarDay,
                        firstWeekDay: DayOfWeek,
                        private val shouldShowWeekDays: Boolean) : KalendarPagerView(materialKalendarView,
                                                                                     firstDayToShow,
                                                                                     firstWeekDay,
                                                                                     shouldShowWeekDays) {

    override fun getRows(): Int = if (shouldShowWeekDays) DEFAULT_MAX_WEEKS + DAY_NAMES_ROW else DEFAULT_MAX_WEEKS

    override fun buildDayViews(dayViews: MutableList<KalendarDayView>, calendar: LocalDate) {
        var tempDate = calendar
        (0 until DEFAULT_MAX_WEEKS).map {
            (0 until DEFAULT_DAYS_IN_WEEK).map {
                addDayView(dayViews, tempDate)
                tempDate = tempDate.plusDays(1)
            }
        }
    }

    override fun isDayEnabled(day: KalendarDay): Boolean = day.date.month == firstDayToShow.date.month

    fun setMonthlyAggregationData(data: KalendarMonthlyAggregation) {
        dayViews.asSequence()
                .forEach { dayView ->
                    dayView.apply {
                        applyBarChartData(KalendarDayViewData(
                                listOf(data.provideAggregationExpensesData().mapValues { it.value.toFloat() }[dayView.day] ?: 0f,
                                       data.provideAggregationIncomesData().mapValues { it.value.toFloat() }[dayView.day]?: 0f,
                                       data.provideAggregationPredictionsData().mapValues { it.value.toFloat() }[dayView.day] ?: 0f)))
                    }
                }
    }

    fun disableCheckedDay(checkedDay: KalendarDay) {
        dayViews.filter { it.day == checkedDay }.forEach { it.apply { setCheckedDay(false) } }
    }

}