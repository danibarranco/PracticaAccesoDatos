package com.example.addalmacenamiento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ActivityContactos extends AppCompatActivity {
    private TextView tvContactos;
    private  String lugarAlm;
    private String nom;
    private String contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        try {
            initComponents();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() throws IOException {
        tvContactos= findViewById(R.id.tvContactos);
        lugarAlm=getIntent().getStringExtra("lugar");
        nom=getIntent().getStringExtra("nombre");
        if (lugarAlm.equalsIgnoreCase(getString(R.string.interno))){
            internalRead();
        }else {
            privateRead();
        }

    }

    private void privateRead() throws IOException {
            String nombre = nom;
            File f = new File(getExternalFilesDir(null), nombre);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            contacts = "";
            while ((linea = br.readLine()) != null) {
                contacts += linea.substring(0,linea.length()-1) + "\n";
            }
            br.close();
            putStringTV();
    }

    private void putStringTV() {
        tvContactos.setText(contacts);
    }

    private void internalRead() throws IOException {
        String nombre=nom;
        File f = new File(getFilesDir(), nombre);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String linea;
        contacts="";
        while ((linea = br.readLine()) != null) {
            contacts+=linea.substring(0,linea.length()-1)+"\n";
       }
        br.close();
        putStringTV();
    }
}
