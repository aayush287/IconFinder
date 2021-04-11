package coding.universe.iconfinder.adapters

import androidx.recyclerview.widget.DiffUtil
import coding.universe.iconfinder.data.Icon

class IconsDiffUtils(private val oldList : List<Icon>, private val newList : List<Icon>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
       return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].iconId == newList[newItemPosition].iconId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}