package com.practice.remindme

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(private val context: Context, private val listener: IToDoAdapter) :
    RecyclerView.Adapter<ToDoAdapter.toDOViewHolder>() {

    val todos = ArrayList<NotesData>()

    class toDOViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val deleteBtn = itemView.findViewById<ImageView>(R.id.deleteicon)
        val textView = itemView.findViewById<TextView>(R.id.output)
        val timeView = itemView.findViewById<TextView>(R.id.selectedTime)
        val idnum = itemView.findViewById<TextView>(R.id.idnum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): toDOViewHolder {
        val viewHolder = toDOViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.itemlist_todoactivity, parent, false))
        viewHolder.deleteBtn.setOnClickListener {
            listener.onItemClicked(todos[viewHolder.adapterPosition] )
        }

        return viewHolder


    }

    override fun getItemCount(): Int {
        return todos.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: toDOViewHolder, position: Int) {

        val currentNote  = todos[position]
        holder.textView.text = currentNote.text
        holder.timeView.text = currentNote.time
        holder.idnum.text = (position +1).toString()

    }


    fun  updateList(newList : List<NotesData>){
        todos.clear()
        todos.addAll(newList)

        notifyDataSetChanged()
    }

    interface IToDoAdapter{
        fun onItemClicked(notesData: NotesData)
    }
}