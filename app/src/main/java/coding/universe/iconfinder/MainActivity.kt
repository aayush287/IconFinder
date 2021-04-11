package coding.universe.iconfinder

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coding.universe.iconfinder.adapters.IconListAdapter
import coding.universe.iconfinder.data.Icon
import coding.universe.iconfinder.data.RasterSize
import coding.universe.iconfinder.databinding.ActivityMainBinding
import coding.universe.iconfinder.util.OnItemClickListener


class MainActivity : AppCompatActivity(), OnItemClickListener<ArrayList<RasterSize>> {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val iconAdapter by lazy { IconListAdapter(this) }

    private val listOfIcons: MutableList<Icon> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.iconListView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2, RecyclerView.VERTICAL, false)
            adapter = iconAdapter
        }

        // show loading progress bar
        showProgressBar(true)

        // observe the live data of icons
        viewModel.iconsListLiveData.observe(this) {
            if (it == null) {
                Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
            } else {
                showProgressBar(false)
                showLoadMoreProgressBar(false)
                listOfIcons.addAll(it)
                iconAdapter.updateList(listOfIcons)
            }
        }

        /*
            Implementing search logic
         */
        var isVisible = false

        binding.searchView.setOnClickListener {
            binding.searchView.setIconifiedByDefault(!isVisible)
            isVisible = !isVisible
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchableInfo = searchManager.getSearchableInfo(componentName)

        binding.searchView.setSearchableInfo(searchableInfo)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.query = query ?: ""
                listOfIcons.clear()
                viewModel.offset = 0
                viewModel.loadMoreIcons()
                showProgressBar(true)
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        /*
            Call 10 more icons if user reached to last element
         */
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY > oldScrollY) {
                    if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                        showLoadMoreProgressBar(true)
                        viewModel.offset += viewModel.count
                        viewModel.loadMoreIcons()
                    }
                }
            }
        })

        /*
            Filtering on the basis of premium and free
         */

        val popupMenu: PopupMenu = showPopupMenu(binding.filterView)

        binding.filterView.setOnClickListener {
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.both -> {
                    if (!item.isChecked) {
                        viewModel.isPremium = null
                        listOfIcons.clear()
                        viewModel.offset = 0
                        viewModel.loadMoreIcons()
                        showProgressBar(true)
                        item.isChecked = true
                        true
                    } else {
                        false
                    }

                }

                R.id.free -> {
                    if (!item.isChecked) {
                        viewModel.isPremium = false
                        listOfIcons.clear()
                        viewModel.offset = 0
                        viewModel.loadMoreIcons()
                        showProgressBar(true)
                        item.isChecked = true
                        true
                    } else {
                        false
                    }

                }

                R.id.premium -> {
                    if (!item.isChecked) {
                        viewModel.isPremium = true
                        listOfIcons.clear()
                        viewModel.offset = 0
                        viewModel.loadMoreIcons()
                        showProgressBar(true)
                        item.isChecked = true
                        true
                    } else {
                        false
                    }

                }

                R.id.reset -> {
                    listOfIcons.clear()
                    viewModel.offset = 0
                    viewModel.query = ""
                    viewModel.loadMoreIcons()
                    showProgressBar(true)
                    true
                }

                else -> false
            }

        }
    }

    override fun onItemClick(position: Int, item: ArrayList<RasterSize>) {
        val dialog = SelectFormatFragment.newInstance(item)
        supportFragmentManager.let { dialog.show(it, dialog.tag)  }
    }

    private fun showPopupMenu(view: View): PopupMenu = PopupMenu(view.context, view).run {
        menuInflater.inflate(R.menu.pop_up_menu, menu)
        return@run this
    }

    /**
     * Function to change the visibility of bottom
     * progress bar
     */
    private fun showLoadMoreProgressBar(show: Boolean) {
        binding.loadingMoreIconsBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Function to change the visibility of center progress bar
     */
    private fun showProgressBar(show: Boolean) {
        binding.loadingBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}