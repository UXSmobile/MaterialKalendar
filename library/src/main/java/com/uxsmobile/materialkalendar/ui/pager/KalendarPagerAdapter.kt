package com.uxsmobile.materialkalendar.ui.pager

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.ui.common.dateRange.DateRangeIndex
import com.uxsmobile.materialkalendar.ui.common.formatter.KalendarWeekDayDateFormatter
import org.threeten.bp.LocalDate
import java.util.ArrayDeque

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
abstract class KalendarPagerAdapter<V : KalendarPagerView> : PagerAdapter() {

    private val currentViews = ArrayDeque<V>()
    private val selectedDates = emptyList<KalendarDay>().toMutableList()
    private val today = KalendarDay.today()

    private lateinit var minDate: KalendarDay
    private lateinit var maxDate: KalendarDay

    private var weekDayFormatter = KalendarWeekDayDateFormatter()

    private var rangeIndex: DateRangeIndex? = null

    init {
        setRangeDates(KalendarDay.from(today.date.year - 200, today.date.monthValue, today.date.dayOfMonth),
                      KalendarDay.from(today.date.year + 200, today.date.monthValue, today.date.dayOfMonth))
    }

    override fun getCount(): Int = rangeIndex?.count ?: 0

    override fun getItemPosition(obj: Any): Int {
        if (!isInstanceOfView(obj)) {
            return PagerAdapter.POSITION_NONE
        }

        val index = indexOf(obj as V)
        return if (index < 0) {
            PagerAdapter.POSITION_NONE
        } else index
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pagerView = createView(position) //use APPLY
        pagerView.apply {
            alpha = 0f
            setWeekDayFormatter(weekDayFormatter)
            setMinimumDate(minDate)
            setMaximumDate(maxDate)
            setSelectedDates(selectedDates)
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
                setMinimumDate(min)
                setMaximumDate(max)
            }
        }

        rangeIndex = createRangeIndex(minDate, maxDate)

        notifyDataSetChanged()
        invalidateSelectedDates()
    }

    fun getIndexForDay(day: KalendarDay?): Int {
        day?.let { kalendarDay ->
            if (kalendarDay.isBefore(minDate)) return 0
            if (kalendarDay.isAfter(maxDate)) return count - 1
            return rangeIndex?.indexOf(day) ?: count / 2
        } ?: return count / 2
    }

    fun setWeekDayFormatter(formatter: KalendarWeekDayDateFormatter) {
        weekDayFormatter = formatter

        currentViews.forEach {
            it.setWeekDayFormatter(formatter)

        }
    }

    fun clearSelections() {
        selectedDates.clear()
        invalidateSelectedDates()
    }

    fun setDateSelected(day: KalendarDay, selected: Boolean) {
        if (selected) {
            if (!selectedDates.contains(day)) {
                selectedDates.add(day)
                invalidateSelectedDates()
            }
        } else {
            if (selectedDates.contains(day)) {
                selectedDates.remove(day)
                invalidateSelectedDates()
            }
        }
    }

    fun selectRange(first: KalendarDay, last: KalendarDay) {
        selectedDates.clear()

        // Copy to start from the first day and increment
        var temp = LocalDate.of(first.date.year, first.date.monthValue, first.date.dayOfMonth)

        // for comparison
        val end = last.date

        while (temp.isBefore(end) || temp == end) {
            selectedDates.add(KalendarDay.from(temp))
            temp = temp.plusDays(1)
        }

        invalidateSelectedDates()
    }

    fun getItem(position: Int): KalendarDay {
        return rangeIndex?.getItem(position) ?: today
    }

    fun getSelectedDates(): List<KalendarDay> = selectedDates.toList()

    fun getRangeIndex() = rangeIndex

    private fun invalidateSelectedDates() {
        validateSelectedDates()

        currentViews.forEach {
            it.setSelectedDates(selectedDates)
        }
    }

    private fun validateSelectedDates() {
        val min = minDate
        val max = maxDate

        selectedDates.forEachIndexed { index, kalendarDay ->
            if (min.isAfter(kalendarDay) || max.isBefore(kalendarDay)) {
                selectedDates.removeAt(index)
                //mcv.onDateUnselected(date)
            }
        }

    }

}
