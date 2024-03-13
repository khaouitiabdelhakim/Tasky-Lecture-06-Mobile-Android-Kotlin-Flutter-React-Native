package com.abdelhakim.tasky

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdelhakim.tasky.adapters.TasksAdapter
import com.abdelhakim.tasky.models.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var tasksRef: DatabaseReference
    private lateinit var taskEditText: EditText
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasks: MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        tasksRef = database.getReference("tasks")

        // Initialize views
        taskEditText = findViewById(R.id.taskEditText)
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasks = mutableListOf()
        tasksAdapter = TasksAdapter(tasks)

        // Set up RecyclerView
        tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tasksAdapter
        }

        // Read tasks from the database
        tasksRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasks.clear()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    task?.let {
                        tasks.add(it)
                    }
                }
                tasksAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // Add task to the database
        findViewById<Button>(R.id.addButton).setOnClickListener {
            val text = taskEditText.text.toString()
            if (text.isNotEmpty()) {
                val taskRef = tasksRef.push() // Generate unique ID for the task
                val task = Task(taskRef.key!!, text, false) // Pass the generated key to the Task constructor
                taskRef.setValue(task)
                taskEditText.text.clear()
            }
        }

    }
}
