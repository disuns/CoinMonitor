package com.android.trade.presentation.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.trade.common.utils.dataStore
import com.android.trade.presentation.R
import com.android.trade.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    private val navController: NavController by lazy {
        findNavController(R.id.container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            getDarkModeSetting()
                .conflate()
                .collect { isDarkMode ->
                    setAppTheme(isDarkMode)
                }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnDarkMode.setOnClickListener {
            lifecycleScope.launch {
                val currentMode = getDarkModeSetting().first()
                saveDarkModeSetting(!currentMode)
            }
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        val mode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun getDarkModeSetting() = dataStore.data.map { preferences -> preferences[DARK_MODE_KEY] ?: false }

    private fun saveDarkModeSetting(isDarkMode: Boolean) {
        lifecycleScope.launch {
            dataStore.edit { settings -> settings[DARK_MODE_KEY] = isDarkMode }
        }
    }
}