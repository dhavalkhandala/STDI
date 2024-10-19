package com.stdi.adapter

import android.app.Activity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.stdi.R
import com.stdi.activity.HomeActivity

class VerticalListAdapter(
    private var activity: Activity,
    private var titleList: MutableList<String>,
    private var infoList: MutableList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.row_vertical_list, parent, false
        )
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is MyViewHolder) {
            viewHolder.bind(position)
        }
    }

    private inner class MyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val llMain: LinearLayout = itemView.findViewById(R.id.llMain)
        private val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        private val tvInfo: AppCompatTextView = itemView.findViewById(R.id.tvInfo)

        init {
            llMain.setOnClickListener {
                (activity as HomeActivity).callDetailActivity(adapterPosition)
            }
        }

        fun bind(position: Int) {
            tvTitle.text = titleList[position]

            if (infoList.size > 0 && position <= infoList.size - 1 && !TextUtils.isEmpty(infoList[position])) {
                tvInfo.visibility = View.VISIBLE
                tvInfo.text = infoList[position]
            } else tvInfo.visibility = View.GONE
        }

    }
}