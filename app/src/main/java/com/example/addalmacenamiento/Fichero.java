package com.example.addalmacenamiento;

import androidx.annotation.NonNull;

public class Fichero {
    private String nombre;
    private String lugar;
    private String fecha;

    public Fichero(String nombre, String lugar, String fecha) {
        this.nombre = nombre;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre+" - "+lugar+" - "+fecha;
    }
}
