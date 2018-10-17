package com.uxsmobile.materialkalendar.ui.pager

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.uxsmobile.materialkalendar.ui.KalendarDayView

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    15/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
abstract class KalendarPagerView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val dayViews = mutableListOf<KalendarDayView>()

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
    }

}