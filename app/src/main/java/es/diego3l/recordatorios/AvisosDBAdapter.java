package es.diego3l.recordatorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dlolh on 07/05/2016.
 */
//Proxy intermediario de un objeto para controlar su acceso AvisosDBAdapter
public class AvisosDBAdapter {

    //estos son los nombres de las columnas
    public static final String COL_ID = "_id";
    public static final String COL_CONTENIDO = "content";
    public static final String COL_IMPORTANTE = "important";

    //estos son los índices correspondientes
    public static final int INDICE_ID = 0;
    public static final int INDICE_CONTENIDO = INDICE_ID + 1;
    public static final int INDICE_IMPORTANTE = INDICE_ID + 2;

    //usado para logearnos
    private static final String TAG = "AvisosDBAdapter";

    //2 Objetos Api Database
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //Constantes para el Nombre de la base de datos, Tabla y Versión
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;

    //Objeto Context
    private final Context mCtx;

    //declaración SQL usada para crear la base de datos
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENIDO + " TEXT, " +
                    COL_IMPORTANTE + " INTEGER );";

    //Métodos abrir y cerrar.
    public AvisosDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    //abrir
    public void abrir() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }

    //cerrar
    public void cerrar(){
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //Operaciones CRUD

    //CREATE
    //Ten en cuenta que la id será creada automáticamente
    public void crearRecordatorio (String nombre, boolean importante) {
        ContentValues values = new ContentValues(); //Servicio de enlace de datos
        values.put(COL_CONTENIDO, nombre);
        values.put(COL_IMPORTANTE, importante ? 1 : 0);
        mDb.insert(TABLE_NAME, null, values);
    }
    //sobrecargado para tomar un aviso
    public long crearRecordatorio(Aviso recordatorio) {
        ContentValues values = new ContentValues();  //Servicio de enlace de datos
        values.put(COL_CONTENIDO, recordatorio.getContenido());
        values.put(COL_IMPORTANTE, recordatorio.getImportante());

        // Insertar fila
        return mDb.insert(TABLE_NAME, null, values);
    }

    //READ
    public Aviso obtenerRecordatorioPorId(int id) {
        Cursor cursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                    COL_CONTENIDO, COL_IMPORTANTE}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }

            return new Aviso(
                    cursor.getInt(INDICE_ID),
                    cursor.getString(INDICE_CONTENIDO),
                    cursor.getInt(INDICE_IMPORTANTE)
            );

    }

    public Cursor obtenerTodosLosRecordatorios(){
        Cursor mCursor = mDb.query(TABLE_NAME, new String[]{COL_ID,
                        COL_CONTENIDO, COL_IMPORTANTE},
                null, null, null, null, null
        );

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //UPDATE
    public void actualizarRecordatorio(Aviso recordatorio) {
        ContentValues values = new ContentValues(); //Servicio de enlace de datos
        values.put(COL_CONTENIDO, recordatorio.getContenido());
        values.put(COL_IMPORTANTE, recordatorio.getImportante());
        mDb.update(TABLE_NAME, values,
                COL_ID + "=?", new String[]{String.valueOf(recordatorio.getId())});
    }

    //DELETE
    public void borrarRecordatorioPorId(int nId) {
        mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
    }

    public void borrarTodosLosRecordatorios() {
        mDb.delete(TABLE_NAME, null, null);
    }


    //Clase interna
    //Usada para abrir y cerrar DDBB y usará Context
    //que es una Clase Android Abstracta.
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) { //Context se encarga de abrir y cerrar DDBB
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
