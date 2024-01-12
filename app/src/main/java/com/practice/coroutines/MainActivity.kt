package com.practice.coroutines

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practice.coroutines.databinding.ActivityMainBinding
import com.practice.coroutines.quote_of_day.QuoteOfTheDayActivity
import com.practice.coroutines.ui.gallery.GalleryActivity
import com.practice.coroutines.ui.quotes.multiple_quote.PopularQuotesActivity
import com.practice.coroutines.ui.supervisor.SupervisorActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnMove.setOnClickListener {
                galleryPermLauncher.launch(galleryPermission)
            }
            btnFetchSingleQuote.setOnClickListener {
                startActivity(Intent(this@MainActivity, QuoteOfTheDayActivity::class.java))
            }
            btnQuotesList.setOnClickListener {
                startActivity(Intent(this@MainActivity, PopularQuotesActivity::class.java))
            }
            btnSupervisorJob.setOnClickListener {
                startActivity(Intent(this@MainActivity, SupervisorActivity::class.java))
            }
        }

    }

    private val galleryPermLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(galleryPermission)) {
                    showSettingsDialog()
                    return@registerForActivityResult
                }
            }

            Toast.makeText(this, "Permission required to view gallery", Toast.LENGTH_SHORT)
                .show()
        }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permission Permanently Denied")
            .setMessage("Please allow permission from settings")
            .setPositiveButton("Go To Settings") { dialog, which ->
                Intent().apply {
                    action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = android.net.Uri.fromParts("package", packageName, null)
                    startActivity(this)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}


val galleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    android.Manifest.permission.READ_MEDIA_VIDEO
} else {
    android.Manifest.permission.READ_EXTERNAL_STORAGE
}