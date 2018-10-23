package com.uxsmobile.materialkalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.data.KalendarDayViewData
import kotlinx.android.synthetic.main.activity_day_view_test.calendarDayView

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    17/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class DayViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_day_view_test)
        setDataToDayView()
    }

    private fun setDataToDayView() {
        calendarDayView.apply {
            setDayNumber(KalendarDay.from(2018, 10, 24))
            applyBarChartData(KalendarDayViewData(listOf(.4f, 1f, .7f)))
        }
    }

}