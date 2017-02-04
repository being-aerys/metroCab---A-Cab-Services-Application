package com.example.shadowstorm.metrocabv2.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.shadowstorm.metrocabv2.LogInActivity;
import com.example.shadowstorm.metrocabv2.R;
import com.example.shadowstorm.metrocabv2.SaveSharedPreference;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Shadow Storm on 8/19/2016.
 */


public class RadialButtonLayout extends FrameLayout {
    String salutation1,fname1,lname1,phone1,actype1,username1,balance1;
    Context ctx = getContext();

    private final static long DURATION_SHORT = 400;
    private WeakReference<Context> weakContext;

    @InjectView(R.id.btn_main)
    View btnMain;
    @InjectView(R.id.btn_green)
    View btnGreen;
    @InjectView(R.id.btn_blue)
    View btnBlue;
    @InjectView(R.id.btn_indigo)
    View btnIndigo;

    private boolean isOpen = false;
    private Toast toast;
    /**
     * Default constructor
     * @param context
     */
    public RadialButtonLayout(final Context context) {
        this(context, null);
    }

    public RadialButtonLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadialButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        weakContext = new WeakReference<Context>( context );
        LayoutInflater.from(context).inflate( R.layout.layout_radial_buttons, this);
        if (isInEditMode()) {
            //
        } else{
            ButterKnife.inject(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            //
        } else {

        }
    }

    private void showToast( final int resId ) {
        if ( toast != null )
            toast.cancel();
        toast = Toast.makeText( getContext(), resId, Toast.LENGTH_SHORT );
       // toast.show();
    }

    @OnClick( R.id.btn_main )
    public void onMainButtonClicked( final View btn ) {
        int resId = 0;
        if ( isOpen ) {
            // close
            hide(btnGreen);
            hide(btnBlue);
            hide(btnIndigo);
            isOpen = false;
            resId = R.string.close;
        } else {
            show(btnGreen, 3, 200);
            show(btnBlue, 4, 200);
            show(btnIndigo, 5, 200);
            isOpen = true;
            resId = R.string.open;
        }
        showToast( resId);
        btn.playSoundEffect( SoundEffectConstants.CLICK);
    }

    @OnClick({  R.id.btn_green, R.id.btn_blue, R.id.btn_indigo})
    public void onButtonClicked( final View btn ) {
        int resId = 0;
        switch ( btn.getId() ) {
            case R.id.btn_green:
                resId = R.string.green;
                //==================================================DISPLAY PROFILE
              /*  Log.e("QQQQQ","ll");
                User_database userDb;
                userDb= new User_database(ctx);
                Cursor res = userDb.getUserData();
                if (res.getCount() > 0) {
                    Log.e("QQQQQ2","ll");
                    while (res.moveToNext()) {

                        salutation1= res.getString(1);
                        fname1 = res.getString(2);
                        lname1 = res.getString(3);
                        phone1 = res.getString(5);
                        actype1 = res.getString(6);
                        username1 = res.getString(9);
                        balance1 = res.getString(14);
                        //======================================
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("QQQQQ3",salutation1);
                                AlertDialog.Builder myAlert = new AlertDialog.Builder(ctx);
                                myAlert.setMessage(salutation1+" "+fname1+" "+lname1+"\nPhone: "+phone1+"\nAcc Type: "+actype1+"\nusername: "+username1+"\nBalance: "+balance1)
                                        .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("My Profile")
                                        .setIcon(R.drawable.myridec)
                                        .create();
                                myAlert.show();
                            }
                        });

                    }
                }
                */
                Intent inte = new Intent(ctx,ViewProfile.class);
                ctx.startActivity(inte);




                //==================================================

                break;
            case R.id.btn_blue:
                resId = R.string.blue;
                Intent inten = new Intent(ctx,ViewBalance.class);
                ctx.startActivity(inten);

                break;
            case R.id.btn_indigo:
                resId = R.string.indigo;
                //===================================clear login======================
                SaveSharedPreference.clearUserName(ctx);

                //==pahile system.exit le kaam garethyo aile garena ra intent nai haldiye


                //System.exit(0);
                //=======================================INSTEAD OF INTENT WE JUST EXIT USING exit(0) as above=======
                 Intent intent = new Intent(ctx, LogInActivity.class);
                ctx.startActivity(intent);
                //===============================================
                break;
            default:
                resId = R.string.undefined;
        }
        showToast(resId);
        btn.playSoundEffect( SoundEffectConstants.CLICK);
    }

    private final void hide( final View child) {
        child.animate()
                .setDuration(DURATION_SHORT)
                .translationX(0)
                .translationY(0)
                .start();
    }

    private final void show(final View child, final int position, final int radius) {
        float angleDeg = 180.f;
        int dist = radius;
        switch (position) {
            case 1:
                angleDeg += 0.f;
                break;
            case 2:
                angleDeg += 45.f;
                break;
            case 3:
                angleDeg += 90.f;
                break;
            case 4:
                angleDeg += 135.f;
                break;
            case 5:
                angleDeg += 180.f;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                break;
        }

        final float angleRad = (float) (angleDeg * Math.PI / 180.f);

        final Float x = dist * (float) Math.cos(angleRad);
        final Float y = dist * (float) Math.sin(angleRad);
        child.animate()
                .setDuration(DURATION_SHORT)
                .translationX(x)
                .translationY(y)
                .start();
    }
}

