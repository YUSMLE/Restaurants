package com.yusmle.restaurants.common

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

open class GeneralListAdapter<
    ITEM : BaseListItem,
    EVENT : BaseActionEvent,
    HOLDER : BaseViewHolder<ITEM>,
    FACTORY : BaseViewHolderFactory<ITEM, EVENT, HOLDER>,
    DIFFER : BaseListItemDiffer<ITEM>>(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewHolderFactory: FACTORY,
    private val listItemDifferProvider: (oldList: List<ITEM>, newList: List<ITEM>) -> DIFFER
) : RecyclerView.Adapter<HOLDER>() {

    private val _viewActions = MutableSharedFlow<EVENT>()
    val viewActions: SharedFlow<EVENT>
        get() = _viewActions

    private val dispatchAction: (viewAction: EVENT) -> Unit = {
        viewLifecycleOwner.lifecycleScope.launch {
            _viewActions.emit(it)
        }
    }

    var items: List<ITEM> by Delegates.observable(emptyList()) { _, oldList, newList ->
        notifyChanges(oldList, newList)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HOLDER = viewHolderFactory.createViewHolder(parent, viewType, dispatchAction)

    override fun onBindViewHolder(
        holder: HOLDER,
        position: Int,
    ) {
        holder.bindData(items[position])
    }

    override fun onViewDetachedFromWindow(holder: HOLDER) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetachFromWindow()
    }

    override fun onViewAttachedToWindow(holder: HOLDER) {
        super.onViewAttachedToWindow(holder)
        holder.onAttachToWindow()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].type()

    private fun notifyChanges(oldList: List<ITEM>, newList: List<ITEM>) =
        DiffUtil.calculateDiff(listItemDifferProvider(oldList, newList)).apply {
            dispatchUpdatesTo(this@GeneralListAdapter)
        }
}

interface BaseListItem {

    @LayoutRes
    fun type(): Int
}

interface BaseActionEvent

abstract class BaseViewHolder<ITEM : BaseListItem>(
    viewBinding: ViewBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {

    protected val context: Context
        get() = itemView.context

    abstract fun bindData(data: ITEM)
    open fun onDetachFromWindow() {}
    open fun onAttachToWindow() {}
}

interface BaseViewHolderFactory<ITEM : BaseListItem, EVENT : BaseActionEvent, HOLDER : BaseViewHolder<ITEM>> {

    @LayoutRes
    fun type(item: ITEM): Int

    fun createViewHolder(
        parent: ViewGroup,
        type: Int,
        dispatchAction: (viewAction: EVENT) -> Unit,
    ): HOLDER
}

abstract class BaseListItemDiffer<ITEM : BaseListItem>(
    protected val oldList: List<ITEM>,
    protected val newList: List<ITEM>
) : DiffUtil.Callback()
