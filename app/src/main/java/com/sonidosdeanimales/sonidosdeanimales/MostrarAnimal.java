package com.sonidosdeanimales.sonidosdeanimales;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;

public class MostrarAnimal extends AppCompatActivity {

    ImageView imageView;
    Animal animal;
    MediaPlayer mp;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_animal);
        anuncio();
        imageView = findViewById(R.id.ivAnimal);
        animal = (Animal) getIntent().getExtras().getSerializable("animal");
        sonido();
        imageView = findViewById(R.id.ivAnimal);
        imageView.setImageResource(animal.getIdImagen());
        imageView.setClickable(true);
        imageView.setOnClickListener(view -> pantallaCompleta());

    }

    private void anuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewMostrar);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        /*mAdView = findViewById(R.id.adViewMostrarTop);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/
    }

    private void sonido() {
        int id = this.getResources().getIdentifier(animal.getNombreEnEspañol().toLowerCase(Locale.ROOT), "raw", this.getPackageName());

        mp = MediaPlayer.create(getApplicationContext(), id);
        mp.setLooping(true);
        mp.start();
    }

    private void pantallaCompleta() {
        getSupportActionBar().hide();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pantallaCompleta();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sonido();
    }
}