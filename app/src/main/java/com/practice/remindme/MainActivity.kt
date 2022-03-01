package com.practice.remindme

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.practice.remindme.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_layout.view.*
import java.util.*

class MainActivity : AppCompatActivity(), ToDoAdapter.IToDoAdapter {
    private lateinit var alarmManager: AlarmManager
    private lateinit var materialTimePicker: MaterialTimePicker
    private lateinit var calender: Calendar
    private lateinit var pendingIntent: PendingIntent
    lateinit var viewModel: NoteViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createNotificationChannel()

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ToDoAdapter(this, this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }

        })
        setupClick()

    }


    override fun onItemClicked(notesData: NotesData) {
        viewModel.deleteNode(notesData)
        Toast.makeText(this, "${notesData.text} Deleted", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    fun setupClick() {
        addButton.setOnClickListener {
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.custom_layout, null)
            val builder = AlertDialog.Builder(this)
                .setView(dialogView)
            val alertdialog = builder.show()

            dialogView.timePicker.setOnClickListener {
                materialTimePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Alarm Time")
                    .build()

                materialTimePicker.show(supportFragmentManager, "Reminder")

                materialTimePicker.addOnPositiveButtonClickListener {
                    if (materialTimePicker.hour > 12) {
                        dialogView.timePicker.text =
                            String.format("%02d", (materialTimePicker.hour - 12)) +
                                    " : " + String.format("%02d", materialTimePicker.minute) + " PM"
                    } else {
                        dialogView.timePicker.text =
                            materialTimePicker.hour
                                .toString() + " : " + materialTimePicker.minute + " AM"
                    }

                    calender = Calendar.getInstance()
                    calender.set(Calendar.HOUR_OF_DAY, materialTimePicker.hour)
                    calender.set(Calendar.MINUTE, materialTimePicker.minute)
                    calender.set(Calendar.SECOND, 0)
                    calender.set(Calendar.MILLISECOND, 0)

                }

                materialTimePicker.addOnNegativeButtonClickListener {
                    cancelAlarm()
                }

            }

            dialogView.add_button.setOnClickListener {
                val todoTitle = dialogView.todoEdit.text.toString()
                val todoTime = dialogView.timePicker.text.toString()
                    viewModel.insertNode(NotesData(todoTitle, todoTime))
                setTimer()
                alertdialog.dismiss()
            }

            dialogView.cancel_button.setOnClickListener {
                alertdialog.dismiss()

            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setTimer() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, NotificationReciver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calender.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show()

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarm() {

        val intent = Intent(this, NotificationReciver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Hey"
            val descriptionText = "It's time to do your Task!"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("RemindMe", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}