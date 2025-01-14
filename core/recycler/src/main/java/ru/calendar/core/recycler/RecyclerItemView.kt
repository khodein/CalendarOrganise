package ru.calendar.core.recycler

interface RecyclerItemView<T : RecyclerState> {
    fun bindState(state: T)
}