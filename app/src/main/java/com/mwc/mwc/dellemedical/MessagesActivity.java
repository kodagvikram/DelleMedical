package com.mwc.mwc.dellemedical;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mwc.mwc.dellemedical.VO.AppSingletonClass;

public class MessagesActivity extends AppCompatActivity {
    Button myhistory,notification,message,lablenoti,logout,sendmessage;
    Button backbtn;
    AppSingletonClass appSingletonClass;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        try {

            Typeface font = Typeface.createFromAsset(getAssets(),
                    "fontawesome-webfont.ttf");

            appSingletonClass=AppSingletonClass.getinstance(this);

            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());

            TextView username=(TextView)findViewById(R.id.USERNAMETEXTVIEW);
            username.setText(sharedPreferences.getString("name", ""));

            RelativeLayout logoimagelayout=(RelativeLayout)findViewById(R.id.LogoImageLayout);
            SetHeightWidth(logoimagelayout, 0.37, 0.25);


            RelativeLayout lablelayout=(RelativeLayout)findViewById(R.id.LABLELAYOUT);
            SetHeightWidth(lablelayout, 0.63, 0.08);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.textfieldbg);


            myhistory = (Button) findViewById(R.id.USERIDEDITTEXT);
            Bitmap bkg = (LoginActivity.getRoundedCornerBitmap(bitmap, 20));
            BitmapDrawable bkgbt = new BitmapDrawable(getResources(),bkg);
            myhistory.setBackgroundDrawable((Drawable) bkgbt);
            SetHeightWidth(myhistory, 0.58, 0.08);

            RelativeLayout middlelayout=(RelativeLayout)findViewById(R.id.MIDDLELAYOUT);
            SetHeightWidth(middlelayout, 0.95, 0.46);

            EditText messageedittext=(EditText)findViewById(R.id.MESSAGEEDITTEXT);

            messageedittext.setPadding((int)(appSingletonClass.devicewidth*0.035),(int)(appSingletonClass.deviceheight*0.015),(int)(appSingletonClass.devicewidth*0.035),(int)(appSingletonClass.deviceheight*0.015));

            backbtn=(Button) findViewById(R.id.BACKBTN);
            SetHeightWidth(backbtn, 0.25, 0.07);

            RelativeLayout sendlayout=(RelativeLayout)findViewById(R.id.SENDLAYOUT);
            SetHeightWidth(sendlayout, 1, 0.08);


//            sendmessage=(Button) findViewById(R.id.SENDMESSAGEBTN);
//            SetHeightWidth(sendmessage, 0.25, 0.08);

            setMargins(lablelayout, 0, 0.04, 0.015, 0);
            setMargins(myhistory, 0.02, 0.03, 0.043, 0);
            setMargins(middlelayout, 0.01, 0, 0.01, 0);
            setMargins(sendlayout, 0.02, 0.026, 0.02, 0);
            setMargins(backbtn, 0.02, 0.02, 0.01, 0.02);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }//end of Oncreate

    public  void SetHeightWidth(View view,double width,double height)
    {

        try {

            if(width==0)
            {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height = (int)((appSingletonClass.deviceheight)*height);
            }
            else  if(height==0)
            {
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = (int)((appSingletonClass.deviceheight)*height);
            }
            else {

                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = (int) ((appSingletonClass.devicewidth) * width);
                params.height = (int) ((appSingletonClass.deviceheight) * height);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();;
        }


    }//end of OnCreate

    public void setMargins(View v, double l, double t, double r, double b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
                    .getLayoutParams();

            final float scale = this.getResources().getDisplayMetrics().density;
            int lpx = (int) ((appSingletonClass.deviceheight) * l);
            int tpx = (int) ((appSingletonClass.deviceheight) * t);
            int rpx = (int) ((appSingletonClass.devicewidth) * r);
            int bpx = (int) ((appSingletonClass.deviceheight) * b);

            p.setMargins(lpx, tpx, rpx, bpx);
            v.requestLayout();
        }
    }

}//end of Activity

