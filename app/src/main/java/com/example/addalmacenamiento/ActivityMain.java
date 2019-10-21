package com.example.addalmacenamiento;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ActivityMain extends AppCompatActivity {
    private static final int ID_PERMISO_LEER_CONTACTOS = 1;
    private ArrayList<Contacto> contactos;
    private String fichero;
    private String ext;
    private String contacts;
    private EditText nom;
    private boolean guardaHis;
    private boolean selecAlm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obtenerPermisos();
        initComponents();
        readSettings();
    }

    private void initComponents() {
        nom = findViewById(R.id.eTNom);
        initExport();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ajustes:
                showSettings();
                return true;
            case R.id.historial:
                showHistorial();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        Intent i = new Intent(this, ActivitySettings.class);
        startActivity(i);
    }

    public void showHistorial() {
        Intent intent = new Intent(this, ActivityHistorial.class);
        startActivity(intent);
    }

    public void initExport() {
        Button impor = findViewById(R.id.bImport);
        impor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    readSettings();
                    checkStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void obtenerPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                //explicacion
                explain(R.string.tituloExplicacion, R.string.mensajeExplicacion, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
        }
    }

    private void explain(int title, int message, final String[] permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.respSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(ActivityMain.this, permissions, ID_PERMISO_LEER_CONTACTOS);
            }
        });
        builder.setNegativeButton(R.string.respNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkStorage() throws IOException {
        ConstraintLayout l = findViewById(R.id.layout);
        if (!nom.getText().toString().equalsIgnoreCase("")) {
            if (!selecAlm) {
                internalWrite();
                return true;
            } else {
                privateWrite();
                return true;
            }
        } else
            Snackbar.make(l, R.string.checkNom, Snackbar.LENGTH_LONG).show();
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void privateWrite() throws IOException {
        accion();
        if (guardaHis) {
            guardarHistorial();
        }
        File f = new File(getExternalFilesDir(null), fichero);
        FileWriter fw = new FileWriter(f);
        fw.write(contacts);
        fw.flush();
        fw.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void guardarHistorial() {
        String alm;
        if (selecAlm) {
            alm = getString(R.string.priv);
        } else {
            alm = getString(R.string.interno);
        }
        LocalDate fecha = LocalDate.now();
        Fichero nuevo = new Fichero(fichero, alm, fecha.toString());
        String nuevoFichero;
        if(readPreferences()!=null){
            nuevoFichero = readPreferences() + "&" + nuevo.toString();
        }else {
            nuevoFichero =nuevo.toString();
        }
        savePreferences(nuevoFichero);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void internalWrite() throws IOException {
        accion();
        if (guardaHis) {
            guardarHistorial();
        }
        File f = new File(getFilesDir(), fichero);
        FileWriter fw = new FileWriter(f);
        fw.write(contacts);
        fw.flush();
        fw.close();
    }

    private String readPreferences() {
        SharedPreferences pc = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return pc.getString(getString(R.string.sharedKey), null);
    }

    private void savePreferences(String fichero) {
        SharedPreferences pc;
        SharedPreferences.Editor editor;
        pc = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pc.edit();
        editor.putString(getString(R.string.sharedKey), fichero);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void readSettings() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        ext = sharedPreferences.getString("extension", ".txt");
        selecAlm = sharedPreferences.getBoolean("ficheL", false);
        guardaHis = sharedPreferences.getBoolean("ficheH", false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ID_PERMISO_LEER_CONTACTOS) {
            //PackageManager.PERMISSION_DENIED; -1
            //PackageManager.PERMISSION_GRANTED; 0
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    accion();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                finish();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void accion() throws IOException {
        contactos = getListaContactos();
        contacts = "";
        for (Contacto c : contactos
        ) {
            contacts += c.toString();
        }
        fichero = nom.getText().toString() + ext.trim();
    }


    public ArrayList<Contacto> getListaContactos() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1", "1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        ArrayList<Contacto> lista = new ArrayList<>();
        Contacto contacto;
        while (cursor.moveToNext()) {
            contacto = new Contacto();
            contacto.setId(cursor.getLong(indiceId));
            contacto.setNombre(cursor.getString(indiceNombre));
            contacto.setTelefonos(getListaTelefonos(contacto.getId()));
            lista.add(contacto);
        }
        Collections.sort(lista);
        return lista;
    }

    public String getListaTelefonos(long id) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String argumentos[] = new String[]{id + ""};
        String orden = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String numero = "";
        while (cursor.moveToNext()) {
            numero += cursor.getString(indiceNumero) + ",";
        }
        numero.substring(0, numero.length() - 1);
        return numero;
    }
}
