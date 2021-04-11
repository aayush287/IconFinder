package coding.universe.iconfinder

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import coding.universe.iconfinder.adapters.SelectFormatAdapter
import coding.universe.iconfinder.data.RasterSize
import coding.universe.iconfinder.databinding.DialogSelectFormatBinding
import coding.universe.iconfinder.util.DownloadOnItemClickListener
import coding.universe.iconfinder.util.downLoadResourcesWithPath


private const val RASTER_LIST = "raster-list"

private const val WRITE_STORAGE_PERMISSION = 101


class SelectFormatFragment : DialogFragment(), DownloadOnItemClickListener {

    private lateinit var binding : DialogSelectFormatBinding

    private val formatAdapter by lazy { SelectFormatAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DialogSelectFormatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfRasterSize = arguments?.getSerializable(RASTER_LIST)

        binding.sizeListView.adapter = formatAdapter

        formatAdapter.updateList(listOfRaster = listOfRasterSize as ArrayList<RasterSize>)

    }

    override fun onItemClick(url: String, fileName: String) {
        Log.d(TAG, "onItemClick: fileName -> $fileName")
        Log.d(TAG, "onItemClick: url -> $url")
        if (!hasPermissions()){
            downLoadResourcesWithPath(requireActivity(), url, fileName)
            dismiss()
        }else{
            requestPermissions()
        }
    }

    private var permissions =
        arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun hasPermissions(): Boolean {
        return permissions.any {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissions(permissions, WRITE_STORAGE_PERMISSION)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_STORAGE_PERMISSION)
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            } else {
                Toast.makeText(context, "Permission needed to download file", Toast.LENGTH_LONG).show()
                dismiss()
            }
    }


    companion object {

        @JvmStatic
        fun newInstance(listOfRasterSize: ArrayList<RasterSize>) =
            SelectFormatFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RASTER_LIST, listOfRasterSize)
                }
            }
    }
}