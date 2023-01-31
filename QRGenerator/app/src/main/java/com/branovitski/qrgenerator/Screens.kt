package com.branovitski.qrgenerator

import com.branovitski.qrgenerator.ui.qrgenerator.QRGeneratorFragment
import com.branovitski.qrgenerator.ui.qrreadder.QRReaderFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen


object Screens {

    fun qrGenerator() = FragmentScreen { QRGeneratorFragment() }
    fun qrScanner() = FragmentScreen { QRReaderFragment() }
}