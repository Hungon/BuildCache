package com.trials.sample001.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trials.sample001.R
import com.trials.sample001.record.Record

class SectionsListAdapter(
    private val layoutId: Int = R.layout.adapter_snap,
    private val listener: (content: Record.Content) -> Unit
) :
    RecyclerView.Adapter<SectionsListAdapter.ViewHolder>() {

    private val records = arrayListOf<Record.SubCategory>()

    fun setRecords(list: List<Record.SubCategory>) {
        records.clear()
        records.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(layoutId, parent, false),
            BookAdapter(listener = listener)
        )
    }

    override fun getItemCount(): Int = records.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(records[position])
    }

    class ViewHolder(view: View, private val bookAdapter: BookAdapter) : RecyclerView.ViewHolder(view) {

        private val textViewTitle: TextView = itemView.findViewById(R.id.text_title)
        private val recyclerViewBookList: RecyclerView =
            itemView.findViewById(R.id.recycler_book_list)
        private val layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.HORIZONTAL, false
        )

        init {
            recyclerViewBookList.apply {
                layoutManager = this@ViewHolder.layoutManager
                adapter = bookAdapter
                clipChildren = false
            }
        }

        fun bind(subCategoryList: Record.SubCategory) {
            textViewTitle.text = subCategoryList.nameCategory
            bookAdapter.setItems(subCategoryList.bookList)
        }

    }
}
