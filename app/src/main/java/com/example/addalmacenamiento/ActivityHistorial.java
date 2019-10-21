package com.example.addalmacenamiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class ActivityHistorial extends AppCompatActivity {
    private String fichero;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        recyclerView= findViewById(R.id.rVHistorial);
        fichero=readPreferences();
        initRecyclerView();
    }

    private String readPreferences() {
        SharedPreferences pc= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return pc.getString(getString(R.string.sharedKey),null);
    }

    private void initRecyclerView() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        if(fichero!=null){
            String[]ficheros=this.fichero.split("&");
            mAdapter = new MyAdapter(ficheros,getApplicationContext());
            recyclerView.setAdapter(mAdapter);
        }
    }


}
