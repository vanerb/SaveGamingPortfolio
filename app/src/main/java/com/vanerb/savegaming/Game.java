package com.vanerb.savegaming;

public class Game {
    public int id;
    public String nombre;
    public String descripcion;
    public String plataforma;

    public boolean platino;
    float nota;

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public Game(int id, String nombre, String descripcion, String plataforma, boolean platino, float nota) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.plataforma = plataforma;
        this.platino = platino;
        this.nota = nota;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }



    public boolean isPlatino() {
        return platino;
    }

    public void setPlatino(boolean platino) {
        this.platino = platino;
    }
}
