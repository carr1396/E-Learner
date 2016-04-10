package edu.craw.e_learner.gui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by farid on 3/18/2016.
 */
public class Toaster extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    private TextView tv;
    private View v;
    public Toaster(Context context) {
        super(context);
        v = this.getView();
        tv = (TextView) v.findViewById(android.R.id.message);
    }
    @DrawableRes
    public Toaster setBackground(int resid){
        v.setBackgroundResource(resid);
        return this;
    }
    public Toaster setBackgroundColor(int color){
        v.setBackgroundColor(color);
        return this;
    }
    public Toaster setTypeface(Typeface tf){
        tv.setTypeface(tf); return this;
    }

}
