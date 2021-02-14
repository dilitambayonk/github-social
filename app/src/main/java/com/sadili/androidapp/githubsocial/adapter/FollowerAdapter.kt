package com.sadili.androidapp.githubsocial.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sadili.androidapp.githubsocial.data.Follower
import com.sadili.androidapp.githubsocial.R
import com.sadili.androidapp.githubsocial.loader.loadImage
import kotlinx.android.synthetic.main.follower_items.view.*
import java.util.ArrayList

class FollowerAdapter : RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {
    private val listFollower = ArrayList<Follower>()

    fun setDataFollower(items: ArrayList<Follower>) {
        listFollower.clear()
        listFollower.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowerViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.follower_items, viewGroup, false)
        return FollowerViewHolder(view)
    }


    override fun getItemCount(): Int {
        return listFollower.size
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.bind(listFollower[position])
    }

    class FollowerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(follower: Follower) {
            with(itemView) {
                img_follower.loadImage(follower.photo)

                tv_followers_name.text = follower.name
            }
        }
    }
}