package io.github.freuvim.sipregister.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MngDatabase extends SQLiteOpenHelper {
    // dados do banco
    private static final String NOME_BANCO = "db_settings";
    private static final int VERSAO_BANCO = 1;

    // tabelas
    private static final String TB_SETTINGS =
            "CREATE TABLE settings ( " +
                    "id_setting  INTEGER PRIMARY KEY, " +
                    "name_setting TEXT, " +
                    "value_setting  TEXT);";

    public MngDatabase(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // criação do banco
        db.execSQL(TB_SETTINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Setup inicial do banco. Ignorando upgrade
    }

}
