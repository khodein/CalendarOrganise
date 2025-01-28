package ru.calendar.core.recycler.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.calendar.core.recycler.RecyclerState
import ru.calendar.core.recycler.holder.HolderItemState
import ru.calendar.core.recycler.holder.RecyclerViewHolder
import java.util.concurrent.ConcurrentHashMap

internal class BaseRecyclerAdapter {

    private val holderViewTypeMap: HashMap<Int, HolderItemState> = hashMapOf()

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getItemView(
            viewType = viewType,
            context = parent.context
        )?.let { view ->
            RecyclerViewHolder(
                view = view,
                viewType = viewType
            )
        } ?: throw IllegalArgumentException("Invalid ViewType")
    }

    fun onBindViewHolder(
        item: RecyclerState,
        holder: RecyclerView.ViewHolder,
    ) {
        if (holder is RecyclerViewHolder && holder.viewType == item.viewType) {
            holder.bind(item)
        }
    }

    fun getItemViewType(
        position: Int,
        item: HolderItemState,
    ): Int {
        holderViewTypeMap.getOrPut(item.viewType) { item }
        return item.viewType
    }

    private fun getItemView(
        viewType: Int,
        context: Context,
    ): View? {
        return holderViewTypeMap[viewType]?.getView(context)
    }
}