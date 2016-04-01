package com.mwc.mwc.dellemedical.VO;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by mwc on 2/5/16.
 */
public class AppSingletonClass {

    public Context context;
    public static AppSingletonClass app_Singleton=null;
    public int SelectedTab=0;
    public String SeletedActivity="";

    public DisplayMetrics metrics;
    public int devicewidth = 0;
    public int deviceheight = 0;

    public AppSingletonClass(Context context)
    {
        this.context=context;

    }//end of constructure

    public static AppSingletonClass getinstance(Context context)
    {
        if(app_Singleton==null)
        {
            app_Singleton=new AppSingletonClass(context);
        }

            return app_Singleton;

    }//end of instance


}//end of class
