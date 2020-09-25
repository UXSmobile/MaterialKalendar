package com.uxsmobile.materialkalendar.presentation.ui.common.formatter

import com.uxsmobile.materialkalendar.data.KalendarDay
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    29/01/2019.
 *
 * Copyright © 20199 UXS Mobile. All rights reserved.
 */
class KalendarDayMonthYearDateFormatter(private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", Locale.getDefault())) : DateFormatter<KalendarDay> {

    override fun format(date: KalendarDay): CharSequence = formatter.format(date.date).capitalize()

}