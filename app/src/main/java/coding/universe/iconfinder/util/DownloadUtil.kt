package coding.universe.iconfinder.util

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import coding.universe.iconfinder.network.API_KEY

//_________________** Downloading the resources from url**_________________________________
fun downLoadResourcesWithPath(activity: Activity, url: String, fileName: String) {

    val downloadManager =
        activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri: Uri = Uri.parse(url)
    val request: DownloadManager.Request = DownloadManager.Request(uri).apply {
        addRequestHeader("Authorization", "Bearer $API_KEY")
        setTitle(fileName)
        setDescription("Downloading...")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "/IconFinder/$fileName"
        )
    }

    downloadManager.enqueue(request)
}