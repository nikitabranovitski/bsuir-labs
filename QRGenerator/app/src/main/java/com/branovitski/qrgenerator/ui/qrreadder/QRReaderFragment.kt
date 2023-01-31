package com.branovitski.qrgenerator.ui.qrreadder

import android.Manifest.permission.CAMERA
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.branovitski.qrgenerator.R
import com.branovitski.qrgenerator.Util
import com.branovitski.qrgenerator.Util.copyToClipboard
import com.branovitski.qrgenerator.databinding.FragmentQRReaderBinding
import com.budiyev.android.codescanner.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRReaderFragment : Fragment(R.layout.fragment_q_r_reader) {

    private val binding by viewBinding(FragmentQRReaderBinding::bind)

    private var scanner: CodeScanner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Util.toggle?.isDrawerIndicatorEnabled = true
        requestPermission()

    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(CAMERA),
            123
        ) else startScanning()
    }

    private fun startScanning() {
        scanner = CodeScanner(requireContext(), binding.scannerView)
        scanner?.camera = CodeScanner.CAMERA_BACK
        scanner?.formats = CodeScanner.ALL_FORMATS

        scanner?.autoFocusMode = AutoFocusMode.SAFE
        scanner?.scanMode = ScanMode.SINGLE
        scanner?.isAutoFocusEnabled = true
        scanner?.isFlashEnabled = false

        scanner?.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                if (!it.text.startsWith("http://") && !it.text.startsWith("https://")){
                    requireContext().copyToClipboard(it.text)
                    Toast.makeText(requireContext(), "${it.text} successfully copied!", Toast.LENGTH_LONG).show()
                }else{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.text)))
                }
            }
        }

        scanner?.errorCallback = ErrorCallback {
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }

        binding.scannerView.setOnClickListener {
            scanner?.startPreview()
        }
    }

}