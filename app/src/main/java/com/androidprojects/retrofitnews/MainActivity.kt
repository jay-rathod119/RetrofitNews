package com.androidprojects.retrofitnews

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var adapter: NewsAdapter
    var pageNum = 1
    var totalResults = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "News Channel"
            val descriptionText = "Channel for news notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("news_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        newslist.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(this, emptyList())
        newslist.adapter = adapter
        getNews(pageNum)
    }

    fun getNews(pageNum: Int) {

        val news = NewsService.newsInstance.getHeadlines("in", pageNum)
        news.enqueue(object : Callback<News> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()

                if (news != null) {
                    totalResults = news.totalResults
                    Log.d("hello", news.toString())

                    if (pageNum == 1) {
                        adapter.articles = news.articles
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter.addNews(news.articles)
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("hello", "Error in fetching news", t)
            }
        })
    }

    fun loadMore() {
        if (adapter.articles.size < totalResults) {
            pageNum++
            getNews(pageNum)
        }
    }

    override fun onResume() {
        super.onResume()

        newslist.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                if (lastVisiblePosition == adapter.articles.size - 1) {
                    loadMore()
                }
            }
        })
    }
}

