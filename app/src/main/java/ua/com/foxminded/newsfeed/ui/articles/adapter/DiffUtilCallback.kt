package ua.com.foxminded.newsfeed.ui.articles.adapter

import androidx.recyclerview.widget.DiffUtil
import ua.com.foxminded.newsfeed.data.dto.Item

class DiffUtilCallback(
    private val oldList: ArrayList<Item>,
    private val newList: ArrayList<Item>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].link == newList[newItemPosition].link
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
