package com.example.slot_one

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.slot_one.databinding.ActivityMainBinding
import io.michaelrocks.paranoid.Obfuscate

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val checkConnection by lazy { CheckConnectivity(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            if (checkConnection.isNetworkAvailable(application)) {
                Log.d("watan", "1-Active")

                checkConnection.observe(this@MainActivity) {
                    Log.d("Connection", it.toString())

                    if (it) {
                        val alertDialog = AlertDialog.Builder(this@MainActivity)
                        Log.d("Connection", " Active")

                    } else {
                        val alertDialog = AlertDialog.Builder(this@MainActivity)
                        Log.d("Connection", " !Active")
                        alertDialog.apply {
                            setTitle("Connection result")
                            setMessage("Result : $it")
                            show()
                        }

                    }
                }
            } else {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                Log.d("watan", "2-!Active")
                alertDialog.apply {
                    setPositiveButton("Exit Game") { _, _ ->
                        this@MainActivity.finish()
                    }
                    setTitle("Connection result")
                    setMessage("Please connect the internet")
                    show()
                }
            }
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.findNavController()
    }
}
