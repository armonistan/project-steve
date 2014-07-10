package com.steve;

import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AndroidApplication implements IActivityRequestHandler{
	AdView ads;
	AdRequest demographics;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
        View gameView = initializeForView(new SteveDriver(this), cfg);
        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView);
        
        ads = new AdView(this); // Put in your secret key here
        ads.setAdUnitId("ca-app-pub-4539283306007418/2063573887");
        ads.setAdSize(AdSize.SMART_BANNER);
        ads.setVisibility(View.GONE);
        
        RelativeLayout.LayoutParams adParams = 
        		new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(ads, adParams);
        
        setContentView(layout);

        demographics = new AdRequest.Builder().addTestDevice(Settings.Secure.ANDROID_ID).build();
        ads.loadAd(demographics);
    }
    
	private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    
    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                    ads.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS:
                {
                    ads.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}