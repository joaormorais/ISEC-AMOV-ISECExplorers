package com.example.amovtp.utils

import androidx.compose.ui.graphics.Color

class Consts {
    companion object {
        const val POINTS_OF_INTEREST: String = "Points of Interest"
        const val DEFAULT_VALUE: String = "DEFAULT_VALUE"
        const val ALL_LOCATIONS: String = "ALL_LOCATIONS"
        const val ALL_CATEGORIES: String = "ALL_CATEGORIES"
        const val SUCCESS: String = "SUCCESS"
        const val ERROR_EXISTING_NAME: String = "ERROR_EXISTING_NAME"
        const val ERROR_EXISTING_POINT_OF_INTEREST: String = "ERROR_EXISTING_POINT_OF_INTEREST"
        const val ERROR_EXISTING_LOCATION: String = "ERROR_EXISTING_LOCATION"
        const val ERROR_NEED_LOGIN: String = "ERROR_NEED_LOGIN"
        const val ORDER_FOR_LOCATIONS: String = "ORDER_FOR_LOCATIONS"
        const val ORDER_FOR_POINTS_OF_INTEREST: String = "ORDER_FOR_POINTS_OF_INTEREST"
        const val ORDER_BY_NAME: String = "ORDER_BY_NAME"
        const val ORDER_BY_DISTANCE: String = "ORDER_BY_DISTANCE"
        const val ORDER_BY_CATEGORY: String = "ORDER_BY_CATEGORY"
        const val VOTES_NEEDED_FOR_APPROVAL: Long = 2
        const val NO_START_CLASSIFICATION: Long = 0
        const val ONE_STAR_CLASSIFICATION: Long = 1
        const val TWO_STAR_CLASSIFICATION: Long = 2
        const val THREE_STAR_CLASSIFICATION: Long = 3
        val NOT_APPROVED_COLOR: Color = Color(255, 135, 70, 255)
        val CONFIRMATION_COLOR: Color = Color(15, 100, 0, 255)
        val WARNING_COLOR: Color = Color(135, 0, 0, 255)
    }
}