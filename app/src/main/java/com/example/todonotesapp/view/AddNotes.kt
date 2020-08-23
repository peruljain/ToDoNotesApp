package com.example.todonotesapp.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.todonotesapp.BuildConfig
import com.example.todonotesapp.R
import com.example.todonotesapp.utils.AppConstants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddNotes : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var saveButton: Button
    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_CODE_CAMERA = 2
    private var picturePath = ""
    private val MY_PERMISSION_CODE = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        bindView()
        clickListener()
    }

    private fun clickListener() {
        imageView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (isPermissionChecked()){
                    setUpDialog()
                }
            }
        })
        saveButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent()
                intent.putExtra(AppConstants.TITLE, title.text.toString())
                intent.putExtra(AppConstants.DESCRIPTION, description.text.toString())
                intent.putExtra(AppConstants.IMAGE_PATH, picturePath)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        })
    }

    private fun isPermissionChecked(): Boolean {

        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val listOfPermission = ArrayList<String>()

        if (cameraPermission !=  PackageManager.PERMISSION_GRANTED)
            listOfPermission.add(android.Manifest.permission.CAMERA)

        if (storagePermission != PackageManager.PERMISSION_GRANTED)
            listOfPermission.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if (listOfPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listOfPermission.toTypedArray<String>(), MY_PERMISSION_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            MY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)   // Granted permission grantResults array me aa jaati hai
                    setUpDialog()
            }
        }
    }

    private fun setUpDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_selector, null)
        val galleyText = view.findViewById<TextView>(R.id.galleryTVID)
        val cameraText : TextView = view.findViewById(R.id.cameraTVID)
        val alertDialog = AlertDialog.Builder(this).setView(view).setCancelable(true).create()
        alertDialog.show()

        galleyText.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_CODE_GALLERY)
                alertDialog.hide()
            }
        })

        cameraText.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var photoFile: File? = null
                photoFile = createImage()
                if (photoFile!=null) {
                    val photoUri = FileProvider.getUriForFile(this@AddNotes, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                    picturePath = photoFile.absolutePath
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(intent, REQUEST_CODE_CAMERA)
                    alertDialog.hide()
                }
            }
        })

    }

    private fun createImage(): File {

        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val fileName = "JPEG_" + timeStamp + "_"
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_GALLERY -> {
                    val imageUri = data?.data
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(imageUri!!, filePath, null, null, null)
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePath[0])
                    picturePath = cursor?.getString(columnIndex!!) !!
                    cursor.close()
                    Glide.with(this).load(picturePath).into(imageView)
                }
                REQUEST_CODE_CAMERA -> {
                    Glide.with(this).load(picturePath).into(imageView)
                }
            }
        }
    }

    private fun bindView() {
        imageView = findViewById(R.id.image)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)
        saveButton = findViewById(R.id.save)
    }
}
