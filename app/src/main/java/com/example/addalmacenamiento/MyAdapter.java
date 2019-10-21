package com.example.addalmacenamiento;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] ficheros;
    private Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        private Button btContacts;
        public MyViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tvFichero);
            btContacts=v.findViewById(R.id.btAbrirF);
        }

    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, Context context) {
        ficheros = myDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(ficheros[position]+"\n");
        holder.btContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContacts(ficheros[position]);
            }
        });
    }

    public void showContacts(String fichero){
        Intent intent = new Intent(context, ActivityContactos.class);
        String[]ficheroArray=fichero.split(" - ");
        String nombreF=ficheroArray[0];
        String lugarAlm=ficheroArray[1];
        intent.putExtra("nombre",nombreF);
        intent.putExtra("lugar",lugarAlm);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
       context.startActivity(intent);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ficheros.length;
    }
}