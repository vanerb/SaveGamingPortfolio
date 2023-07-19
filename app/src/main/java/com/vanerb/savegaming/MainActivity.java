package com.vanerb.savegaming;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public RecyclerView mRecyclerView;
    MyAdapter mAdapter;
    ArrayList<Game> games = new ArrayList<>();
    String[] plataformas = {"PC", "PLAYSTATION", "NINTENDO", "XBOX", "OTRO"};
    ImageView filterdeletebt;
    String buscar;
    String platforma;
    Spinner filterplatform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btadd = findViewById(R.id.btaddmenu);

        mRecyclerView = findViewById(R.id.rView);

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(games);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.Mostrar(getApplicationContext());

        filterplatform = findViewById(R.id.filterplatform);

        filterplatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                platforma = filterplatform.getSelectedItem().toString();
                if(filterplatform.getSelectedItem().toString().equals("TODO")){
                    filterdeletebt.setVisibility(View.INVISIBLE);

                    mAdapter.Mostrar(getApplicationContext());

                }
                else{
                    if(buscar == null){
                        mAdapter.filtrarplataforma(getApplicationContext(), filterplatform.getSelectedItem().toString());

                    }
                    else{
                        mAdapter.busquedaavanzada(getApplicationContext(), filterplatform.getSelectedItem().toString(), buscar);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMenu();


            }
        });

        mRecyclerView.addOnItemTouchListener(new buttonManager(this,
                mRecyclerView, new buttonManager.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                    Intent intent = new Intent(getApplicationContext(), masinfo.class);
                    intent.putExtra("titulo", games.get(position).getNombre());
                    intent.putExtra("opinion", games.get(position).getDescripcion());
                    intent.putExtra("plataforma", games.get(position).getPlataforma());
                    intent.putExtra("platino", games.get(position).isPlatino());
                    intent.putExtra("id", games.get(position).getId());
                    intent.putExtra("nota", games.get(position).getNota());
                    startActivity(intent);
                    finish();
            }}));

        filterdeletebt = findViewById(R.id.deletefilterbt);
        filterdeletebt.setVisibility(View.INVISIBLE);
        filterdeletebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.Mostrar(getApplicationContext());
                buscar = "";
                filterplatform.setSelection(0);

                filterdeletebt.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        MenuInflater ift = getMenuInflater();
        ift.inflate(R.menu.menuoptions, m);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.acercade:
                Acercade();
                break;

            case R.id.bt_buscar:
                Buscar();
                break;

            case R.id.borrartodo:
                BorrarTodo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.opeditar:
                EditMenu(games.get(item.getGroupId()).getId(), games.get(item.getGroupId()).getNombre(), games.get(item.getGroupId()).getDescripcion(),games.get(item.getGroupId()).getPlataforma(), games.get(item.getGroupId()).isPlatino(), games.get(item.getGroupId()).getNota());
                mAdapter.Mostrar(getApplicationContext());

                break;
            case R.id.opeliminar:
                Eliminar(games.get(item.getGroupId()).getId());
                mAdapter.Mostrar(getApplicationContext());
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void BorrarTodo(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.aviso);

        Button si = dialog.findViewById(R.id.btsi);
        Button no = dialog.findViewById(R.id.btno);

        TextView txtaviso = dialog.findViewById(R.id.txtaviso);
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        filterplatform.setSelection(0);
        buscar = "";
        platforma = "TODO";
        filterdeletebt.setVisibility(View.INVISIBLE);
        mAdapter.Mostrar(getApplicationContext());
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.borrarTodo(getApplicationContext());
                MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.deletesound);
                music.start();
                mAdapter.Mostrar(getApplicationContext());
                notification("Elementos eliminados", "Se ha eliminado el listado con exito");

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void Acercade(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.acercade);
        dialog.show();
    }

    public void Buscar(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buscar);

        EditText editText = dialog.findViewById(R.id.edbuscar);
        Button btbuscar= dialog.findViewById(R.id.btbuscard);

        btbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().isEmpty()){

                }
                else{
                    buscar = editText.getText().toString();
                    if(platforma.equals("TODO")){
                        mAdapter.buscar(getApplicationContext(), editText.getText().toString());

                    }
                    else{
                        mAdapter.busquedaavanzada(getApplicationContext(), platforma, buscar);
                    }
                    filterdeletebt.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    public void AddMenu(){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.addform);

        Button btadd = dialog.findViewById(R.id.btadd);
        EditText txtnombre  = dialog.findViewById(R.id.txtname);
        EditText txtDescr = dialog.findViewById(R.id.txtdescr);
        CheckBox cbplatino = dialog.findViewById(R.id.cbplatino);
        Spinner platformselect = dialog.findViewById(R.id.ptselect);
        EditText ednota = dialog.findViewById(R.id.addednota);
        filterplatform.setSelection(0);
        buscar = "";
        platforma = "TODO";
        filterdeletebt.setVisibility(View.INVISIBLE);
        mAdapter.Mostrar(getApplicationContext());
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtnombre.getText().toString().equals("") || txtnombre.getText().toString().trim().isEmpty() || ednota.getText().toString().equals("") || ednota.getText().toString().trim().isEmpty()){
                }
                else{
                    float nota = Float.valueOf(ednota.getText().toString());
                    if(Float.valueOf(ednota.getText().toString())>10f){
                        nota = 10;
                    }
                    else if(Float.valueOf(ednota.getText().toString())<0f){
                        nota = 0;
                    }
                    mAdapter.anadir(getApplicationContext(), new Game(0+1,txtnombre.getText().toString().toUpperCase(Locale.ROOT), txtDescr.getText().toString(), platformselect.getSelectedItem().toString(), cbplatino.isChecked(), nota ));
                    mAdapter.Mostrar(getApplicationContext());
                    MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.add);
                    music.start();
                    notification("Elemento añadido", "Se ha añadido con exito el elemento");
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    public void Eliminar(int id){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.aviso);

        Button si = dialog.findViewById(R.id.btsi);
        Button no = dialog.findViewById(R.id.btno);

        TextView txtaviso = dialog.findViewById(R.id.txtaviso);
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);
        filterplatform.setSelection(0);
        buscar = "";
        platforma = "TODO";
        filterdeletebt.setVisibility(View.INVISIBLE);
        mAdapter.Mostrar(getApplicationContext());
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.eliminar(getApplicationContext(), id);
                MediaPlayer music = MediaPlayer.create(MainActivity.this, R.raw.deletesound);
                music.start();
                mAdapter.Mostrar(getApplicationContext());
                notification("Elemento eliminado", "Se ha eliminado con exito el elemento");

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void notification(String titulo, String subtitulo){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"name",NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        //PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);
        Notification notification=new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentText(subtitulo)
                .setContentTitle(titulo)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_dialog_info,"Mas información",pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1,notification);

        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    public void EditMenu(int id, String nombre, String opinion, String plataforma, boolean platino, float nota){
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.editform);

        Button btedit = dialog.findViewById(R.id.btedit);
        EditText txtnombre  = dialog.findViewById(R.id.ednombre);
        EditText txtDescr = dialog.findViewById(R.id.edopinion);
        CheckBox cbplatino = dialog.findViewById(R.id.cbplatinoed);
        Spinner platformselect = dialog.findViewById(R.id.spplataformaed);
        EditText ednota = dialog.findViewById(R.id.editnotaed);
        ednota.setText(String.valueOf(nota));
        txtnombre.setText(nombre);
        txtDescr.setText(opinion);
        filterplatform.setSelection(0);
        buscar = "";
        platforma = "TODO";
        filterdeletebt.setVisibility(View.INVISIBLE);
        mAdapter.Mostrar(getApplicationContext());
        if(platino){
            cbplatino.setChecked(true);
        }
        else{
            cbplatino.setChecked(false);

        }


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, plataformas);
        platformselect.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(plataforma);
        platformselect.setSelection(spinnerPosition);


        btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtnombre.getText().toString().equals("") || txtnombre.getText().toString().trim().isEmpty() || ednota.getText().toString().equals("") || ednota.getText().toString().trim().isEmpty()){

                }
                else{
                    float nota = Float.valueOf(ednota.getText().toString());
                    if(Float.valueOf(ednota.getText().toString())>10f){
                        nota = 10;
                    }
                    else if(Float.valueOf(ednota.getText().toString())<0f){
                        nota = 0;
                    }
                    mAdapter.editar(getApplicationContext(), new Game(id, txtnombre.getText().toString().toUpperCase(Locale.ROOT), txtDescr.getText().toString(), platformselect.getSelectedItem().toString(), cbplatino.isChecked(), nota), String.valueOf(id));
                    mAdapter.Mostrar(getApplicationContext());
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }
}