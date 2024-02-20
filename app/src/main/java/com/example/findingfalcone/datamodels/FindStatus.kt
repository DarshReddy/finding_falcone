package com.example.findingfalcone.datamodels

enum class FindStatus(private val statusKey: String) {
    SUCCESS("success"),
    FAILURE("false");

    companion object {
        private val keyMap = values().associateBy(FindStatus::statusKey)
        fun fromStatusKey(statusKey: String?) = keyMap[statusKey]
    }
}