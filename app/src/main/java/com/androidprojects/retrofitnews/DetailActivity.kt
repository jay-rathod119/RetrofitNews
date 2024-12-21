package com.androidprojects.retrofitnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.androidprojects.retrofitnews.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val url = intent.getStringExtra("KEY")
//        val url = "https://www.google.com/"

        if(url !=null){
            DetailWebview.settings.javaScriptEnabled = true
            DetailWebview.webViewClient = object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                    DetailWebview.visibility = View.VISIBLE
                }

            }
            DetailWebview.loadUrl(url)

        }
    }
}