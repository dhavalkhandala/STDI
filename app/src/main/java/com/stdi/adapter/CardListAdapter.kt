package com.stdi.adapter

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stdi.R
import com.stdi.activity.HomeActivity

class CardListAdapter(
    private var activity: Activity,
    private var titleList: MutableList<String>,
    private var infoList: MutableList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.row_card_list, parent, false
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

    private inner class MyViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val llMain: LinearLayout = itemView.findViewById(R.id.llMain)
        private val tvTitle: AppCompatTextView = itemView.findViewById(R.id.tvTitle)
        private val tvInfo: AppCompatTextView = itemView.findViewById(R.id.tvInfo)
        private val tvReadMore: AppCompatTextView = itemView.findViewById(R.id.tvReadMore)

        init {
            tvReadMore.setOnClickListener {
                    (activity as HomeActivity).callDetailActivity(adapterPosition)
                    Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }

        fun bind(position: Int) {
            when (position % 5) {
                1 -> {
                    llMain.setBackgroundResource(R.drawable.gradient_brown)
                    tvReadMore.setTextColor(ContextCompat.getColor(activity, R.color.brown_dark))
                }
                2 -> {
                    llMain.setBackgroundResource(R.drawable.gradient_drak_pink)
                    tvReadMore.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.pink_dark_dark
                        )
                    )
                }
                3 -> {
                    llMain.setBackgroundResource(R.drawable.gradient_green)
                    tvReadMore.setTextColor(ContextCompat.getColor(activity, R.color.green_dark))
                }
                4 -> {
                    llMain.setBackgroundResource(R.drawable.gradient_pink)
                    tvReadMore.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.pink_light_dark
                        )
                    )
                }
                else -> {
                    llMain.setBackgroundResource(R.drawable.gradient_navy_blue)
                    tvReadMore.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            R.color.navy_blue_dark
                        )
                    )
                }
            }

            tvTitle.text = titleList[position]

            if (infoList.size > 0 && position <= infoList.size - 1 && !TextUtils.isEmpty(infoList[position])) {
                tvInfo.visibility = View.VISIBLE
                tvInfo.text = infoList[position]
            } else tvInfo.visibility = View.GONE

        }

    }
}