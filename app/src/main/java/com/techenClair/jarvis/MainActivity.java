package com.techenClair.jarvis;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private static final String JARVIS_URL = "http://localhost:8000";
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progressBar);

        // Request audio permission for mic
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        }

        setupWebView();
        webView.loadUrl(JARVIS_URL);
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setUserAgentString("JarvisAndroid/1.0 " + settings.getUserAgentString());

        webView.setBackgroundColor(Color.parseColor("#0a0a0f"));

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                // Grant mic access to WebView
                request.grant(request.getResources());
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(android.webkit.WebResourceRequest request,
                                        android.webkit.WebResourceError error) {
                // Show offline page if Jarvis not running
                webView.loadData(getOfflinePage(), "text/html", "UTF-8");
            }
        });
    }

    private String getOfflinePage() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
            "<style>body{background:#0a0a0f;color:#fff;font-family:sans-serif;" +
            "display:flex;flex-direction:column;align-items:center;justify-content:center;" +
            "height:100vh;margin:0;text-align:center;}" +
            "h1{font-size:2em;color:#6c63ff;}p{color:#aaa;}" +
            ".retry{margin-top:20px;padding:12px 28px;background:#6c63ff;color:#fff;" +
            "border:none;border-radius:24px;font-size:1em;cursor:pointer;}" +
            "</style></head><body>" +
            "<h1>⚡ Jarvis</h1>" +
            "<p>Jarvis n'est pas encore démarré sur le PC.</p>" +
            "<p>Lancez <b>Jarvis.exe</b> puis réessayez.</p>" +
            "<button class='retry' onclick='window.location.reload()'>🔄 Réessayer</button>" +
            "</body></html>";
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
