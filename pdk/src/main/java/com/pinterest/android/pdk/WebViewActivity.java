package com.pinterest.android.pdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class WebViewActivity extends AppCompatActivity {

    public WebView wbvWebView;
    public WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        wbvWebView = (WebView) findViewById(R.id.wbvWebView);

        webSettings = wbvWebView.getSettings();

        setWebView(getIntent().getStringExtra("url"));

    }

    private void setWebView(String url) {
//        url = "http://www.google.com";
        this.webSettings.setJavaScriptEnabled(true);
        this.webSettings.setBuiltInZoomControls(false);
        this.webSettings.setDisplayZoomControls(false);
        this.webSettings.setDomStorageEnabled(true);
        this.webSettings.setSaveFormData(false);
        this.webSettings.setAllowFileAccess(true);
        this.webSettings.setSupportZoom(false);
        this.webSettings.setUserAgentString("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:44.0) Gecko/20100101 Firefox/44.0");
        this.webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        this.wbvWebView.setVerticalScrollBarEnabled(false);
        this.wbvWebView.setHorizontalScrollBarEnabled(false);
        this.wbvWebView.setBackgroundColor(0x00000000);
        this.wbvWebView.setLayerType(wbvWebView.LAYER_TYPE_SOFTWARE, null);

        this.wbvWebView.loadUrl(url);

        this.wbvWebView.setWebViewClient(new WebViewClient());
        this.wbvWebView.setWebChromeClient(new WebChromeClient());
    }
}
