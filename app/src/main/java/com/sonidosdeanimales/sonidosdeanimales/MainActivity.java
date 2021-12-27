package com.sonidosdeanimales.sonidosdeanimales;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.sonidosdeanimales.sonidosdeanimales.Animal;
import com.sonidosdeanimales.sonidosdeanimales.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ListView lvAnimales;
    List<Animal> listaAnimales;
    Intent intent;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        anuncio();
        pantallaCompleta();
        initializeData();
        lvAnimales = (ListView) findViewById(R.id.cv);
        lvAnimales.setAdapter(new AdaptadorAnimal(getApplicationContext(), R.layout.activity_adaptador_animal, (ArrayList<Animal>) listaAnimales));

        lvAnimales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), MostrarAnimal.class);
                Animal animal = listaAnimales.get(position);
                intent.putExtra("animal", animal);
                startActivity(intent);
            }
        });
    }

    private void anuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private void initializeData() {
        listaAnimales = new ArrayList<>();
        AssetManager am = getApplicationContext().getAssets();
        InputStream is = null;
        String nombreEnEspañol;

        try {
            is = am.open("nombres.txt");
            InputStreamReader osw = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(osw);
            while (br.ready()) {
                nombreEnEspañol = br.readLine();
                listaAnimales.add(new Animal(nombreEnEspañol, getResources().getIdentifier(nombreEnEspañol.toLowerCase(Locale.ROOT), "drawable", this.getPackageName())));
            }
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!Locale.getDefault().getLanguage().equals("es")) {

            //otros idiomas
            InputStream elOtroIdioma = null;
            try {
                elOtroIdioma = am.open("nombres" + Locale.getDefault().getLanguage() + ".txt");
            } catch (IOException e) {
                try {
                    elOtroIdioma = am.open("nombresen.txt");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            InputStreamReader osw = new InputStreamReader(elOtroIdioma);
            BufferedReader br = new BufferedReader(osw);
            for (Animal a : listaAnimales) {
                try {
                    a.setNombre(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(listaAnimales);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        anuncio();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pantallaCompleta();
    }

    private void pantallaCompleta() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; //View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.actRate) {
            rateApp();
        }
        /*if (id == R.id.actShare) {
            Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
        }*/
        return true;
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        intent.addFlags(flags);
        return intent;
    }
}
