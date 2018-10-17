package com.uxsmobile.materialkalendar.ui.common.formatter

import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
class KalendarWeekDayDateFormatter: DateFormatter<DayOfWeek> {

    override fun format(weekDay: DayOfWeek): CharSequence = weekDay.getDisplayName(TextStyle.SHORT, Locale.getDefault())

}