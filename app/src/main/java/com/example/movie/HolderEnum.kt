package com.example.movie

enum class HolderEnum {
    ITEM, FOOTER;

    companion object {
        fun getHolderType(viewType: Int): HolderEnum {
            return values().first { it.ordinal == viewType }
        }
    }
}