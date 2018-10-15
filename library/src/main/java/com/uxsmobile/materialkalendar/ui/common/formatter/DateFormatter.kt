package com.uxsmobile.materialkalendar.ui.common.formatter

/**
 * @author   Daniel Manrique <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 */
interface DateFormatter<T> {

    fun format(date: T): CharSequence

}