package com.uxsmobile.materialkalendar.ui.pager

import android.view.View
import android.view.ViewGroup
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.ui.KalendarDayView
import com.uxsmobile.materialkalendar.ui.KalendarWeekDayView
import com.uxsmobile.materialkalendar.ui.common.formatter.DateFormatter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
abstract class KalendarPagerView(val materialKalendarView: View,
                                 val firstDayToShow: KalendarDay,
                                 val firstWeekDay: DayOfWeek) : ViewGroup(materialKalendarView.context) {

    companion object {
        const val DEFAULT_DAYS_IN_WEEK = 7
        const val DEFAULT_MAX_WEEKS = 6
        const val DAY_NAMES_ROW = 1
    }

    private val dayViews = mutableListOf<KalendarDayView>()
    private val weekDayViews = mutableListOf<KalendarWeekDayView>()

    private lateinit var minDate: KalendarDay
    private lateinit var maxDate: KalendarDay

    init {
        clipChildren = false
        clipToPadding = false

        provideInitialCalendar().apply {
            buildWeekDays(this)
            buildDayViews(dayViews, this)
        }
    }

    override fun shouldDelayChildPressedState(): Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (specHeightMode == View.MeasureSpec.UNSPECIFIED || specWidthMode == View.MeasureSpec.UNSPECIFIED) {
            return
        }

        val measureTileWidth = specWidthSize / DEFAULT_DAYS_IN_WEEK
        val measureTileHeight = specHeightSize / getRows()

        setMeasuredDimension(specWidthSize, specHeightSize)

        (0 until childCount).map { getChildAt(it) }.forEach { child ->
            val childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measureTileWidth,
                                                                         View.MeasureSpec.EXACTLY)

            val childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measureTileHeight,
                                                                          View.MeasureSpec.EXACTLY)

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val parentLeft = 0

        var childTop = 0
        var childLeft = parentLeft

        (0 until childCount).map { getChildAt(it) }.forEachIndexed { index, child ->
            val width = child.measuredWidth
            val height = child.measuredHeight

            child.layout(childLeft, childTop, childLeft + width, childTop + height)
            childLeft += width

            if (index % DEFAULT_DAYS_IN_WEEK == DEFAULT_DAYS_IN_WEEK - 1) {
                childLeft = parentLeft
                childTop += height
            }
        }
    }

    fun setWeekDayFormatter(formatter: DateFormatter<DayOfWeek>) {
        weekDayViews.forEach {
            it.setWeekDayFormatter(formatter)
        }
    }

    fun setMinimumDate(minDate: KalendarDay) {
        this.minDate = minDate
        updateUi()
    }

    fun setMaximumDate(maxDate: KalendarDay) {
        this.maxDate = maxDate
        updateUi()
    }

    fun setSelectedDates(dates: List<KalendarDay>?) {
        dayViews.forEach {
            it.setCheckedDay(dates != null && dates.contains(it.day))
        }
        postInvalidate()
    }

    private fun updateUi() {
        dayViews.forEach {
            it.setupSelection(it.day.isInDateRange(minDate, maxDate), isDayEnabled(it.day))
        }
        postInvalidate()
    }

    protected abstract fun getRows(): Int

    protected abstract fun buildDayViews(dayViews: List<KalendarDayView>, calendar: LocalDate)

    protected abstract fun isDayEnabled(day: KalendarDay): Boolean

    protected fun addDayView(dayViews: MutableList<KalendarDayView>, temp: LocalDate) {
        val day = KalendarDay.from(temp)
        val dayView = KalendarDayView(context, day)
        dayView.setOnClickListener {
            //if (it is KalendarDayView) materialKalendar.onDayClicked(it)
        }
        dayViews.add(dayView)
        addView(dayView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
    }

    protected fun getFirstDayOfWeek() = firstWeekDay

    private fun buildWeekDays(calendar: LocalDate) {
        var local = calendar
        for (i in 0 until DEFAULT_DAYS_IN_WEEK) {
            val weekDayView = KalendarWeekDayView(context, local.dayOfWeek)
            weekDayView.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            weekDayViews.add(weekDayView)
            addView(weekDayView)
            local = local.plusDays(1)
        }
    }

    private fun provideInitialCalendar(): LocalDate {
        val firstDayOfWeek = WeekFields.of(firstWeekDay, 1).dayOfWeek()
        val temp = firstDayToShow.date.with(firstDayOfWeek, 1)
        val dayOfWeek = temp.dayOfWeek.value
        var delta = firstWeekDay.value - dayOfWeek
        if (delta >= 0) {
            delta -= DEFAULT_DAYS_IN_WEEK
        }
        return temp.plusDays(delta.toLong())
    }

}