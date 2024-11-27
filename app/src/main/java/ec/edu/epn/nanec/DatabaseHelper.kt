package ec.edu.epn.nanec

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

// Inicio de código nuevo
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "PuntosInteres.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "puntos_interes"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "nombre"
        const val COLUMN_ADDRESS = "direccion"
        const val COLUMN_DESCRIPTION = "descripcion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_ADDRESS TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPuntoInteres(nombre: String, direccion: String, descripcion: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, nombre)
            put(COLUMN_ADDRESS, direccion)
            put(COLUMN_DESCRIPTION, descripcion)
        }
        return db.insert(TABLE_NAME, null, values)
    }
    fun getAllPuntosInteres(): Cursor {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_NAME, COLUMN_ADDRESS, COLUMN_DESCRIPTION)
        return db.query(TABLE_NAME, null, null, null, null, null, null)
    }
}
// Fin de código nuevo
