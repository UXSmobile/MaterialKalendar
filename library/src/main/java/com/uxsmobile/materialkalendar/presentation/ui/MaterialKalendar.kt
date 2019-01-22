package com.uxsmobile.materialkalendar.presentation.ui

import android.content.Context
import android.support.annotation.IntDef
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.uxsmobile.library.R
import com.uxsmobile.materialkalendar.app.dpToPx
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.data.KalendarMonthlyAggregation
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.ArrayKalendarWeekDayDateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.DateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.common.formatter.KalendarWeekDayDateFormatter
import com.uxsmobile.materialkalendar.presentation.ui.pager.KalendarMonthPagerAdapter
import com.uxsmobile.materialkalendar.presentation.ui.pager.KalendarPager
import com.uxsmobile.materialkalendar.presentation.ui.pager.KalendarPagerAdapter
import com.uxsmobile.materialkalendar.presentation.ui.pager.KalendarPagerView.Companion.DEFAULT_DAYS_IN_WEEK
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    18/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class MaterialKalendar
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_TILE_SIZE_DP = 55
        private const val DEFAULT_MAX_WEEKS = 6
        private const val DEFAULT_WEEK_DAYS_ROW = 1
        private const val INVALID_TILE_DIMENSION = -10

        const val SHOWING_MODE_DEFAULT = 1
        const val SHOWING_MODE_NON_CURRENT_MONTHS = 1 shl 1
        const val SHOWING_MODE_OUT_OF_CALENDAR_DATE_RANGE = 1 shl 2
        const val SHOWING_MODE_ALL = SHOWING_MODE_DEFAULT.or(
                SHOWING_MODE_OUT_OF_CALENDAR_DATE_RANGE.or(SHOWING_MODE_NON_CURRENT_MONTHS))
    }

    @IntDef(flag = true,
            value = [SHOWING_MODE_DEFAULT, SHOWING_MODE_OUT_OF_CALENDAR_DATE_RANGE, SHOWING_MODE_NON_CURRENT_MONTHS, SHOWING_MODE_ALL])
    @Retention(AnnotationRetention.SOURCE)
    annotation class ShowingDateModes

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            pagerScrollState = state
            pagerScrollState.let {
                if (it == ViewPager.SCROLL_STATE_IDLE) {
                    dispatchOnMonthChanged(adapter.getItem(pager.currentItem))
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {}
    }

    private val pager: KalendarPager

    private lateinit var firstDayOfWeek: DayOfWeek
    private var adapter: KalendarPagerAdapter<*>

    private var dateSelectedListener: OnDateSelectedListener? = null
    private var monthChangedListener: OnMonthChangedListener? = null

    private var pagerScrollState: Int? = null

    private var selectedDay: KalendarDay
    private var currentDay: KalendarDay
    private var minDate: KalendarDay? = null
    private var maxDate: KalendarDay? = null
    private var tileHeight: Int = INVALID_TILE_DIMENSION
    private var tileWidth: Int = INVALID_TILE_DIMENSION
    @ShowingDateModes private var showingDateFlagModes = SHOWING_MODE_DEFAULT
    private var allowClickDaysOutsideCurrentMonth: Boolean = true
    private var allowDynamicWeeksHeightResize: Boolean = false
    private var shouldShowWeekDays: Boolean = true

    init {
        clipToPadding = false
        clipChildren = false

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.MaterialKalendar, 0, 0)
        try {
            val firstDayOfWeekInt = a.getInteger(R.styleable.MaterialKalendar_mk_firstDayOfWeek, -1)

            firstDayOfWeek = if (firstDayOfWeekInt in 1..7) {
                DayOfWeek.of(firstDayOfWeekInt)
            } else {
                WeekFields.of(Locale.getDefault()).firstDayOfWeek
            }

            val tileSize = a.getLayoutDimension(R.styleable.MaterialKalendar_mk_tileSize, INVALID_TILE_DIMENSION)
            if (tileSize > INVALID_TILE_DIMENSION) {
                setTileSize(tileSize)
            }

            val tileWidth = a.getLayoutDimension(R.styleable.MaterialKalendar_mk_tileWidth, INVALID_TILE_DIMENSION)
            if (tileWidth > INVALID_TILE_DIMENSION) {
                setTileWidth(tileWidth)
            }

            val tileHeight = a.getLayoutDimension(R.styleable.MaterialKalendar_mk_tileHeight, INVALID_TILE_DIMENSION)
            if (tileHeight > INVALID_TILE_DIMENSION) {
                setTileHeight(tileHeight)
            }

            shouldShowWeekDays = a.getBoolean(R.styleable.MaterialKalendar_mk_showWeekDayLabels, true)

            val weekLabelsArray: Array<CharSequence>? = a.getTextArray(R.styleable.MaterialKalendar_mk_weekDayLabels)
            weekLabelsArray?.let {
                setWeekDayFormatter(ArrayKalendarWeekDayDateFormatter(weekLabelsArray))
            }

            showingDateFlagModes = a.getInteger(R.styleable.MaterialKalendar_mk_showingModes,
                                                SHOWING_MODE_DEFAULT)

            //setWeekDayTextAppearance(a.getResourceId(R.styleable.MaterialKalendar_mk_weekDayTextAppearance, R.style.TextAppearance_MaterialKalendarWidget_WeekDay))

            setAllowClickDaysOutsideCurrentMonth(
                    a.getBoolean(R.styleable.MaterialKalendar_mk_allowClickDaysOutsideCurrentMonth, true))
            setAllowDynamicWeeksHeightResize(
                    a.getBoolean(R.styleable.MaterialKalendar_mk_allowDynamicWeeksHeightResize, false))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            a.recycle()
        }

        adapter = KalendarMonthPagerAdapter(this@MaterialKalendar).apply {
            setShowingDatesMode(showingDateFlagModes)
        }
        pager = KalendarPager(context).apply {
            addOnPageChangeListener(pageChangeListener)
            pageMargin = 16.dpToPx()
            setPadding(16.dpToPx(), 0, 16.dpToPx(), 0)
        }
        pager.adapter = adapter

        setupChildren()

        currentDay = KalendarDay.today()
        selectedDay = currentDay
        setCurrentDate(currentDay)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val specHeightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val specHeightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth = specWidthSize - paddingLeft - paddingRight
        val desiredHeight = specHeightSize - paddingTop - paddingBottom

        val weeksToShow = getWeekCount()

        val desiredTileWidth = desiredWidth / DEFAULT_DAYS_IN_WEEK
        val desiredTileHeight = desiredHeight / weeksToShow

        var measureTileSize = -1
        var measureTileWidth = -1
        var measureTileHeight = -1

        if (this.tileWidth != INVALID_TILE_DIMENSION || this.tileHeight != INVALID_TILE_DIMENSION) {
            measureTileWidth = if (this.tileWidth > 0) {
                this.tileWidth
            } else {
                desiredTileWidth
            }
            measureTileHeight = if (this.tileHeight > 0) {
                this.tileHeight
            } else {
                desiredTileHeight
            }
        } else if (specWidthMode == View.MeasureSpec.EXACTLY || specWidthMode == View.MeasureSpec.AT_MOST) {
            measureTileSize = if (specHeightMode == View.MeasureSpec.EXACTLY) {
                Math.min(desiredTileWidth, desiredTileHeight)
            } else {
                desiredTileWidth
            }
        } else if (specHeightMode == View.MeasureSpec.EXACTLY || specHeightMode == View.MeasureSpec.AT_MOST) {
            measureTileSize = desiredTileHeight
        }

        if (measureTileSize > 0) {
            measureTileHeight = measureTileSize
            measureTileWidth = measureTileSize
        } else if (measureTileSize <= 0) {
            if (measureTileWidth <= 0) {
                measureTileWidth = DEFAULT_TILE_SIZE_DP.dpToPx()
            }
            if (measureTileHeight <= 0) {
                measureTileHeight = DEFAULT_TILE_SIZE_DP.dpToPx()
            }
        }

        var measuredWidth = measureTileWidth * DEFAULT_DAYS_IN_WEEK
        var measuredHeight = (measureTileHeight + 8.dpToPx()) * (weeksToShow)

        measuredWidth += paddingLeft + paddingRight
        measuredHeight += paddingTop + paddingBottom

        setMeasuredDimension(clampSize(measuredWidth, widthMeasureSpec), clampSize(measuredHeight, heightMeasureSpec))

        (0 until childCount).map { getChildAt(it) }.forEach { child ->
            val childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    DEFAULT_DAYS_IN_WEEK * measureTileWidth,
                    View.MeasureSpec.EXACTLY)

            val childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    child.layoutParams.height * (measureTileHeight + 8.dpToPx()),
                    View.MeasureSpec.EXACTLY)

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val parentLeft = paddingLeft
        val parentWidth = right - left - parentLeft - paddingRight

        var childTop = paddingTop

        (0 until childCount).map { getChildAt(it) }
                .filterNot { it.visibility == View.GONE }
                .forEach { child ->
                    val width = child.measuredWidth
                    val height = child.measuredHeight

                    val delta = (parentWidth - width) / 2
                    val childLeft = parentLeft + delta

                    child.layout(childLeft, childTop, childLeft + width, childTop + height)

                    childTop += height
                }
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    fun getFirstDayOfWeek() = firstDayOfWeek

    fun getShouldShowWeekDays() = shouldShowWeekDays

    fun setCalendarBounds(minDate: KalendarDay, maxDate: KalendarDay) {
        this.minDate = minDate
        this.maxDate = maxDate
        adapter.setRangeDates(minDate, maxDate)
        setCurrentDate(if (minDate.isAfter(currentDay)) minDate else currentDay)
    }

    fun setCurrentDate(date: KalendarDay) {
        pager.setCurrentItem(adapter.getIndexForDay(date), true)
    }

    fun setTileSize(size: Int) {
        this.tileWidth = size
        this.tileHeight = size
        requestLayout()
    }

    fun setTileSizeDp(tileSizeDp: Int) {
        setTileSize(tileSizeDp.dpToPx())
    }

    fun setTileHeight(height: Int) {
        this.tileHeight = height
        requestLayout()
    }

    fun setTileHeightDp(tileHeightDp: Int) {
        setTileHeight(tileHeightDp.dpToPx())
    }

    fun setTileWidth(width: Int) {
        this.tileWidth = width
        requestLayout()
    }

    fun setTileWidthDp(tileWidthDp: Int) {
        setTileWidth(tileWidthDp.dpToPx())
    }

    fun setShowingDatesMode(@MaterialKalendar.ShowingDateModes flagsMode: Int) {
        showingDateFlagModes = flagsMode
        adapter.setShowingDatesMode(flagsMode)
    }

    fun setWeekDayFormatter(formatter: DateFormatter<DayOfWeek>?) {
        adapter.setWeekDayFormatter(formatter ?: KalendarWeekDayDateFormatter())
    }

    fun setAllowClickDaysOutsideCurrentMonth(enable: Boolean) {
        allowClickDaysOutsideCurrentMonth = enable
    }

    fun setAllowDynamicWeeksHeightResize(enable: Boolean) {
        allowDynamicWeeksHeightResize = enable
    }

    fun getSelectedDayDate(): KalendarDay = selectedDay

    fun getVisibleMonthDate(): KalendarDay {
        return adapter.getItem(pager.currentItem)
    }

    fun setPagingEnabled(enable: Boolean) {
        pager.pagingEnabled = enable
    }

    fun setOnDateChangedListener(listener: OnDateSelectedListener) {
        dateSelectedListener = listener
    }

    fun setOnMonthChangedListener(listener: OnMonthChangedListener) {
        monthChangedListener = listener
    }

    fun setMonthlyAggregationData(data: KalendarMonthlyAggregation) {
        (adapter as? KalendarMonthPagerAdapter)?.setMonthlyAggregationData(pager.findViewWithTag(data.provideMonthAggregationDate().date.withDayOfMonth(1).toString()), data)
    }

    internal fun onDateClicked(dayView: KalendarDayView) {
        val currentDate = getVisibleMonthDate()
        val selectedDate = dayView.day
        val currentMonth = currentDate.date.monthValue
        val selectedMonth = selectedDate.date.monthValue

        clearSelectedDay(selectedDay)
        selectedDay = dayView.day
        dayView.setCheckedDay(true)

        if (allowClickDaysOutsideCurrentMonth && currentMonth != selectedMonth) {
            if (currentDate.isAfter(selectedDate)) {
                goToPreviousMonth()
            } else if (currentDate.isBefore(selectedDate)) {
                goToNextMonth()
            }
        }
        dispatchOnDateSelected(selectedDate)
    }

    private fun clearSelectedDay(selectedDay: KalendarDay) {
        (adapter.getItemFromMonth(KalendarDay.from(year = selectedDay.date.year, month = selectedDay.date.monthValue)) as? KalendarMonthView)?.disableCheckedDay(selectedDay)
    }

    private fun getWeekCount(): Int {
        var weekCount = DEFAULT_MAX_WEEKS
        if (allowDynamicWeeksHeightResize) {
            val cal = adapter.getItem(pager.currentItem).date
            val tempLastDay = cal.withDayOfMonth(cal.lengthOfMonth())
            weekCount = tempLastDay.get(WeekFields.of(firstDayOfWeek, 1).weekOfMonth())
        }
        return if (shouldShowWeekDays) weekCount + DEFAULT_WEEK_DAYS_ROW else weekCount
    }

    private fun dispatchOnDateSelected(day: KalendarDay) {
        dateSelectedListener?.onDateSelected(this, day)
    }

    private fun dispatchOnMonthChanged(day: KalendarDay) {
        monthChangedListener?.onMonthChanged(this, day)
    }

    private fun setupChildren() {
        pager.offscreenPageLimit = 1
        val tileHeight = if (shouldShowWeekDays) DEFAULT_MAX_WEEKS + DEFAULT_WEEK_DAYS_ROW else DEFAULT_MAX_WEEKS
        addView(pager, LayoutParams(LayoutParams.MATCH_PARENT, tileHeight))
    }

    private fun goToPreviousMonth() {
        if (canGoBack()) {
            pager.setCurrentItem(pager.currentItem - 1, true)
        }
    }

    private fun goToNextMonth() {
        if (canGoForward()) {
            pager.setCurrentItem(pager.currentItem + 1, true)
        }
    }

    private fun canGoForward(): Boolean {
        return pager.currentItem < adapter.count - 1
    }

    private fun canGoBack(): Boolean {
        return pager.currentItem > 0
    }

    private fun clampSize(size: Int, spec: Int): Int {
        val specMode = View.MeasureSpec.getMode(spec)
        val specSize = View.MeasureSpec.getSize(spec)
        return when (specMode) {
            View.MeasureSpec.EXACTLY -> {
                specSize
            }
            View.MeasureSpec.AT_MOST -> {
                Math.min(size, specSize)
            }
            View.MeasureSpec.UNSPECIFIED -> {
                size
            }
            else -> {
                size
            }
        }
    }

    interface OnDateSelectedListener {

        fun onDateSelected(widget: MaterialKalendar, date: KalendarDay)

    }

    interface OnMonthChangedListener {

        fun onMonthChanged(widget: MaterialKalendar, date: KalendarDay)

    }

}