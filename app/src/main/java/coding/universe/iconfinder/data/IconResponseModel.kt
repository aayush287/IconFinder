package coding.universe.iconfinder.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BaseResponse(
    @SerializedName("total_count") val totalCount : Int,
    @SerializedName("icons") val icons:List<Icon>
) : Serializable

data class Icon(
    @SerializedName("is_premium") val isPremium: Boolean,
    @SerializedName("icon_id") val iconId: Int,
    @SerializedName("raster_sizes") val rasterSize: List<RasterSize>,
    @SerializedName("prices") val prices : List<Price>
) : Serializable

data class FormatData(
    @SerializedName("download_url") val downloadUrl: String,
    @SerializedName("format") val format: String,
    @SerializedName("preview_url") val previewUrl : String
) : Serializable

data class RasterSize(
    @SerializedName("size_width") val width : Int,
    @SerializedName("size_height") val height : Int,
    @SerializedName("formats") val formats : List<FormatData>
) : Serializable

data class Price(
    @SerializedName("price") val price : Int,
    @SerializedName("currency") val currency : String
) : Serializable