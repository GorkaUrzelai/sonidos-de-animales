package com.sonidosdeanimales.sonidosdeanimales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorAnimal extends BaseAdapter {

    private final Context context;
    private ArrayList<Animal> listaAnimales;

    public AdaptadorAnimal(Context context, int layout, ArrayList<Animal> listaAnimales) {
        this.context = context;
        this.listaAnimales = listaAnimales;
    }

    @Override
    public int getCount() {
        return this.listaAnimales.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listaAnimales.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        v = layoutInflater.inflate(R.layout.activity_adaptador_animal, null);

        String nombre;
        if ((nombre = listaAnimales.get(position).getNombre()) == null) {
            nombre = listaAnimales.get(position).getNombreEnEspañol();
        }
        ImageView imagenview = (ImageView) v.findViewById(R.id.foto);
        imagenview.setImageResource(listaAnimales.get(position).getIdImagen());
        TextView tvNombre = (TextView) v.findViewById(R.id.nombre);
        tvNombre.setText(nombre);
        return v;
    }
}