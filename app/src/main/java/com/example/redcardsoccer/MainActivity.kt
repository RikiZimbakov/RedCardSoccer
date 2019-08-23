package com.example.redcardsoccer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var active = false
    }

    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder

    private val channelId = "com.example.redcardsoccer"
    private val description = "Red Card Soccer"
    private val notificationID = 1999

    val promotionsArrayList = ArrayList<PromotionObject>()
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        setTitle("RedCardSoccer Retail Store")
        recyclerview_main.adapter = adapter
        pullPromotions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.add_promotion -> {
                val intent = Intent(this,AddPromotionActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun pullPromotions() {
        val reference = FirebaseDatabase.getInstance().getReference("/promotions")
        reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val promotionObject = p0.getValue(PromotionObject::class.java)!!
                promotionsArrayList.add(promotionObject)
                refreshRecyclerView()
                showNotification(promotionObject.description)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun refreshRecyclerView(){
        adapter.clear()
        promotionsArrayList.forEach{
            adapter.add(PromotionRow(it))
        }
    }

    private fun showNotification(pulledDescription: String){

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if(!active) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setContentTitle("New post!")
                    .setContentText(pulledDescription)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setStyle(Notification.BigTextStyle().bigText(pulledDescription))
                    .setSmallIcon(R.drawable.abc_ic_star_black_48dp)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.abc_ic_star_black_48dp))
                    .setContentIntent(pendingIntent)
            }
            else {
                builder = Notification.Builder(this)
                    .setContentTitle("New post!")
                    .setContentText(pulledDescription)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setStyle(Notification.BigTextStyle().bigText(pulledDescription))
                    .setSmallIcon(R.drawable.abc_ic_star_black_48dp)
                    .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.abc_ic_star_black_48dp))
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(notificationID, builder.build())
        }
    }

    override fun onStart() {
        active = true
        super.onStart()
    }

    override fun onStop() {
        active = false
        super.onStop()
    }
}
