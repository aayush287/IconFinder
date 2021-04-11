package coding.universe.iconfinder.adapters

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coding.universe.iconfinder.R
import coding.universe.iconfinder.data.RasterSize
import coding.universe.iconfinder.databinding.ItemSelectFormatBinding
import coding.universe.iconfinder.util.DownloadOnItemClickListener
import coding.universe.iconfinder.util.downLoadResourcesWithPath

class SelectFormatAdapter( private val listener : DownloadOnItemClickListener) :
    RecyclerView.Adapter<SelectFormatAdapter.FormatViewHolder>() {

    private val listOfRasterSize: MutableList<RasterSize> = mutableListOf()

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormatViewHolder {
        context = parent.context
        return FormatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_select_format, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FormatViewHolder, position: Int) {
        val data = listOfRasterSize[position]

        holder.binding.size.text = "${data.width}x${data.height}"

        holder.binding.root.setOnClickListener {
            val url = data.formats.firstOrNull()?.downloadUrl ?: ""
            val format = data.formats.firstOrNull()?.format ?: ""
            val fileName = "Icon_${SystemClock.elapsedRealtime()}.$format"
            listener.onItemClick(url, fileName)
        }
    }


    override fun getItemCount(): Int {
        return listOfRasterSize.size
    }

    fun updateList(listOfRaster : ArrayList<RasterSize>){
        listOfRasterSize.addAll(listOfRaster)
        notifyDataSetChanged()
    }

    class FormatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemSelectFormatBinding = ItemSelectFormatBinding.bind(itemView)
    }
}