package coding.universe.iconfinder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coding.universe.iconfinder.data.Icon
import coding.universe.iconfinder.repository.IconsRepository

class MainViewModel : ViewModel() {
    var count: Int = 10
    var offset: Int = 0

    // Took hard coded category to finish project earlly
    var category: String = "abstract"

    var isPremium: Boolean? = null

    var query: String = ""


    init {
        IconsRepository.loadIcons(
            count = count,
            category = category,
            offset = offset,
            isPremium = null
        )
    }

    private var _iconsListLiveData =
        MediatorLiveData<List<Icon>?>().apply {
            addSource(IconsRepository.getIconData()) {
                this.value = it
            }
        }
    val iconsListLiveData: LiveData<List<Icon>?> = _iconsListLiveData


    fun loadMoreIcons() {
        if (query.isEmpty()) {
            IconsRepository.loadIcons(
                count = count,
                category = category,
                offset = offset,
                isPremium = isPremium
            )
        } else {
            IconsRepository.searchIcons(
                count = count,
                offset = offset,
                query = query,
                isPremium = isPremium
            )
        }

    }

}