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
import com.sadili.androidapp.githubsocial.adapter.FollowerAdapter
import com.sadili.androidapp.githubsocial.data.Follower
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.*
import org.json.JSONArray
import java.util.ArrayList

class FollowerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
        private lateinit var adapter: FollowerAdapter
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val mBundle = Bundle()
            mBundle.putString(ARG_USERNAME, username)
            fragment.arguments = mBundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        adapter = FollowerAdapter()
        adapter.notifyDataSetChanged()

        rv_follower.setHasFixedSize(true)
        rv_follower.layoutManager = LinearLayoutManager(Activity())
        rv_follower.adapter = adapter

        showLoading(true)
        setFollower(username)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun setFollower(username: String?) {
        val listFollower = ArrayList<Follower>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", "token 7dcb8b7656dc11c46ea3580e5520e6b356ba4fd1")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                try {
                    //parsing JSON
                    val result = String(responseBody)
                    val list = JSONArray(result)

                    for (i in 0 until list.length()) {
                        val follower = list.getJSONObject(i)
                        val followerItems =
                            Follower()
                        followerItems.name = follower.getString("login")
                        followerItems.photo = follower.getString("avatar_url")
                        listFollower.add(followerItems)
                    }

                    //set data ke adapter follower
                    adapter.setDataFollower(listFollower)
                    showLoading(false)
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray, error: Throwable) {
                Log.d(TAG, error.message.toString())
                Toast.makeText(Activity(), error.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}