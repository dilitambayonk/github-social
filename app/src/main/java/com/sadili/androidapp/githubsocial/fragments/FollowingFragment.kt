package com.sadili.androidapp.githubsocial.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sadili.androidapp.githubsocial.R
import com.sadili.androidapp.githubsocial.adapter.FollowingAdapter
import com.sadili.androidapp.githubsocial.data.Following
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import java.util.ArrayList

class FollowingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
        private lateinit var adapter: FollowingAdapter
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val mBundle = Bundle()
            mBundle.putString(ARG_USERNAME, username)
            fragment.arguments = mBundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        rv_following.setHasFixedSize(true)
        rv_following.layoutManager = LinearLayoutManager(Activity())
        rv_following.adapter = adapter

        showLoading(true)
        setFollowing(username)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility  =View.GONE
        }
    }

    fun setFollowing(username: String?) {
        val listFollowing = ArrayList<Following>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", "token 7dcb8b7656dc11c46ea3580e5520e6b356ba4fd1")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val following = list.getJSONObject(i)
                        val followingItems = Following()
                        followingItems.name = following.getString("login")
                        followingItems.photo = following.getString("avatar_url")
                        listFollowing.add(followingItems)
                    }

                    //set data ke adapter following
                    adapter.setDataFollowing(listFollowing)
                    showLoading(false)
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                Log.d(TAG, error.message.toString())
                Toast.makeText(Activity(), error.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}