package smktelkom_mlg.sch.id.mywallet.Promo_screen.Blanja_screen;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import smktelkom_mlg.sch.id.mywallet.R;

public class BlanjaActivity extends AppCompatActivity {

    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blanja);

        webview = (WebView)findViewById(R.id.blanja);
        WebSettings webSetting = webview.getSettings();
        webview.setWebViewClient(new WebViewClient());


        //define your website url here which you want to open in webview.
        webview.loadUrl("https://www.blanja.com");
    }

}