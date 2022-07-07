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

    fun inserClientToDb(name: String): Int {
        val clients = readFromClient

        while (clients.moveToNext()) {
            if(clients.getString(0) == name){
                return 1
            }
        }
        val value = ContentValues().apply {
            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME,name)
        }
        val success = db?.insert(MyDbNameClass.Client.TABLE_NAME_CLIENT,null,value)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }
    fun inserProductToDb(name: String, price: Double): Int {
        val products = readFromProduct

        while (products.moveToNext()) {
            if(products.getString(0) == name && products.getString(1) == price.toString()){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME,name)
            put(MyDbNameClass.Product.COLUMN_NAME_PRODUCT_PRICE,price)
        }
        val success = db?.insert(MyDbNameClass.Product.TABLE_NAME_PRODUCT,null,values)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }

//    fun update(oldItem: String, newItem: String): Boolean{
//        val value = ContentValues().apply {
//            put(MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME,newItem)
//        }
//        val success = db?.update(MyDbNameClass.Client.TABLE_NAME_CLIENT,value, MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME + "=?", arrayOf(oldItem))
//        return Integer.parseInt("$success") != -1
//    }
    fun updateClient(oldClient: String, newClient: String): Boolean{
        val success1 = inserClientToDb(newClient)
        var success2 = false
        if(success1 == 0){
            success2 = deleteClient(oldClient)
        }
        return success2
    }
    fun updateProduct(oldProduct: String, newProduct: Product): Boolean{
        var success2: Boolean = deleteProduct(oldProduct)
        val success1 = inserProductToDb(newProduct.name,newProduct.price)
        return success1 == 0 && success2
    }

    val readFromClient : Cursor
    get() {
        val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Client.TABLE_NAME_CLIENT, null)
        return res
    }
    val readFromProduct : Cursor
    get() {
        val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Product.TABLE_NAME_PRODUCT, null)
        return res
    }

    fun deleteClient(client: String): Boolean {
        val success = db!!.delete(MyDbNameClass.Client.TABLE_NAME_CLIENT, MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME + "=?", arrayOf(client))
        return Integer.parseInt("$success") != -1
    }
    fun deleteProduct(product: String): Boolean {
        val success = db!!.delete(MyDbNameClass.Product.TABLE_NAME_PRODUCT, MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME + "=?", arrayOf(product))
        return Integer.parseInt("$success") != -1
    }
}


