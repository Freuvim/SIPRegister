package io.github.freuvim.sipregister.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class DAOSettings {
    private SQLiteDatabase database;
    private MngDatabase mngDatabase;

    // Colunas
    private static final String ID_SETTING = "id_setting";
    private static final String NAME_SETTING = "name_setting";
    private static final String VALUE_SETTING = "value_setting";

    private static final String[] todasAsColunas =
            {
                    ID_SETTING, NAME_SETTING, VALUE_SETTING
            };

    // Tabela
    private static final String TABLE_SETTINGS = "settings";

    public DAOSettings(Context contexto) {
        mngDatabase = new MngDatabase(contexto);
    }

    public void open() throws SQLException {
        database = mngDatabase.getWritableDatabase();
    }

    public void close() {
        mngDatabase.close();
    }

    public void insert(BeanSettings item) {
        ContentValues valores = new ContentValues();
        valores.put(ID_SETTING, item.getIdSetting());
        valores.put(NAME_SETTING, item.getName_setting());
        valores.put(VALUE_SETTING, item.getValue_setting());

        database.insert(TABLE_SETTINGS, null, valores);
    }

    public void update(BeanSettings item) {
        ContentValues valores = new ContentValues();
        valores.put(NAME_SETTING, item.getName_setting());
        valores.put(VALUE_SETTING, item.getValue_setting());

        database.update(TABLE_SETTINGS, valores, ID_SETTING + " = " + item.getIdSetting(), null);
    }

    public void delete(BeanSettings item) {
        int id = item.getIdSetting();
        database.delete(TABLE_SETTINGS, ID_SETTING + " = " + id, null);
    }

    public BeanSettings selectUm(BeanSettings item) {
        Cursor cursor = database.query(
                TABLE_SETTINGS,
                todasAsColunas,
                ID_SETTING + " = " + item.getIdSetting(),
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()){
            return cursorToItem(cursor);
        }
        return null;
    }

    public List<BeanSettings> selectTodos() {
        List<BeanSettings> itens = new ArrayList<>();
        Cursor cursor = database.query(TABLE_SETTINGS,
                todasAsColunas, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeanSettings item = cursorToItem(cursor);
            itens.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return itens;
    }

    private BeanSettings cursorToItem(Cursor cursor) {
        BeanSettings item = new BeanSettings();
            item.setIdSetting(cursor.getInt(0));
            item.setName_setting(cursor.getString(1));
            item.setValue_setting(cursor.getString(2));
        return item;
    }

    public void deletaTodos() {
        database.delete(TABLE_SETTINGS, null, null);
    }
}
