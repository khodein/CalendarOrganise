package ru.calendar.core.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.core.recycler.base.BaseRecyclerAdapter
import ru.calendar.core.recycler.diff.RecyclerStateDiffCallback
import ru.calendar.core.recycler.holder.RecyclerViewHolder

class RecyclerAdapter : ListAdapter<RecyclerState, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder(RecyclerStateDiffCallback()).build()
) {
    private val asyncListDiffer = AsyncListDiffer(this, RecyclerStateDiffCallback())
    private val baseAdapter by lazy(initializer = ::BaseRecyclerAdapter)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return baseAdapter.onCreateViewHolder(
            parent = parent,
            viewType = viewType,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is RecyclerViewHolder) return
        val item = getItem(position)
        baseAdapter.onBindViewHolder(
            item = item,
            holder = holder,
        )
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun getItem(position: Int): RecyclerState = asyncListDiffer.currentList[position]

    override fun submitList(list: MutableList<RecyclerState>?, commitCallback: Runnable?) {
        asyncListDiffer.submitList(list, commitCallback)
    }

    override fun submitList(list: List<RecyclerState>?) { asyncListDiffer.submitList(list) }

    override fun getItemViewType(position: Int) = baseAdapter.getItemViewType(
        position = position,
        item = getItem(position)
    )
}