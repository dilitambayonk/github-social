package com.sadili.androidapp.githubsocial.adapter

import android.content.ContentValues
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sadili.androidapp.githubsocial.DetailUser
import com.sadili.androidapp.githubsocial.R
import com.sadili.androidapp.githubsocial.data.User
import com.sadili.androidapp.githubsocial.db.UserContract.UserColumns.Companion.PHOTO
import com.sadili.androidapp.githubsocial.db.UserContract.UserColumns.Companion.USERNAME
import com.sadili.androidapp.githubsocial.db.UserHelper
import com.sadili.androidapp.githubsocial.loader.loadImage
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.favorite_items.view.*
import java.util.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_items, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int {
        return this.listFavorite.size
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var statusFavorite = false

        fun bind(user: User) {
            with(itemView) {
                tv_user_name.text = user.name
                img_user.loadImage(user.photo)

                itemView.setOnClickListener {
                    val toDetail = Intent(itemView.context, DetailUser::class.java)
                    toDetail.putExtra(DetailUser.EXTRA_NAME, user.name)
                    toDetail.putExtra(DetailUser.EXTRA_PHOTO, user.photo)
                    itemView.context.startActivity(toDetail)
                }

                img_favorite_user.setOnClickListener {
                    val userHelper = UserHelper(itemView.context)
                    statusFavorite = !statusFavorite

                    if (statusFavorite) {
                        userHelper.deleteById(user.name.toString())

                        img_favorite_user.setColorFilter(
                            ContextCompat.getColor(context, android.R.color.white),
                            android.graphics.PorterDuff.Mode.MULTIPLY)

                        statusFavorite = true
                        FancyToast.makeText(itemView.context, "User dihapus dari Favorite",
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
                    } else {
                        val values = ContentValues()
                        values.put(USERNAME, user.name)
                        values.put(PHOTO, user.photo)
                        userHelper.insert(values)

                        img_favorite_user.setColorFilter(
                            ContextCompat.getColor(context, R.color.colorBgPink),
                            android.graphics.PorterDuff.Mode.MULTIPLY)

                        statusFavorite = false
                        FancyToast.makeText(itemView.context, "User ditambahkan ke Favorite",
                            FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                    }
                }
            }
        }
    }
}