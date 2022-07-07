package ordinary.frostsreport.ui.helper.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ordinary.frostsreport.ui.helper.items.Client
import ordinary.frostsreport.ui.helper.items.Product

class DbManager(context: Context) {
    val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = dbHelper.writableDatabase
    }
    fun closeDb(){
        dbHelper.close()
    }

    fun inserClientToDb(name: String){
        val value = ContentValues().apply {
            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME,name)
        }
        db?.insert(MyDbNameClass.Client.TABLE_NAME_CLIENT,null,value)
    }
    fun inserProductToDb(name: String, price: Double){
        val values = ContentValues().apply {
            put(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME,name)
            put(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_PRICE,price)
        }
        db?.insert(MyDbNameClass.Product.TABLE_NAME_PRODUCT,null,values)
    }

    val readFromClient : Cursor
    get() {
        val db = dbHelper.writableDatabase
        val res = db.rawQuery("SELECT * FROM " + MyDbNameClass.Client.TABLE_NAME_CLIENT, null)
        return res
    }
    val readFromProduct : Cursor
    get() {
        val db = dbHelper.writableDatabase
        val res = db.rawQuery("SELECT * FROM " + MyDbNameClass.Product.TABLE_NAME_PRODUCT, null)
        return res
    }

    fun deleteClient(client: String):Int {
        val db = dbHelper.writableDatabase
        return db.delete(MyDbNameClass.Client.TABLE_NAME_CLIENT, MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME + "=?", arrayOf(client))
    }
    fun deleteProduct(product: String):Int {
        val db = dbHelper.writableDatabase
        return db.delete(MyDbNameClass.Product.TABLE_NAME_PRODUCT, MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME + "=?", arrayOf(product))
    }
}


