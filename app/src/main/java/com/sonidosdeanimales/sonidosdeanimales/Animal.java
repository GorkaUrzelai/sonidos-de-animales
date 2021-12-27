package com.sonidosdeanimales.sonidosdeanimales;

import java.io.Serializable;

public class Animal implements Serializable, Comparable {

    String nombre, nombreEnEspañol;
    int idImagen;

    public Animal(String nombreEnEspañol, int idImagen) {
        this.nombreEnEspañol = nombreEnEspañol;
        this.idImagen = idImagen;
    }


    public int getIdImagen() {
        return idImagen;
    }

    public String getNombreEnEspañol() {
        return nombreEnEspañol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int compareTo(Object o) {
        return nombre.compareTo(((Animal) o).nombre);
    }
}


