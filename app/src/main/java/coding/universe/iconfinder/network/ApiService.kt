package coding.universe.iconfinder.network

import coding.universe.iconfinder.data.BaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("icons/search")
    fun getIconsInSpecificCategory(
        @Query("category") category : String,
        @Query("count") count : Int,
        @Query("offset") offset : Int,
        @Query("premium") premium : Boolean?
    ) : Call<BaseResponse>

    @GET("icons/search")
    fun getSearchedIcons(
        @Query("count") count : Int,
        @Query("offset") offset : Int,
        @Query("query") query : String,
        @Query("premium") premium : Boolean?
    ) : Call<BaseResponse>

}