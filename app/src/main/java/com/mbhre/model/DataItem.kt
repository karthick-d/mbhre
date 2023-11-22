package com.mbhre.model

/**
 * Output object of get api
 *
 * @property id
 * @property name
 * @property email
 * @property gender
 * @property status
 */
data class DataItem(
    val id: String,
    val name: String,
    val email: String,
    val gender: String,
    val status: String
)