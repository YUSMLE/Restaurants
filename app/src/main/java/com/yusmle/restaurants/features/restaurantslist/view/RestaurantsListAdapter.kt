package com.yusmle.restaurants.features.restaurantslist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.yusmle.restaurants.common.BaseActionEvent
import com.yusmle.restaurants.common.BaseListItem
import com.yusmle.restaurants.common.BaseListItemDiffer
import com.yusmle.restaurants.common.BaseViewHolder
import com.yusmle.restaurants.common.BaseViewHolderFactory
import com.yusmle.restaurants.common.GeneralListAdapter
import com.yusmle.restaurants.common.helper.util.onClickThrottled
import com.yusmle.restaurants.databinding.ItemLoadingBinding
import com.yusmle.restaurants.databinding.ItemRestaurantBinding
import com.yusmle.restaurants.databinding.ItemRetryBinding

class RestaurantsListAdapter(viewLifecycleOwner: LifecycleOwner) :
    GeneralListAdapter<RestaurantListItem, RestaurantActionEvent, BaseViewHolder<RestaurantListItem>, RestaurantViewHolderFactory, RestaurantListItemDiffer>(
        viewLifecycleOwner,
        RestaurantViewHolderFactory(viewLifecycleOwner),
        restaurantsListItemDifferProvider
    )

val restaurantsListItemDifferProvider: (
    oldList: List<RestaurantListItem>,
    newList: List<RestaurantListItem>
) -> RestaurantListItemDiffer = { oldList, newList ->
    RestaurantListItemDiffer(oldList, newList)
}

sealed class RestaurantListItem : BaseListItem {

    data class RestaurantItem(val name: String) : RestaurantListItem() {
        override fun type(): Int = RestaurantViewHolder.viewType
    }

    object LoadingItem : RestaurantListItem() {
        override fun type(): Int = LoadingViewHolder.viewType
    }

    data class RetryItem(val errorMessage: String) : RestaurantListItem() {
        override fun type() = RetryViewHolder.viewType
    }
}

sealed class RestaurantActionEvent : BaseActionEvent {

    data class RestaurantItemClicked(val name: String) : RestaurantActionEvent()

    object RetryItemClicked : RestaurantActionEvent()
}

class RestaurantViewHolder(
    private val viewBinding: ItemRestaurantBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val dispatchAction: (viewAction: RestaurantActionEvent) -> Unit,
) : BaseViewHolder<RestaurantListItem.RestaurantItem>(viewBinding) {

    override fun bindData(data: RestaurantListItem.RestaurantItem) {
        viewBinding.name.text = data.name
        viewBinding.root.onClickThrottled {
            dispatchAction(RestaurantActionEvent.RestaurantItemClicked(data.name))
        }
    }

    companion object {
        const val viewType: Int = 0
    }
}

class LoadingViewHolder(
    private val viewBinding: ItemLoadingBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val dispatchAction: (viewAction: RestaurantActionEvent) -> Unit,
) : BaseViewHolder<RestaurantListItem.LoadingItem>(viewBinding) {

    override fun bindData(data: RestaurantListItem.LoadingItem) {}

    companion object {
        const val viewType: Int = 1
    }
}

class RetryViewHolder(
    private val viewBinding: ItemRetryBinding,
    private val viewLifecycleOwner: LifecycleOwner,
    private val dispatchAction: (viewAction: RestaurantActionEvent) -> Unit,
) : BaseViewHolder<RestaurantListItem.RetryItem>(viewBinding) {

    override fun bindData(data: RestaurantListItem.RetryItem) {
        viewBinding.retryButton.onClickThrottled {
            dispatchAction(RestaurantActionEvent.RetryItemClicked)
        }
    }

    companion object {
        const val viewType: Int = 2
    }
}

@Suppress("UNCHECKED_CAST")
class RestaurantViewHolderFactory(
    private val viewLifecycleOwner: LifecycleOwner
) : BaseViewHolderFactory<RestaurantListItem, RestaurantActionEvent, BaseViewHolder<RestaurantListItem>> {

    override fun type(item: RestaurantListItem): Int {
        return item.type()
    }

    override fun createViewHolder(
        parent: ViewGroup,
        type: Int,
        dispatchAction: (viewAction: RestaurantActionEvent) -> Unit,
    ): BaseViewHolder<RestaurantListItem> = when (type) {
        RestaurantViewHolder.viewType -> RestaurantViewHolder(
            ItemRestaurantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewLifecycleOwner,
            dispatchAction
        )
        LoadingViewHolder.viewType -> LoadingViewHolder(
            ItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewLifecycleOwner,
            dispatchAction
        )
        RetryViewHolder.viewType -> RetryViewHolder(
            ItemRetryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewLifecycleOwner,
            dispatchAction
        )
        else -> throw IllegalStateException("Invalid View Type")
    } as BaseViewHolder<RestaurantListItem>
}

class RestaurantListItemDiffer(
    oldList: List<RestaurantListItem>,
    newList: List<RestaurantListItem>
) : BaseListItemDiffer<RestaurantListItem>(oldList, newList) {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is RestaurantListItem.RestaurantItem && newItem is RestaurantListItem.RestaurantItem)
            oldItem.name == newItem.name
        else oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}
