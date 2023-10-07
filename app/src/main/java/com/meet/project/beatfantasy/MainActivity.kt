package com.meet.project.beatfantasy

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.meet.project.beatfantasy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        initData()

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null)
                    if (url.contains("dream11.com")) {
                        val intent = packageManager.getLaunchIntentForPackage("com.dream11.fantasy.cricket.football.kabaddi")
                        if (intent != null) {
                            startActivity(intent)
                        } else {
                            openInBrowser(url)
                        }
                        return true
                    } else if (url.contains("t.me")) {
                        if (appInstalledOrNot("org.telegram.messenger")) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent)
                        } else {
                            openInBrowser(url)
                        }
                        return true
                    } else if (url.contains("phonepe.com")) {
                        openInBrowser(url)
                        return true
                    }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webView.visibility = View.VISIBLE
                binding.progressIndicator.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                Log.e("WebView Error", error?.description.toString())
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            // Refresh the WebView content
            binding.webView.reload()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initData() {
        binding.webView.visibility = View.GONE
        binding.progressIndicator.visibility = View.VISIBLE

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()

        binding.webView.loadUrl("https://www.beatfantasy.com")
    }

    private fun openInBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        browserIntent.setPackage("com.android.chrome")
        startActivity(browserIntent)
    }

    private fun appInstalledOrNot(uri: String): Boolean {
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return false
    }
}
