package smktelkom_mlg.sch.id.mywallet.Promo_screen.Shopee_screen;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import smktelkom_mlg.sch.id.mywallet.R;

public class ShopeeActivity extends AppCompatActivity {

    WebView webview;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopee);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.logowallet);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setIcon(R.drawable.logowallet);

        webview = (WebView)findViewById(R.id.blanja);
        webview.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onResume() {
        super.onResume();

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setMediaPlaybackRequiresUserGesture(false);

        if (Build.VERSION.SDK_INT >= 19)
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        else
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressDialog.setProgress(newProgress);
            }
        });

        //define your website url here which you want to open in webview.
        webview.loadUrl("https://www.shopee.co.id");
        progressDialog.show();
    }
}