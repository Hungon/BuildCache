package com.trials.sample001.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trials.sample001.R
import com.trials.sample001.record.Record

class BookAdapter(private val layoutId: Int = R.layout.adapter_book, private val listener: (content: Record.Content) -> Unit) :
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private val books = arrayListOf<Record.Content>()

    fun setItems(list: List<Record.Content>) {
        books.clear()
        books.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(layoutId, parent, false)
        )
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(books[position].imageUrl).into(holder.imageViewBook)
        holder.imageViewBook.setOnClickListener {
            listener.invoke(books[position])
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewBook: ImageView = itemView.findViewById(R.id.image_book)
     }
}
