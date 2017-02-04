package com.example.shadowstorm.metrocabv2.views;

import android.app.Activity;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.shadowstorm.metrocabv2.GlobalValues;
import com.example.shadowstorm.metrocabv2.R;
import com.example.shadowstorm.metrocabv2.User_database;

/**
 * Created by Dipesh on 21/08/16.
 */
public class ViewProfile extends Activity {
    private WebView myWebView;
    User_database userDb;
    String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDb =new User_database(this);
        setContentView(R.layout.view_profile);

        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                userPhone = ress.getString(5);
            }
        }


        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            String url = GlobalValues.MyAppUrl+"/metrocab/viewProfile.php?userPhone="+userPhone;
            myWebView = (WebView)findViewById(R.id.webViewProfile);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new WebViewClient(){
                public void onPageFinished(WebView view, String url) {
                }
            });
            myWebView.loadUrl(url);
        }else{
            Toast.makeText(ViewProfile.this, "NO Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }

}

