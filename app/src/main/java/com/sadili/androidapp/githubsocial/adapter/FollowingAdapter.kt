package com.sadili.androidapp.githubsocial.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sadili.androidapp.githubsocial.data.Following
import com.sadili.androidapp.githubsocial.R
import com.sadili.androidapp.githubsocial.loader.loadImage
import kotlinx.android.synthetic.main.following_items.view.*
import java.util.ArrayList

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private val listFollowing = ArrayList<Following>()

    fun setDataFollowing(items: ArrayList<Following>) {
        listFollowing.clear()
        listFollowing.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.following_items, viewGroup, false)
        return FollowingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFollowing.size
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listFollowing[position])
    }

    class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(following: Following) {
            with(itemView) {
                img_following.loadImage(following.photo)

                tv_following_name.text = following.name
            }
        }
    }
}