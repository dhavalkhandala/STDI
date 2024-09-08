package com.stdi.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.stdi.R
import com.stdi.model.MoreAppDetailsModel
import java.util.*


class MoreAppAdapter (
    var activityContext: Activity,
    var inflateList: ArrayList<MoreAppDetailsModel>
) : RecyclerView.Adapter<MoreAppAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view :View= LayoutInflater.from(activityContext).inflate(R.layout.row_item_more_app, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (inflateList == null) {
            return 0
        }else{
            return inflateList.size
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val app_icon:ImageView=itemView.findViewById(R.id.iv_app_icon)
        val bg:ImageView=itemView.findViewById(R.id.iv_background)
        val app_name:TextView=itemView.findViewById(R.id.tv_app_name)
        val app_desc:TextView=itemView.findViewById(R.id.tv_app_desc)
        val app_down:TextView=itemView.findViewById(R.id.tv_app_download)
        val app_install:TextView=itemView.findViewById(R.id.tv_install)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var bg_image:ArrayList<Int> = ArrayList()
        bg_image.add(R.drawable.first)
        bg_image.add(R.drawable.second)
        bg_image.add(R.drawable.third)
        bg_image.add(R.drawable.fourth)
       /* val random_method = Random()
        val index: Int = random_method.nextInt(bg_image.size)*/
        if(position%4==0){
            Glide.with(activityContext).load(bg_image[3]).into(holder.bg)
        }else if (position%3==0){
            Glide.with(activityContext).load(bg_image[2]).into(holder.bg)
        }else if (position%2==0){
            Glide.with(activityContext).load(bg_image[1]).into(holder.bg)
        }else{
            Glide.with(activityContext).load(bg_image[0]).into(holder.bg)
        }



        val mData=inflateList[position]
        Glide.with(activityContext).load(mData.app_icon).into(holder.app_icon)
        holder.app_name.setText(mData.app_name)
        holder.app_desc.setText(mData.app_desc)
        holder.app_down.setText(mData.app_download)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=${mData.app_package}"))
                try {
                    activityContext.startActivity(Intent(intent).setPackage("com.android.vending"))
                } catch (exception: ActivityNotFoundException) {
                    activityContext.startActivity(intent)
                }
            }
        })
        holder.app_install.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=${mData.app_package}"))
                try {
                   activityContext.startActivity(Intent(intent).setPackage("com.android.vending"))
                } catch (exception: ActivityNotFoundException) {
                    activityContext.startActivity(intent)
                }
            }
        })
    }


}