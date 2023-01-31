package com.branovitski.qrgenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.branovitski.qrgenerator.Util.toggle
import com.branovitski.qrgenerator.databinding.ActivityMainBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding by viewBinding(ActivityMainBinding::bind, R.id.drawer_layout)

    private val navigator = AppNavigator(this, R.id.container)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router.navigateTo(Screens.qrScanner())
        setupDrawer()

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.qr_generator -> {
                    router.navigateTo(Screens.qrGenerator())
                }
                R.id.qr_scanner -> {
                    router.navigateTo(Screens.qrScanner())
                }

            }
            supportActionBar?.subtitle = null
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            toggle?.isDrawerIndicatorEnabled = false
            false
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun setupDrawer() {
        toggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle!!)
        toggle!!.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle!!.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}