package com.mwc.mwc.dellemedical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mwc.mwc.dellemedical.VO.AppUtils;

import java.net.URLEncoder;

public class OpenPdfActivity extends AppCompatActivity {
    public WebView mywebView;
    String url="",titletext="";
    public ProgressBar myProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pdf);
        mywebView=(WebView)findViewById(R.id.OPENPDFWEBVIEW);
        myProgressBar=(ProgressBar)findViewById(R.id.WebviewprogressBar);

            if(!AppUtils.isNetworkAvailable(OpenPdfActivity.this))
            {
                AppUtils.ShowAlertDialog(OpenPdfActivity.this,"No internet connection");
            }

            try {

                        url="http://mobiwebsoft.com/DELLE/pdf_doc/pdf_demo.pdf";
                        String urlEncoded = URLEncoder.encode(url, "UTF-8");
                        url = "http://docs.google.com/viewer?url=" + urlEncoded;

                WebSettings webSettings = mywebView.getSettings();
                webSettings.setJavaScriptEnabled(true);

                mywebView.loadUrl(url);
                mywebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });


                mywebView.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        //LenderTVLoadUrl.this.setTitle("Loading...");
                        OpenPdfActivity.this.setProgress(progress * 100);
                        myProgressBar.setVisibility(View.VISIBLE);


                        if (progress == 100) {
                            myProgressBar.setVisibility(View.INVISIBLE);

                            //  LenderTVLoadUrl.this.setTitle(R.string.app_name);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//extends of Oncreate
}//end of Activity
