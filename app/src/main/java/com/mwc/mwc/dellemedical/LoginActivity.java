package com.mwc.mwc.dellemedical;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gcm.GCMRegistrar;
import com.mwc.mwc.dellemedical.VO.AppSingletonClass;
import com.mwc.mwc.dellemedical.VO.AppUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static DisplayMetrics metrics;
//    int devicewidth = 0;
//    int deviceheight = 0;

    //************************************
    Controller aController;
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    public static String GCMRegister_Id="";
    //************************************

    public EditText Userid,Password;

    AppSingletonClass appSingletonClass;
    SharedPreferences sharedPreferences;
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS1 = 1;
    String responseString = "";
    String responseText = "", status = "";

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS1:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Processing request, Please wait ...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                return mProgressDialog;

            default:
                return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {

            metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            appSingletonClass=AppSingletonClass.getinstance(this);
            appSingletonClass.devicewidth=(int) (metrics.widthPixels);
            appSingletonClass.deviceheight=(int) (metrics.heightPixels);

            try {

                if (!sharedPreferences.getString("userid", "").equals("")) {
                    Intent intent = new Intent(getApplicationContext(),
                            HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                }

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }


            RelativeLayout logoimagelayout=(RelativeLayout)findViewById(R.id.LogoImageLayout);

            ViewGroup.LayoutParams params = logoimagelayout.getLayoutParams();
            params.width = (int)( appSingletonClass.devicewidth*1);
            params.height = (int)( appSingletonClass.deviceheight*0.60);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.textfieldbg);


            Userid = (EditText) findViewById(R.id.USERIDEDITTEXT);
            Bitmap bkg = (getRoundedCornerBitmap(bitmap, 20));
            BitmapDrawable bkgbt = new BitmapDrawable(getResources(),bkg);
            Userid.setBackgroundDrawable((Drawable) bkgbt);

            ViewGroup.LayoutParams params2 = Userid.getLayoutParams();
            params2.height = (int)( appSingletonClass.deviceheight*0.10);

            Password = (EditText) findViewById(R.id.PASSWORDEDITTEXT);
            Password.setBackgroundDrawable((Drawable) bkgbt);

            ViewGroup.LayoutParams params3 = Password.getLayoutParams();
            params3.height = (int)( appSingletonClass.deviceheight*0.10);

            Userid.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                            //Password.setFocusable(true);

                        return true;
                    }
                    return false;
                }
            });
            Password.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

