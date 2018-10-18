package com.uxsmobile.materialkalendar.ui.pager

import android.view.View
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.ui.KalendarMonthView
import com.uxsmobile.materialkalendar.ui.common.dateRange.DateRangeIndex
import com.uxsmobile.materialkalendar.ui.common.dateRange.MonthlyDateRange
import org.threeten.bp.DayOfWeek

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    18/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class KalendarMonthPagerAdapter(private val materialKalendar: View): KalendarPagerAdapter<KalendarMonthView>() {

    override fun createView(position: Int): KalendarMonthView = KalendarMonthView(materialKalendar,
                                                                                  getItem(position),
                                                                                  DayOfWeek.FRIDAY /*To be changed*/)

    override fun indexOf(view: KalendarMonthView, dateRangeIndex: DateRangeIndex?): Int = getRangeIndex()?.indexOf(view.firstDayToShow) ?: 0

    override fun isInstanceOfView(obj: Any): Boolean = obj is KalendarMonthView

    override fun createRangeIndex(min: KalendarDay, max: KalendarDay): DateRangeIndex = MonthlyDateRange(min, max)
}