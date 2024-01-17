package com.sumin.activityresultapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var getUsernameButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var getImageButton: Button
    private lateinit var imageFromGalleryImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()

        val usernameLauncher = getUsernameLauncher()
        val imageLauncher = getImageLauncher()

        getUsernameButton.setOnClickListener {
            usernameLauncher.launch(Unit)
        }
        getImageButton.setOnClickListener {
            imageLauncher.launch(Unit)
        }
    }

    private fun getImageLauncher(): ActivityResultLauncher<Unit> {
        val imageContract = object: ActivityResultContract<Unit, Uri?>() {

            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(Intent.ACTION_PICK).apply {
                    type = "image/*" // MIME types
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return intent?.data
            }
        }

        return registerForActivityResult(imageContract) {
            it?.let {
                imageFromGalleryImageView.setImageURI(it)
            }
        }
    }

    private fun getUsernameLauncher(): ActivityResultLauncher<Unit> {
        val usernameContract = object: ActivityResultContract<Unit, String?>() {

            override fun createIntent(context: Context, input: Unit): Intent {
                return UsernameActivity.newIntent(context)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): String? {
                if (resultCode == RESULT_OK) {
                    return intent?.getStringExtra(UsernameActivity.EXTRA_USERNAME)
                }
                return null
            }
        }

        return registerForActivityResult(usernameContract) {
            if (!it.isNullOrBlank()) {
                usernameTextView.text = it
            }
        }
    }

    private fun initViews() {
        getUsernameButton = findViewById(R.id.get_username_button)
        usernameTextView = findViewById(R.id.username_textview)
        getImageButton = findViewById(R.id.get_image_button)
        imageFromGalleryImageView = findViewById(R.id.image_from_gallery_imageview)
    }

}




