package com.sadili.androidapp.githubsocial.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sadili.androidapp.githubsocial.DetailUser
import com.sadili.androidapp.githubsocial.R
import com.sadili.androidapp.githubsocial.data.User
import kotlinx.android.synthetic.main.user_items.view.*
import java.util.ArrayList

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val listUser = ArrayList<User>()

    fun setDataUser(items: ArrayList<User>) {
        listUser.clear()
        listUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_items, viewGroup, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_user)

                tv_user_name.text = user.name

                itemView.setOnClickListener {
                    val toDetail = Intent(itemView.context, DetailUser::class.java)
                    toDetail.putExtra(DetailUser.EXTRA_NAME, user.name)
                    toDetail.putExtra(DetailUser.EXTRA_PHOTO, user.photo)
                    itemView.context.startActivity(toDetail)
                }
            }
        }
    }
}