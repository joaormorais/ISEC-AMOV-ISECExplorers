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
        const val ORDER_BY_VOTES: String = "ORDER_BY_VOTES"
        const val ORDER_BY_NAME: String = "ORDER_BY_NAME"
        const val ORDER_BY_DISTANCE: String = "ORDER_BY_DISTANCE"
        const val VOTES_NEEDED_FOR_APPROVAL: Int = 2
        const val NO_START_CLASSIFICATION: Int = 0
        const val ONE_STAR_CLASSIFICATION: Int = 1
        const val TWO_STAR_CLASSIFICATION: Int = 2
        const val THREE_STAR_CLASSIFICATION: Int = 3
        val NOT_APPROVED_COLOR: Color = Color(255, 135, 70, 255)
        val CONFIRMATION_COLOR: Color = Color(15, 100, 0, 255)
        val WARNING_COLOR: Color = Color(135, 0, 0, 255)
    }
}