package com.mwc.mwc.dellemedical;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mwc.mwc.dellemedical.VO.AppSingletonClass;

public class HomeScreenActivity extends AppCompatActivity {

    Button myhistory,notification,message,lablenoti,logout;
    Button lockbtn;
    ToggleButton toggleButton;

    AppSingletonClass appSingletonClass;

    SharedPreferences sharedPreferences;

    public DisplayMetrics metrics;

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        try {
            metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            Typeface font = Typeface.createFromAsset(getAssets(),
                    "fontawesome-webfont.ttf");

            appSingletonClass=AppSingletonClass.getinstance(this);
            appSingletonClass.devicewidth=(int) (metrics.widthPixels);
            appSingletonClass.deviceheight=(int) (metrics.heightPixels);

            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());

            TextView username=(TextView)findViewById(R.id.USERNAMETEXTVIEW);
            username.setText(sharedPreferences.getString("name", ""));

            lockbtn=(Button)findViewById(R.id.LOCKBTN);
            lockbtn.setTypeface(font);
             SetHeightWidth(lockbtn, 0.08, 0.08);
            lockbtn.setTextSize((float) (appSingletonClass.devicewidth * 0.05));

            RelativeLayout logoimagelayout=(RelativeLayout)findViewById(R.id.LogoImageLayout);
            SetHeightWidth(logoimagelayout, 0.37, 0.25);


            RelativeLayout lablelayout=(RelativeLayout)findViewById(R.id.LABLELAYOUT);
            SetHeightWidth(lablelayout, 0, 0.10);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.textfieldbg);


            myhistory = (Button) findViewById(R.id.USERIDEDITTEXT);
            Bitmap bkg = (getRoundedCornerBitmap(bitmap, 20));
            BitmapDrawable bkgbt = new BitmapDrawable(getResources(),bkg);
            myhistory.setBackgroundDrawable((Drawable) bkgbt);
            SetHeightWidth(myhistory, 0.89, 0.08);

            ImageView imageView = (ImageView) findViewById(R.id.USERIDEDITTEXT2);
            SetHeightWidth(imageView, 0.89, 0.08);
            imageView.setBackgroundDrawable((Drawable) bkgbt);



            notification = (Button) findViewById(R.id.PASSWORDEDITTEXT);
            notification.setBackgroundDrawable((Drawable) bkgbt);
            SetHeightWidth(notification, 0, 0.08);

            message = (Button) findViewById(R.id.MESSAGEBTN);
            message.setBackgroundDrawable((Drawable) bkgbt);
            SetHeightWidth(message, 0, 0.08);

            lablenoti = (Button) findViewById(R.id.NOTIFICATIONLABLE);
            lablenoti.setBackgroundDrawable((Drawable) bkgbt);
            SetHeightWidth(lablenoti, 0.74, 0.08);

            toggleButton = (ToggleButton) findViewById(R.id.TPGGLEBTN);
            SetHeightWidth(toggleButton, 0.22, 0.06);

            logout = (Button) findViewById(R.id.LOGOUTBTN);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.logout);
            Bitmap bkg2 = (getRoundedCornerBitmap(bitmap2, 20));
            BitmapDrawable bkgbt2 = new BitmapDrawable(getResources(),bkg2);
            logout.setBackgroundDrawable((Drawable) bkgbt2);
            SetHeightWidth(logout, 0.40, 0.08);


            setMargins(myhistory, 0.005, 0.03, 0.000, 0);
            setMargins(imageView, 0.005, 0.03, 0.000, 0);
            setMargins(lockbtn, 0.000, 0.03, 0.017, 0);
            setMargins(notification, 0.005, 0.03, 0.005, 0);
            setMargins(message, 0.005, 0.04, 0.005, 0);
            setMargins(lablenoti, 0.005, 0.03, 0.005, 0);
            setMargins(toggleButton, 0, 0.04, 0.017, 0);
            setMargins(logout, 0.005, 0.03, 0.03, 0);


            myhistory.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MymedicalHistoryActivity.class);
                    startActivity(intent);
                }
            });

            notification.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                    startActivity(intent);
                }
            });

            message.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                    startActivity(intent);
                }
            });

            logout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e)
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
            int tpx = (int) ((metrics.heightPixels) * t);
            int rpx = (int) ((appSingletonClass.devicewidth) * r);
            int bpx = (int) ((appSingletonClass.deviceheight) * b);

            p.setMargins(lpx, tpx, rpx, bpx);
            v.requestLayout();
        }
    }

    public  Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}//end of Activity
