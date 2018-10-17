package com.uxsmobile.materialkalendar.ui.common.formatter

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
interface DateFormatter<T> {

    fun format(date: T): CharSequence

}