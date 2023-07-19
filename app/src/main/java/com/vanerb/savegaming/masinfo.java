package com.vanerb.savegaming;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class masinfo extends AppCompatActivity {
    String[] plataformas = {"PC", "PLAYSTATION", "NINTENDO", "XBOX", "OTRO"};
    MyAdapter mAdapter;
    ArrayList<Game> games = new ArrayList<>();
    TextView txttitulo, txtopinion, txtplataforma, txtplatino, txtnota;
    ImageView imageView;
    ImageView imagenportada;
    ImageView musthave;
    String titulo, opinion, plataforma, platino, imagen;
    Integer[] imagenesplat = {R.drawable.pc, R.drawable.xbox, R.drawable.playstation, R.drawable.nintendo, R.drawable.more};

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masinfo);

        mAdapter = new MyAdapter(games);


        txttitulo = findViewById(R.id.txttituloinfo);
        txtopinion = findViewById(R.id.txtopinioninfo);
        txtplataforma = findViewById(R.id.txtplataformainfo);
        txtplatino = findViewById(R.id.txtplatinoinfo);
        imageView = findViewById(R.id.imglogro);
        musthave = findViewById(R.id.musthave);
        txtnota = findViewById(R.id.txtnota);


        ImageView btedit = findViewById(R.id.bteditinfo);
        ImageView btdelete = findViewById(R.id.btdeleteinfo);
        imagenportada = findViewById(R.id.imgport);
        ImageView btback = findViewById(R.id.backbt);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        titulo = intent.getStringExtra("titulo");
        opinion = intent.getStringExtra("opinion");
        plataforma = intent.getStringExtra("plataforma");
        platino = String.valueOf(intent.getBooleanExtra("platino", false));
        txttitulo.setText(intent.getStringExtra("titulo"));
        txtopinion.setText(intent.getStringExtra("opinion"));
        txtplataforma.setText(intent.getStringExtra("plataforma"));
        //imagenportada.setImageURI(Uri.parse(intent.getParcelableExtra("imagen")));
        switch (plataforma.toUpperCase(Locale.ROOT)){
            case "PC":
                imagenportada.setImageResource(imagenesplat[0]);
                break;

            case "XBOX":
                imagenportada.setImageResource(imagenesplat[1]);
                break;

            case "PLAYSTATION":
                imagenportada.setImageResource(imagenesplat[2]);
                break;

            case "NINTENDO":
                imagenportada.setImageResource(imagenesplat[3]);
                break;
            case "OTRO":
                imagenportada.setImageResource(imagenesplat[4]);
                break;

            default:
                imagenportada.setImageResource(R.drawable.game1);

                break;
        }
        //imagenportada.setImageResource(R.drawable.game1);
        txtnota.setText(String.valueOf(intent.getFloatExtra("nota", 0f)) );

        if(intent.getFloatExtra("nota", 0f) > 5.0f){
            musthave.setImageResource(R.drawable.musthave);

        }
        else {
            musthave.setImageResource(R.drawable.no);

        }

        if(intent.getBooleanExtra("platino", false)){
            txtplatino.setText("Tienes todos los logros de "+intent.getStringExtra("titulo").toLowerCase(Locale.ROOT));
            imageView.setImageResource(R.drawable.trophy);
        }
        else{
            txtplatino.setText("No tienes los logros de "+intent.getStringExtra("titulo").toLowerCase(Locale.ROOT));
            imageView.setImageResource(R.drawable.notrophy);

        }

        btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditMenu(intent.getIntExtra("id", 0) , intent.getStringExtra("titulo"), intent.getStringExtra("opinion"), intent.getStringExtra("plataforma"), intent.getBooleanExtra("platino", false), intent.getFloatExtra("nota", 0f));

            }
        });

        btdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eliminar(intent.getIntExtra("id", 0));

            }
        });

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });




    }


    public void Eliminar(int id){
        Dialog dialog = new Dialog(masinfo.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.aviso);

        Button si = dialog.findViewById(R.id.btsi);
        Button no = dialog.findViewById(R.id.btno);

        TextView txtaviso = dialog.findViewById(R.id.txtaviso);
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.eliminar(getApplicationContext(), id);
                MediaPlayer music = MediaPlayer.create(masinfo.this, R.raw.deletesound);
                music.start();
                mAdapter.Mostrar(getApplicationContext());
                notification("Elemento eliminado", "Se ha eliminado con exito el elemento");
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();
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

    public void EditMenu(int id, String nombre, String opinion, String plataforma, boolean platino, float nota){
        Dialog dialog = new Dialog(masinfo.this);
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
                    mAdapter.editar(getApplicationContext(), new Game(id, txtnombre.getText().toString().toUpperCase(Locale.ROOT), txtDescr.getText().toString(), platformselect.getSelectedItem().toString(), cbplatino.isChecked(), nota ), String.valueOf(id) );
                    mAdapter.Mostrar(getApplicationContext());
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    finish();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }


    public void notification(String titulo, String subtitulo){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        String CHANNEL_ID="MYCHANNEL";
        NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);

        //PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);
        Notification notification=new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentText(subtitulo)
                .setContentTitle(titulo)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.sym_def_app_icon,"Mas información",pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .build();

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1,notification);

        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }
}