package ordinary.frostsreport.ui.helper.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context,MyDbNameClass.DATABASE_NAME,
    null,MyDbNameClass.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(MyDbNameClass.SQL_CREATE_CLIENT)
        db?.execSQL(MyDbNameClass.SQL_CREATE_PRODUCT)
        db?.execSQL(MyDbNameClass.SQL_CREATE_ORDERS)
        db?.execSQL(MyDbNameClass.SQL_CREATE_ORDER_PRODUCTS)
        db?.execSQL(MyDbNameClass.SQL_CREATE_SPENDING)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(MyDbNameClass.SQL_DELETE_CLIENT)
        db?.execSQL(MyDbNameClass.SQL_DELETE_PRODUCT)
        db?.execSQL(MyDbNameClass.SQL_DELETE_ORDERS)
        db?.execSQL(MyDbNameClass.SQL_DELETE_ORDER_PRODUCTS)
        db?.execSQL(MyDbNameClass.SQL_DELETE_SPENDING)

        onCreate(db)
    }
}

