package com.example.addalmacenamiento;

class Contacto implements Comparable{
    private long id;
    private String nombre;
    private String telefonos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    @Override
    public String toString() {
        return nombre +','+ telefonos+
                "\n";
    }

    @Override
    public int compareTo(Object c) {
        return (this.nombre.compareToIgnoreCase(((Contacto)c).getNombre()));
    }
}
