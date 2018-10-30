package com.uxsmobile.materialkalendar.presentation.ui.common.formatter

import com.uxsmobile.materialkalendar.data.KalendarDay
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
internal class KalendarDayDateFormatter(
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())) : DateFormatter<KalendarDay> {

    override fun format(date: KalendarDay): CharSequence = formatter.format(date.date)

}