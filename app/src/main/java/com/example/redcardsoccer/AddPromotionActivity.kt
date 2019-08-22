package com.example.redcardsoccer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_promotion.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AddPromotionActivity : AppCompatActivity() {

    //this is where Kotlin defines global variables
    companion object {
        var selectedPhotoUri : Uri? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promotion)

        image_view_add_promotion.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    //runs after an activity is finished (specifically when the photo selector is closed)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data!= null){
            //shows photo selected in a circular image view
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            image_view_add_promotion.setImageBitmap(bitmap)
        }
    }//OnActivityResult function

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_promotion_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId)
        {
            R.id.save_promotion -> {

                if(selectedPhotoUri == null)
                {
                    Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show()
                }
                else if(description_edit_text_add_promotion.text.toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Please write a description", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    uploadPromotionImage()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun uploadPromotionImage(){
        val fileName  = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        //pushes the file to firebase storage
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    savePromotion(it.toString())
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

    }

    private fun savePromotion(profileImageUrl: String){
        val timeStamp = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val dateAndTime = timeStamp.format(timeFormatter)

        val ref = FirebaseDatabase.getInstance().getReference("/promotions").push()
        ref.setValue(PromotionObject(ref.key!!, profileImageUrl, description_edit_text_add_promotion.text.toString().trim(), dateAndTime))
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully saved promotion", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

    }
}
