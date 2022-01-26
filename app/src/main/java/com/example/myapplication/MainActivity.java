package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    private final static String TAG = "AdMob";
    Button btn1, btn2, btn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity-Started");

        btn1 = findViewById(R.id.btn_1);
        btn2 = findViewById(R.id.btn_2);
        btn3 = findViewById(R.id.btn_3);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button-1 Clicked");

                if (mInterstitialAd != null) {
                    save_id(btn1.getId());
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                    Intent intent = new Intent(MainActivity.this, Activity1.class);
                    startActivity(intent);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button-2 Clicked");
                if (mInterstitialAd != null) {
                    save_id(btn2.getId());
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                    Intent intent = new Intent(MainActivity.this, Activity2.class);
                    startActivity(intent);
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button-3 Clicked");
                if (mInterstitialAd != null) {
                    save_id(btn3.getId());
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                    Intent intent = new Intent(MainActivity.this, Activity3.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void save_id(int id) {
        SharedPreferences preferences = getSharedPreferences("SAVING", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("mID",id);
        editor.apply();
    }

    private int load_id(){
        SharedPreferences preferences = getSharedPreferences("SAVING", MODE_PRIVATE);
        return preferences.getInt("mID",0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(MainActivity.this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d(TAG, "The ad was dismissed.");

                                Intent intent;
                                switch (load_id()){
                                    case R.id.btn_1:
                                        intent = new Intent(MainActivity.this, Activity1.class);
                                        break;
                                    case R.id.btn_2:
                                        intent = new Intent(MainActivity.this, Activity2.class);
                                        break;
                                    case R.id.btn_3:
                                        intent = new Intent(MainActivity.this, Activity3.class);
                                        break;
                                    default:
                                        return;
                                }
                                startActivity(intent);
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d(TAG, "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d(TAG, "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
            }
        });

    }
}