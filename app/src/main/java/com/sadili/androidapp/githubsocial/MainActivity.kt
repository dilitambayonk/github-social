package com.sadili.androidapp.githubsocial

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sadili.androidapp.githubsocial.adapter.UserAdapter
import com.sadili.androidapp.githubsocial.data.User
import com.sadili.androidapp.githubsocial.setting.SettingActivity
import com.shashank.sony.fancytoastlib.FancyToast
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            supportActionBar?.setLogo(R.drawable.github_mark_logo_light)
            supportActionBar?.setDisplayUseLogoEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = getString(R.string.title_Main)
        }

        rv_users.setHasFixedSize(true)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchUser.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchUser.queryHint = resources.getString(R.string.search_hint)
        searchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                setListUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        img_user_not_found.visibility = View.VISIBLE
        text_main.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val intentToFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intentToFavorite)
            }
            R.id.setting_menu -> {
                val intentToSetting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intentToSetting)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
            img_user_not_found.visibility = View.GONE
            text_main.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setListUser(userNames: String) {
        val listUser = ArrayList<User>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$userNames"
        BuildConfig.GITHUB_TOKEN
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    Log.d(TAG, result)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val user = list.getJSONObject(i)
                        val userItems = User()
                        userItems.name = user.getString("login")
                        userItems.photo = user.getString("avatar_url")
                        listUser.add(userItems)
                    }

                    adapter.setDataUser(listUser)
                    showLoading(false)

                    //jika user tidak ditemukan
                    val userFounded = responseObject.getInt("total_count")
                    if (userFounded < 1) {
                        img_user_not_found.visibility = View.VISIBLE
                        text_not_found.visibility = View.VISIBLE

                        FancyToast.makeText(this@MainActivity, "User Tidak Ditemukan!",
                            FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d(TAG, error.message.toString())
                showLoading(false)
                FancyToast.makeText(this@MainActivity, error.message.toString(),
                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }
        })
    }
}