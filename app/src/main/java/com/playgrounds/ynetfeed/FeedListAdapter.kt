package com.playgrounds.ynetfeed

import android.app.Activity
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.playgrounds.ynetfeed.models.PostItemInfo
import java.util.*

class FeedListAdapter : ListAdapter<PostItemInfo, FeedListAdapter.Holder>(Diff()) {
    var onItemClicked: (PostItemInfo) -> Unit = {}

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.feed_cell_title)
        private val description: TextView = view.findViewById(R.id.feed_cell_description)
        private val date: TextView = view.findViewById(R.id.feed_cell_date)
        fun bind(data: PostItemInfo?, onItemClicked: (PostItemInfo) -> Unit) {
            itemView.isEnabled = data != null
            title.text = data?.title ?: ""
            description.text = data?.description ?: ""
            date.text = data?.time?.let { DateFormat.getDateFormat(itemView.context)?.format(Date(it)) } ?: ""
            itemView.setOnClickListener {
                data?.let(onItemClicked::invoke)
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<PostItemInfo>() {
        override fun areItemsTheSame(oldItem: PostItemInfo, newItem: PostItemInfo) = oldItem == newItem
        override fun areContentsTheSame(oldItem: PostItemInfo, newItem: PostItemInfo) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = (parent.context as Activity).layoutInflater
        return Holder(inflater.inflate(R.layout.feed_list_cell, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClicked)
    }
}
