package com.example.coroutineretrofit.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.coroutineretrofit.R
import com.example.coroutineretrofit.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private var mediaPlayer: MediaPlayer? = null
    private var backPressedTime: Long = 0
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.progressBarMainActivity.visibility = View.GONE

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id) {
                R.id.beginFragment,
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.rememberEmailFragment,
                R.id.resetPasswordFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.toolbar.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }


        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener {item ->
            playClickSound()
            NavigationUI.onNavDestinationSelected(item, navController)
            true
        }


        binding.toolbar.title = ""
        binding.toolbar.background = null
        setSupportActionBar(binding.toolbar)



        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment)
                val currentDestination = navController.currentDestination?.id

                if (currentDestination == R.id.beginFragment) {
                    navController.popBackStack()
                } else {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressedTime < 2000) {
                        toast?.cancel()
                        finishAffinity()
                    } else {
                        backPressedTime = currentTime
                        toast = Toast.makeText(this@MainActivity, "Tap back again to close the app.", Toast.LENGTH_SHORT)
                        toast?.show()
                    }
                }
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = navHostFragment.navController
        return when(item.itemId) {
            R.id.logout_action -> {

                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("LOGOUT")
                    .setMessage("It will be logged out securely.")
                    .setIcon(R.drawable.logout_icon_black)

                    .setPositiveButton("Logout") { dialog, which ->
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@MainActivity, "Logged out", Toast.LENGTH_SHORT).show()
                            logOffSound()
                            navController.navigate(R.id.loginFragment)
                        }

                    }

                    .setNegativeButton("Cancel") { dialog, which ->

                    }
                    .create()

                alertDialog.show()
                true
            }
            else -> false
        }
    }

    private fun playClickSound() {
        mediaPlayer?.release()
        val mediaPlayer = MediaPlayer.create(this, R.raw.standard_click)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
    }

    private fun logOffSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.logoff_sound)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            it.release()
            mediaPlayer = null
        }
    }
}