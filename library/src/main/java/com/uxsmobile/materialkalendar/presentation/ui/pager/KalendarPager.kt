package com.uxsmobile.materialkalendar.presentation.ui.pager

import android.annotation.SuppressLint
import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @author   Daniel Manrique Lucas <daniel.manrique@uxsmobile.com>
 * @version  1
 * @since    10/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
internal class KalendarPager: androidx.viewpager.widget.ViewPager {

    var pagingEnabled = true

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return pagingEnabled && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return pagingEnabled && super.onInterceptTouchEvent(ev)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return pagingEnabled && super.canScrollVertically(direction)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return pagingEnabled && super.canScrollHorizontally(direction)
    }
}