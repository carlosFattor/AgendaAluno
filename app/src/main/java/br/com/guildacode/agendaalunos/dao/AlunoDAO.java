package br.com.guildacode.agendaalunos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.guildacode.agendaalunos.models.Aluno;

/**
 * Created by carlos on 04/03/17.
 */

public class AlunoDAO extends SQLiteOpenHelper{


    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "rate REAL, " +
                "pathFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";

        switch (oldVersion){
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN pathFoto TEXT";
                db.execSQL(sql);

            case 2:
                System.out.println("Caso tenha uma nova versão");
        }

    }

    public void insertAluno(Aluno aluno) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = fillAluno(aluno);

        db.insert("Alunos", null, values);

        DatabaseManager.getInstance().closeDatabase();
    }

    public List<Aluno> getAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM Alunos;";
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery(sql, null);
        while(c.moveToNext()){
            parseAluno(alunos, c);
        }
        c.close();
        DatabaseManager.getInstance().closeDatabase();
        return alunos;
    }

    public void delete(Aluno aluno) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void updateAluno(Aluno aluno) {
        DatabaseManager.initializeInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String[] params = {aluno.getId().toString()};
        db.update("Alunos", this.fillAluno(aluno), "id = ?", params);
    }

    private void parseAluno(List<Aluno> alunos, Cursor c) {
        Aluno aluno = new Aluno();

        aluno.setId(c.getLong(c.getColumnIndex("id")));
        aluno.setNome(c.getString(c.getColumnIndex("nome")));
        aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
        aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
        aluno.setSite(c.getString(c.getColumnIndex("site")));
        aluno.setRate(c.getDouble(c.getColumnIndex("rate")));
        aluno.setPathFoto(c.getString(c.getColumnIndex("pathFoto")));

        alunos.add(aluno);
    }

    @NonNull
    private ContentValues fillAluno(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("endereco", aluno.getEndereco());
        values.put("telefone", aluno.getTelefone());
        values.put("site", aluno.getSite());
        values.put("rate", aluno.getRate());
        values.put("pathFoto", aluno.getPathFoto());
        return values;
    }
}
