package ru.calendar.core.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import ru.calendar.core.recycler.RecyclerState

internal class RecyclerStateDiffCallback : DiffUtil.ItemCallback<RecyclerState>() {
    override fun areItemsTheSame(
        oldItem: RecyclerState,
        newItem: RecyclerState
    ) = oldItem.provideId == newItem.provideId

    override fun areContentsTheSame(
        oldItem: RecyclerState,
        newItem: RecyclerState
    ) = oldItem.areContentsTheSame(newItem)
}