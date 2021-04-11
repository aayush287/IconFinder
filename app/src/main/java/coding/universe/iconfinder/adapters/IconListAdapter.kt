package coding.universe.iconfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coding.universe.iconfinder.R
import coding.universe.iconfinder.data.Icon
import coding.universe.iconfinder.data.RasterSize
import coding.universe.iconfinder.databinding.ItemIconViewBinding
import coding.universe.iconfinder.util.OnItemClickListener
import com.squareup.picasso.Picasso

class IconListAdapter(private val listener : OnItemClickListener<ArrayList<RasterSize>>) : RecyclerView.Adapter<IconListAdapter.IconViewHolder>() {

    private val listOfIcons: MutableList<Icon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        return IconViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_icon_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = listOfIcons[position]

        val iconImage = icon.rasterSize.lastOrNull()?.formats?.firstOrNull()?.previewUrl

        Picasso.get().load(iconImage).into(holder.binding.iconView)


        holder.binding.premium.visibility = if (icon.isPremium) View.VISIBLE else View.GONE
        holder.binding.downloadBtn.visibility = if (icon.isPremium) View.GONE else View.VISIBLE

        if(!icon.isPremium){
            holder.binding.downloadBtn.setOnClickListener {
                val list = icon.rasterSize
                listener.onItemClick(position, list as ArrayList<RasterSize>)
            }
        }


    }

    override fun getItemCount(): Int {
        return listOfIcons.size
    }

    fun updateList(newList: List<Icon>) {
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(IconsDiffUtils(oldList = this.listOfIcons, newList = newList))
        listOfIcons.clear()
        listOfIcons.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class IconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemIconViewBinding = ItemIconViewBinding.bind(itemView)
    }
}