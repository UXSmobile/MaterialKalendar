package com.uxsmobile.materialkalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import com.uxsmobile.materialkalendar.data.KalendarDay
import com.uxsmobile.materialkalendar.presentation.ui.MaterialKalendar
import kotlinx.android.synthetic.main.activity_basic_calendar.calendarView
import kotlinx.android.synthetic.main.activity_basic_calendar.textView
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    19/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class BasicCalendarTestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContentView(R.layout.activity_basic_calendar)
        calendarView.setCalendarBounds(KalendarDay.from(2018, 1, 30), KalendarDay.from(2018, 12, 20))
        calendarView.setOnDateChangedListener(object : MaterialKalendar.OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialKalendar, day: KalendarDay) {
                textView.text = day.date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault()))
            }
        })
    }

}