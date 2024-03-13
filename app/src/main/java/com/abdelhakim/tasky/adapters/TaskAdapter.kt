package com.abdelhakim.tasky.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abdelhakim.tasky.R
import com.abdelhakim.tasky.models.Task
import com.google.firebase.database.FirebaseDatabase

class TasksAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskText: TextView = itemView.findViewById(R.id.taskText)
        private val taskCheckbox: CheckBox = itemView.findViewById(R.id.taskCheckbox)

        fun bind(task: Task) {
            taskText.text = task.text
            taskCheckbox.isChecked = task.completed
            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                // Update task completion status in the database
                val taskRef = FirebaseDatabase.getInstance().getReference("tasks").child(task.id.toString())
                taskRef.child("completed").setValue(isChecked)
            }
        }
    }
}
