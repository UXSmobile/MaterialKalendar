package com.uxsmobile.materialkalendar.presentation.ui.pager

import android.view.View
import android.view.ViewGroup
import com.uxsmobile.materialkalendar.app.dpToPx
import com.uxsmobile.materialkalendar.app.safeLet
import com.uxsmobile.materialkalendar.app.shouldShowNonCurrentMonths
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.presentation.ui.KalendarDayView
import com.uxsmobile.materialkalendar.presentation.ui.KalendarWeekDayView
import com.uxsmobile.materialkalendar.presentation.ui.MaterialKalendar
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
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
abstract class KalendarPagerView(private val materialKalendar: MaterialKalendar,
                                 val firstDayToShow: KalendarDay,
                                 private val firstWeekDay: DayOfWeek) : ViewGroup(materialKalendar.context) {

    companion object {
        const val DEFAULT_DAYS_IN_WEEK = 7
        const val DEFAULT_MAX_WEEKS = 6
        const val DAY_NAMES_ROW = 1
    }

    private val dayViews = mutableListOf<KalendarDayView>()
    private val weekDayViews = mutableListOf<KalendarWeekDayView>()

    private var minDate: KalendarDay? = null
    private var maxDate: KalendarDay? = null

    private var showDateFlagsMode = MaterialKalendar.ShowingDateModes.DEFAULT

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

        val measureTileWidth = (specWidthSize - (DEFAULT_DAYS_IN_WEEK -1) * 8.dpToPx()) / (DEFAULT_DAYS_IN_WEEK)
        val measureTileHeight = (specHeightSize - (DEFAULT_MAX_WEEKS + DAY_NAMES_ROW - 1) * 8.dpToPx()) / (DEFAULT_MAX_WEEKS + DAY_NAMES_ROW)

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
            childLeft += width + 8.dpToPx()

            if (index % DEFAULT_DAYS_IN_WEEK == DEFAULT_DAYS_IN_WEEK - 1) {
                childLeft = parentLeft
                childTop += height + 8.dpToPx()
            }
        }
    }

    fun setMinimumDate(minDate: KalendarDay) {
        this.minDate = minDate
        refreshUi()
    }

    fun setMaximumDate(maxDate: KalendarDay) {
        this.maxDate = maxDate
        refreshUi()
    }

    fun setShowingDatesMode(mode: MaterialKalendar.ShowingDateModes) {
        showDateFlagsMode = mode
        refreshUi()
    }

    fun setWeekDayFormatter(formatter: DateFormatter<DayOfWeek>) {
        weekDayViews.forEach {
            it.setWeekDayFormatter(formatter)
        }
    }

    private fun refreshUi() {
        safeLet(minDate, maxDate) { first, second ->
            updateDateRange(first, second)
        }
    }

    private fun updateDateRange(minDate: KalendarDay, maxDate: KalendarDay = KalendarDay.today()) {
        dayViews.forEach {
            it.setupDayShowingMode(showDateFlagsMode, it.day.isInDateRange(minDate, maxDate), isDayEnabled(it.day))
        }
        postInvalidate()
    }

    protected abstract fun getRows(): Int

    protected abstract fun buildDayViews(dayViews: MutableList<KalendarDayView>, calendar: LocalDate)

    protected abstract fun isDayEnabled(day: KalendarDay): Boolean

    protected fun addDayView(dayViews: MutableList<KalendarDayView>, temp: LocalDate) {
        val day = KalendarDay.from(temp)
        val dayView = KalendarDayView(context, day)
        dayView.setOnClickListener {
            if (it is KalendarDayView) {
                materialKalendar.onDateClicked(it)
            }
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
        val removeRow = if (showDateFlagsMode.shouldShowNonCurrentMonths()) delta >= 0 else delta > 0
        if (removeRow) {
            delta -= DEFAULT_DAYS_IN_WEEK
        }
        return temp.plusDays(delta.toLong())
    }

}