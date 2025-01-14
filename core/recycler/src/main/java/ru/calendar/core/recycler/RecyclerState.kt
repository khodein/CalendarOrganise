package ru.calendar.core.recycler

import android.content.Context
import android.view.View
import ru.calendar.core.recycler.holder.HolderItemState

interface RecyclerState : HolderItemState {
    val provideId: String
    fun areContentsTheSame(other: RecyclerState) = this == other
    override val viewType: Int
    override fun getView(context: Context): View
}