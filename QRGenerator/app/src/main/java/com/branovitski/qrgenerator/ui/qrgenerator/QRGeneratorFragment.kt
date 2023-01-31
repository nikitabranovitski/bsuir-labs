package com.branovitski.qrgenerator.ui.qrgenerator

import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.branovitski.qrgenerator.R
import com.branovitski.qrgenerator.Util
import com.branovitski.qrgenerator.databinding.FragmentQRGeneratorBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QRGeneratorFragment : Fragment(R.layout.fragment_q_r_generator) {

    private val binding by viewBinding(FragmentQRGeneratorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Util.toggle?.isDrawerIndicatorEnabled = true

        binding.generateButton.setOnClickListener {
            val qrgEncoder =
                QRGEncoder(binding.editText.text.toString(), null, QRGContents.Type.TEXT, 800)
            qrgEncoder.colorBlack = Color.WHITE;
            qrgEncoder.colorWhite = Color.BLACK;

            try {
                val bitmap = qrgEncoder.bitmap
                binding.qrImageView.setImageBitmap(bitmap)
                binding.editText.setText("")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}