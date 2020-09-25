package com.uxsmobile.materialkalendar.data

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    25/10/2018.
 *
 * Copyright © 2018 UXS Mobile. All rights reserved.
 */
interface KalendarMonthlyAggregation {

    fun provideMonthAggregationDate(): KalendarDay

    fun provideAggregationIncomesData(): Map<KalendarDay, Double>

    fun provideAggregationExpensesData(): Map<KalendarDay, Double>

    fun provideAggregationPredictionsData(): Map<KalendarDay, Double>

}