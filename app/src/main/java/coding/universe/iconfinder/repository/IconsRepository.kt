package coding.universe.iconfinder.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coding.universe.iconfinder.data.Icon
import coding.universe.iconfinder.network.ApiClient
import coding.universe.iconfinder.network.enqueue

object IconsRepository {

    private val iconData = MutableLiveData<List<Icon>?>()

    fun getIconData(): LiveData<List<Icon>?> {
        return iconData
    }

    fun loadIcons(count: Int, offset: Int, category: String, isPremium: Boolean?) {

        ApiClient.getInstance().apis?.getIconsInSpecificCategory(
            category = category,
            count = count,
            offset = offset,
            premium = isPremium
        )?.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    iconData.value = it.body()?.icons
                    Log.d(TAG, "loadIcons: data -> ${it.body()?.icons}")
                } else {
                    iconData.value = null
                }

            }

            onFailure = {
                iconData.value = null
            }
        }

    }

    fun searchIcons(count: Int, offset: Int, query: String, isPremium: Boolean?) {
        ApiClient.getInstance().apis?.getSearchedIcons(
            query = query,
            count = count,
            offset = offset,
            premium = isPremium
        )?.enqueue {
            onResponse = {
                if (it.isSuccessful && it.body() != null) {
                    iconData.value = it.body()?.icons
                    Log.d(TAG, "loadIcons: data -> ${it.body()?.icons}")
                } else {
                    iconData.value = null
                }

            }

            onFailure = {
                iconData.value = null
            }
        }

    }

}