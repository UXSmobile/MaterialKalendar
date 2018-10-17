package com.uxsmobile.materialkalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uxsmobile.materialkalendar.data.KalendarDayViewData
import kotlinx.android.synthetic.main.activity_day_view_test.calendarDayView

/**
 * @author   Daniel Manrique <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    17/10/2018.
 */
class DayViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_view_test)
        setDataToDayView()
    }

    private fun setDataToDayView() {
        calendarDayView.applyBarChartData(KalendarDayViewData(listOf(.4f, 1f, .7f)))
    }

}