package com.uxsmobile.materialkalendar.presentation.ui.pager

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.presentation.ui.common.dateRange.DateRangeIndex
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarWeekDayDateFormatter
import org.threeten.bp.DayOfWeek

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
abstract class KalendarPagerAdapter<V : KalendarPagerView> : PagerAdapter() {

    private val currentViews = arrayListOf<V>()
    private val today = KalendarDay.today()

    private lateinit var minDate: KalendarDay
    private lateinit var maxDate: KalendarDay

    private var weekDayFormatter: DateFormatter<DayOfWeek> = KalendarWeekDayDateFormatter()

    private var rangeIndex: DateRangeIndex? = null

    init {
        setRangeDates(KalendarDay.from(today.date.year - 5, today.date.monthValue, today.date.dayOfMonth),
                      KalendarDay.from(today.date.year + 5, today.date.monthValue, today.date.dayOfMonth))
    }

    override fun getCount(): Int = rangeIndex?.count ?: 0

    override fun getItemPosition(obj: Any): Int {
        val index = indexOf(obj as V)
        return if (index < 0) {
            PagerAdapter.POSITION_NONE
        } else index
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pagerView = createView(position).apply {
            setWeekDayFormatter(weekDayFormatter)
            applyDateRange(minDate, maxDate)
        }

        container.addView(pagerView)
        currentViews.add(pagerView)

        return pagerView
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val pagerView = obj as? V ?: return
        currentViews.remove(pagerView)
        container.removeView(pagerView)
    }

    protected abstract fun createView(position: Int): V

    protected abstract fun indexOf(view: V, dateRangeIndex: DateRangeIndex? = rangeIndex): Int

    protected abstract fun isInstanceOfView(obj: Any): Boolean

    protected abstract fun createRangeIndex(min: KalendarDay, max: KalendarDay): DateRangeIndex

    fun setRangeDates(min: KalendarDay, max: KalendarDay) {
        minDate = min
        maxDate = max

        currentViews.forEach {
            it.apply {
                applyDateRange(minDate, maxDate)
            }
        }

        rangeIndex = createRangeIndex(minDate, maxDate)

        notifyDataSetChanged()
    }

    fun getIndexForDay(day: KalendarDay?): Int {
        day?.let { kalendarDay ->
            if (kalendarDay.isBefore(minDate)) return 0
            if (kalendarDay.isAfter(maxDate)) return count - 1
            return rangeIndex?.indexOf(day) ?: count / 2
        } ?: return count / 2
    }

    fun setWeekDayFormatter(formatter: DateFormatter<DayOfWeek>) {
        weekDayFormatter = formatter

        currentViews.forEach {
            it.setWeekDayFormatter(formatter)

        }
    }

    fun getItem(position: Int): KalendarDay {
        return rangeIndex?.getItem(position) ?: today
    }

    fun getRangeIndex() = rangeIndex

}
