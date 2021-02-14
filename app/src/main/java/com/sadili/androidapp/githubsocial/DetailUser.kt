package com.sadili.androidapp.githubsocial

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sadili.androidapp.githubsocial.adapter.SectionsPagerAdapter
import com.sadili.androidapp.githubsocial.db.UserContract
import com.sadili.androidapp.githubsocial.db.UserHelper
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.json.JSONObject

class DetailUser : AppCompatActivity() {

    private var statusFavorite = false
    private lateinit var userHelper: UserHelper
    private var getName: String = ""
    private var getPhoto: String = ""

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO = "extra_photo"
        private val TAG = DetailUser::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        getName = intent.getStringExtra(EXTRA_NAME)
        getName.let { setDetailUser(getName) }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = getName
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        isFavoriteUser()

        getPhoto = intent.getStringExtra(EXTRA_PHOTO)
        fab_favorite.setOnClickListener {
            setFavoriteUser()
        }
    }

    private fun setFavoriteUser() {
        statusFavorite = !statusFavorite
        if (statusFavorite) {
            val values = ContentValues()
            values.put(UserContract.UserColumns.USERNAME, getName)
            values.put(UserContract.UserColumns.PHOTO, getPhoto)
            userHelper.insert(values)

            showSnack(this, "User ditambahkan ke Favorite")
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        } else {
            userHelper.deleteById(getName)
            showSnack(this, "User dihapus dari Favorite")
            statusFavorite = false
            setStatusFavorite(statusFavorite)
        }
    }

    private fun isFavoriteUser() {
        val dataUser: Cursor = userHelper.queryByUsername(getName)
        if (dataUser.moveToNext()) {
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        }
    }


    private fun setDetailUser(userName: String?) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$userName"
        BuildConfig.GITHUB_TOKEN
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    val dataUser = JSONObject(result)
                    val name = dataUser.getString("name")
                    val photo = dataUser.getString("avatar_url")
                    val country = dataUser.getString("location")
                    val followers = dataUser.getInt("followers")
                    val following = dataUser.getInt("following")

                    //set data user ke view
                    Glide.with(applicationContext)
                        .load(photo)
                        .apply(RequestOptions())
                        .into(img_user)

                    tv_user_name.text = name
                    tv_location.text = country
                    tv_followers.text = followers.toString()
                    tv_following.text = following.toString()

                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d(TAG, error.message.toString())
                Toast.makeText(this@DetailUser, error.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fab_favorite.setImageResource(R.drawable.baseline_favorite_white_48)
        } else {
            fab_favorite.setImageResource(R.drawable.baseline_favorite_border_white_48)
        }
    }

    private fun showSnack(activity: Activity, message: String) {
        val rootView = activity.window.decorView
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val message = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            message.view.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorAccent))
            message.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).setTextColor(Color.DKGRAY)
            message.show()
        } else {
            Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}