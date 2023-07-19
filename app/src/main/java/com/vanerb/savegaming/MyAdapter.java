package com.vanerb.savegaming;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    public ArrayList<Game> games;
    Integer[] imagenesplat = {R.drawable.pc, R.drawable.xbox, R.drawable.playstation, R.drawable.nintendo, R.drawable.more};

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private TextView nombre, descripcion, plataforma, platino;
        private ImageView imageview;
        private ImageView imgplat;

        public ViewHolder(View view) {
            super(view);

            nombre = (TextView) view.findViewById(R.id.txtnombreshow);
            plataforma = view.findViewById(R.id.txtplatshow);
            imageview = view.findViewById(R.id.imageView);
            imgplat = view.findViewById(R.id.imgplat);
            view.setOnCreateContextMenuListener(this);



        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), R.id.opeditar, 0, "Editar");
            menu.add(this.getAdapterPosition(), R.id.opeliminar, 0, "Borrar");
        }

        public TextView getNombre() {
            return nombre;
        }


        public TextView getPlataforma() {
            return plataforma;
        }


        public ImageView getImageview() {
            return imageview;
        }

        public ImageView getImgplat() {
            return imgplat;
        }
    }

    public MyAdapter(ArrayList<Game> title) {
        games = title;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plantillashow, viewGroup, false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getNombre().setText(games.get(position).getNombre());
        //viewHolder.getDescripcion().setText(games.get(position).getDescripcion());
        viewHolder.getPlataforma().setText(games.get(position).getPlataforma());
        switch (games.get(position).getPlataforma()){
            case "PC":
                viewHolder.getImgplat().setImageResource(imagenesplat[0]);

                break;

            case "XBOX":
                viewHolder.getImgplat().setImageResource(imagenesplat[1]);

                break;

            case "PLAYSTATION":
                viewHolder.getImgplat().setImageResource(imagenesplat[2]);

                break;

            case "NINTENDO":
                viewHolder.getImgplat().setImageResource(imagenesplat[3]);

                break;
            case "OTRO":
                viewHolder.getImgplat().setImageResource(imagenesplat[4]);

                break;

            default:
                viewHolder.getImgplat().setImageResource(R.drawable.game1);

                break;
        }

        viewHolder.getImageview().setImageResource(R.drawable.informacion);
    }


    @Override
    public int getItemCount() {
        return games.size();
    }

    public void anadir(Context a, Game game){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getWritableDatabase();

        db.execSQL("INSERT INTO Game(nombre, descripcion, plataforma, platino, nota) VALUES('"+game.getNombre()+"','"+game.getDescripcion()+"','"+game.getPlataforma()+"','"+game.isPlatino()+"','"+game.getNota()+"')");

        db.close();
        notifyDataSetChanged();
    }

    public void borrarTodo(Context a){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getWritableDatabase();

        db.execSQL("DELETE FROM Game");
        db.close();
        notifyDataSetChanged();
    }

    public void eliminar(Context a, int id){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getWritableDatabase();

        db.execSQL("DELETE FROM Game WHERE _id="+id);
        db.close();
        notifyDataSetChanged();
    }

    public void editar(Context a, Game game, String id){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getWritableDatabase();

        db.execSQL("UPDATE Game SET nombre='"+game.getNombre()+"', descripcion='"+game.getDescripcion()+"', plataforma='"+game.getPlataforma()+"', platino='"+game.isPlatino()+"', nota='"+game.getNota()+"' WHERE _id='"+id+"'");

        db.close();
        notifyDataSetChanged();
    }



    public void filtrarplataforma(Context a, String plataformas){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nombre, descripcion, plataforma, platino, _id, nota FROM Game WHERE plataforma='"+plataformas+"'", null);
        String nombre, descripcion, plataforma;
        boolean platino;
        int id;
        float nota;
        games.clear();
        if(c.moveToFirst()){
            do{
                //
                nombre = c.getString(0);
                descripcion = c.getString(1);
                plataforma = c.getString(2);
                platino = Boolean.valueOf(c.getString(3));
                id = c.getInt(4);
                nota = Float.valueOf(c.getString(5)) ;

                games.add(new Game(id, nombre, descripcion, plataforma, platino, nota));

            }
            while (c.moveToNext());

        }

        c.close();
        db.close();
        notifyDataSetChanged();
    }



    public void buscar(Context a, String buscar){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nombre, descripcion, plataforma, platino, _id, nota FROM Game WHERE nombre LIKE '%"+buscar+"%'", null);
        String nombre, descripcion, plataforma;
        boolean platino;
        int id;
        float nota;
        games.clear();
        if(c.moveToFirst()){
            do{
                //
                nombre = c.getString(0);
                descripcion = c.getString(1);
                plataforma = c.getString(2);
                platino = Boolean.valueOf(c.getString(3));
                id = c.getInt(4);
                nota = Float.valueOf(c.getString(5));
                games.add(new Game(id, nombre, descripcion, plataforma, platino, nota));

            }
            while (c.moveToNext());

        }

        c.close();
        db.close();
        notifyDataSetChanged();

    }



    public void busquedaavanzada(Context a, String plataform, String buscar){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nombre, descripcion, plataforma, platino, _id, nota FROM Game WHERE plataforma = '"+plataform+"' AND nombre LIKE '%"+buscar+"%'" , null);

        String nombre, descripcion, plataforma;
        boolean platino;
        int id;
        float nota1;
        games.clear();
        if(c.moveToFirst()){
            do{
                //
                nombre = c.getString(0);
                descripcion = c.getString(1);
                plataforma = c.getString(2);
                platino = Boolean.valueOf(c.getString(3));
                id = c.getInt(4);
                nota1 = Float.valueOf(c.getString(5));
                games.add(new Game(id, nombre, descripcion, plataforma, platino, nota1));

            }
            while (c.moveToNext());

        }

        c.close();
        db.close();
        notifyDataSetChanged();
    }

    public void Mostrar(Context a){
        MySql mysql = new MySql(a.getApplicationContext(), "juego.db", null, 1);

        SQLiteDatabase db = mysql.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nombre, descripcion, plataforma, platino, _id, nota FROM Game", null);
        String nombre, descripcion, plataforma;
        boolean platino;
        int id;
        float nota;
        games.clear();
        if(c.moveToFirst()){
            do{
                //
                nombre = c.getString(0);
                descripcion = c.getString(1);
                plataforma = c.getString(2);
                platino = Boolean.valueOf(c.getString(3));
                id = c.getInt(4);
                nota = Float.valueOf(c.getString(5));
                games.add(new Game(id, nombre, descripcion, plataforma, platino, nota));

            }
            while (c.moveToNext());

        }

        c.close();
        db.close();
        notifyDataSetChanged();
    }



}
