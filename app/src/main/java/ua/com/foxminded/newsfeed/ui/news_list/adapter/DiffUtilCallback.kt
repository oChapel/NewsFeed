package ua.com.foxminded.newsfeed.ui.news_list.adapter

import androidx.recyclerview.widget.DiffUtil
import ua.com.foxminded.newsfeed.data.model.Item
import kotlin.collections.ArrayList

class DiffUtilCallback(
    private val oldList: ArrayList<Item>,
    private val newList: ArrayList<Item>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}