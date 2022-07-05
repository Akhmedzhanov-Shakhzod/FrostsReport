package ordinary.frostsreport.ui.helper.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class DbManager(context: Context) {
    val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null

    fun openDb(){
        db = dbHelper.readableDatabase
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

    fun readFromClient(): ArrayList<String>{
        val dataList = ArrayList<String>()
        val cursor = db?.query(MyDbNameClass.Client.TABLE_NAME_CLIENT,null,null,null,
            null,null,MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME)

        while (cursor?.moveToNext()!!){
            val element = cursor.getColumnIndex(MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME)
                .let { cursor.getString(it) }
            dataList.add(element.toString())
        }
        cursor.close()
        return dataList
    }
    fun readFromProduct(): ArrayList<Pair<String, Double>> {
        val dataList = ArrayList<Pair<String,Double>>()
        val cursor = db?.query(MyDbNameClass.Product.TABLE_NAME_PRODUCT,null,null,null,
            null,null,MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME)

        while (cursor?.moveToNext()!!){
            val element1 = cursor.getColumnIndex(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME)
                .let { cursor.getString(it) }
            val element2 = cursor.getColumnIndex(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_PRICE)
                .let { cursor.getString(it) }
            dataList.add(element1.toString() to element2.toDouble())
        }
        cursor.close()
        return dataList
    }
}


