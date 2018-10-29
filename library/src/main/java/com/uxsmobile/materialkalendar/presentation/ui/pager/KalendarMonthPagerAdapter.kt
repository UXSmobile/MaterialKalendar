package com.uxsmobile.materialkalendar.presentation.ui.pager

import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.data.KalendarMonthlyAggregation
import com.uxsmobile.materialkalendar.presentation.ui.KalendarMonthView
import com.uxsmobile.materialkalendar.presentation.ui.MaterialKalendar
import com.uxsmobile.materialkalendar.presentation.ui.common.dateRange.DateRangeIndex
import com.uxsmobile.materialkalendar.presentation.ui.common.dateRange.MonthlyDateRange

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    18/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
internal class KalendarMonthPagerAdapter(private val materialKalendar: MaterialKalendar): KalendarPagerAdapter<KalendarMonthView>() {

    override fun createView(position: Int): KalendarMonthView = KalendarMonthView(materialKalendar,
                                                                                  getItem(position),
                                                                                  materialKalendar.getFirstDayOfWeek(),
                                                                                  materialKalendar.getShouldShowWeekDays())

    override fun indexOf(view: KalendarMonthView, dateRangeIndex: DateRangeIndex?): Int = getRangeIndex()?.indexOf(view.firstDayToShow) ?: 0

    override fun isInstanceOfView(obj: Any): Boolean = obj is KalendarMonthView

    override fun createRangeIndex(min: KalendarDay, max: KalendarDay): DateRangeIndex = MonthlyDateRange(min, max)

    fun setMonthlyAggregationData(monthView: KalendarMonthView?, data: KalendarMonthlyAggregation) {
        monthView?.setMonthlyAggregationData(data)
    }

}