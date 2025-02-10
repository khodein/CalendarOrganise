package ru.calendar.feature.calendar.ui.date_carousel.model

data class DatePickerBuilderModel(
    val list: Array<String>,
    val focusIndex: Int,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DatePickerBuilderModel

        if (!list.contentEquals(other.list)) return false
        if (focusIndex != other.focusIndex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + list.contentHashCode()
        result = 31 * result + focusIndex
        return result
    }
}