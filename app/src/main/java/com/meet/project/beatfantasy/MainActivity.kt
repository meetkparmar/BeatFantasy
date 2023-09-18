package com.meet.project.beatfantasy

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
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

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()

        binding.webView.loadUrl("https://www.beatfantasy.com")

        binding.backButton.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            }
        }

        binding.forwardButton.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null)
                    if (url.contains("dream11.com")) {
                        val intent = packageManager.getLaunchIntentForPackage("com.dream11.android")
                        if (intent != null) {
                            startActivity(intent)
                        } else {
                            openInBrowser(url)
                        }
                        return true
                    } else if (url.contains("t.me")) {
                        val isAppInstalled: Boolean = try {
                            packageManager.getPackageInfo("org.telegram.messenger", PackageManager.GET_ACTIVITIES)
                            true
                        } catch (e: PackageManager.NameNotFoundException) {
                            openInBrowser(url)
                            return false
                        }
                        if (isAppInstalled) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            intent.setPackage("org.telegram.messenger")
                            startActivity(intent)
                        }
                    } else if (url.contains("phonepe.com")) {
                        openInBrowser(url)
                    }
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (view?.canGoBack() == true) {
                    binding.topBar.visibility = View.VISIBLE
                } else {
                    binding.topBar.visibility = View.GONE
                }
                super.onPageFinished(view, url)
            }
        }
    }

    fun openInBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}