//                            Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
//                            startActivity(intent);
                        if(AppUtils.isNetworkAvailable(LoginActivity.this)) {
                            if (GCMRegister_Id.equalsIgnoreCase("")) {
                                try {

                                    AppUtils.ShowAlertDialog(LoginActivity.this,
                                            "Please wait your device is registring with GCM server.\nTry after some time.");
                                    getGSMRegister_Id();

                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                             else  if (emptyCheck(Userid) && emptyCheck(Password))
                                    new CallLoginService().execute();
                                else
                                    AppUtils.ShowAlertDialog(LoginActivity.this, "All fields are mandatory");
                            }
                        else
                        AppUtils.ShowInternetDialog(LoginActivity.this);

                        return true;
                    }
                    return false;
                }
            });

            SetHeightWidth(Password, 0, 0.08);
            SetHeightWidth(Userid, 0, 0.08);


            setMargins(Userid, 0.005, 0.03, 0.005, 0);
            setMargins(Password, 0.005, 0.03, 0.005, 0);

            if(GCMRegister_Id.equalsIgnoreCase(""))
            {
                try {

                    getGSMRegister_Id();

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }//end of Oncreate

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
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

    // JSON AsyncTask for saveUserDetails upload
    class CallLoginService extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(DIALOG_DOWNLOAD_PROGRESS1);
        }

        @Override
        protected Void doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("username",
                    Userid.getText().toString()));
            nameValuePair.add(new BasicNameValuePair("password",
                    Password.getText().toString()));

            nameValuePair.add(new BasicNameValuePair("device_id",
                    GCMRegister_Id));
            nameValuePair.add(new BasicNameValuePair("device_type",
                   "android"));

            try {
                // Defined URL where to send data
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://mobiwebsoft.com/DELLE/loginuserJson.php?");
                httppost.setHeader("Content-Type",
                        "application/x-www-form-urlencoded;");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePair,
                        "UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                responseString = EntityUtils.toString(response.getEntity());
                System.out.println(responseString + "response is display");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
            JSONObject jresponse = null;
            try {

                if(responseString.equalsIgnoreCase("no"))
                {
                    AppUtils.ShowAlertDialog(LoginActivity.this,"Invalid User Name or Password");

                }
                else {

                    jresponse = new JSONObject(responseString);
                    //String staus=jresponse.getString("user");
                    JSONObject activityArray2 = null;
                    activityArray2 = jresponse.getJSONObject("userdetail");
                    JSONObject activityArray = null;
                    activityArray = activityArray2.getJSONObject("user");
//
//                        for (int i = 0; i < activityArray.length(); i++) {
//
//                            JSONObject activityObject = (JSONObject) activityArray.get(i);
//
////                        if (!activityObject.isNull("id"))
////                            cVo.userid = activityObject.getString("id");
//
//                        }// end of for

                    String id="";

//                    sharedPreferences = PreferenceManager
//                            .getDefaultSharedPreferences(getApplicationContext());

                    SharedPreferences.Editor editor = sharedPreferences
                            .edit();

                    if (!activityArray.isNull("id")) {
                        editor.putString("userid", activityArray.getString("id"));
                    }

                    if (!activityArray.isNull("surname")) {
                        editor.putString("surname", activityArray.getString("surname"));
                    }

                    if (!activityArray.isNull("name")) {
                        editor.putString("name", activityArray.getString("name"));
                    }
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                           startActivity(intent);
                    finish();

                }//end of else
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (mProgressDialog != null)
                    mProgressDialog.dismiss();
                AppUtils.ShowAlertDialog(LoginActivity.this,"Server Error");
                e.printStackTrace();
            }
        }// end of onpost()
    }// ends of Async task

    public boolean emptyCheck(EditText editText) {
        if (editText.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public void getGSMRegister_Id()
    {
        try{
            //Get Global Controller Class object (see application tag in AndroidManifest.xml)
            aController = (Controller) getApplicationContext();


            // Check if Internet present
            if (!aController.isConnectingToInternet()) {

                // Internet Connection is not present
                aController.showAlertDialog(LoginActivity.this,
                        "Internet Connection Error",
                        "Please connect to Internet connection", false);
                // stop executing code by return
                return;
            }

            // Make sure the device has the proper dependencies.
            GCMRegistrar.checkDevice(this);

            GCMRegistrar.checkManifest(this);

            registerReceiver(mHandleMessageReceiver, new IntentFilter(
                    GCMConfig.DISPLAY_MESSAGE_ACTION));

            // Get GCM registration id
            final String regId = GCMRegistrar.getRegistrationId(this);

            GCMRegister_Id=regId;
            // Check if regid already presents
            if (regId.equals("")) {

                // Register with GCM
                GCMRegistrar.register(this, GCMConfig.GOOGLE_SENDER_ID);

            } else {

                // Device is already registered on GCM Server
                if (GCMRegistrar.isRegisteredOnServer(this)) {


                } else {


                    final Context context = this;
                    mRegisterTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {

                            // Register on our server
                            // On server creates a new user
                            aController.register(context, "", "", regId);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            mRegisterTask = null;
                        }
                    };

                    // execute AsyncTask
                    mRegisterTask.execute(null, null, null);
                }
            }//end of else
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }//end of GSM register id ******************************


    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String newMessage = intent.getExtras().getString(GCMConfig.EXTRA_MESSAGE);

                aController.acquireWakeLock(getApplicationContext());

                sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());


                // Releasing wake lock
                //aController.releaseWakeLock();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    };

}//end of Activity
