package coding.universe.iconfinder.util

interface OnItemClickListener<T> {
    fun onItemClick(position: Int, item: T)
}

interface DownloadOnItemClickListener {
    fun onItemClick(url: String, fileName :String)
}