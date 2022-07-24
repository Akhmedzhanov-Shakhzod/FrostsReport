package ordinary.frostsreport.ui.helper.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ordinary.frostsreport.ui.helper.items.*

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
            if(clients.getString(1) == name){
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
            if(products.getString(1) == name && products.getString(2) == price.toString()){
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
    fun insertOrderToDb(order: Order): Int {
        val orders = readFromOrders

        while (orders.moveToNext()) {
            if(orders.getString(0).toInt() == order.orderId){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_DATE,order.orderDate)
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_CLIENT,order.orderClient)
            put(MyDbNameClass.Orders.COLUMN_NAME_ORDER_AMOUNT,order.amount)
        }
        val success = db?.insert(MyDbNameClass.Orders.TABLE_NAME_ORDERS,null,values)
        return if(Integer.parseInt("$success") != -1) 0 else 2
    }
    fun insertOrderProductToDb(orderProduct: OrederProducts): Int {
        val orderProducts = readFromOrderProducts

        while (orderProducts.moveToNext()) {
            if(orderProducts.getString(0).toInt() == orderProduct.orderProductId){
                return 1
            }
        }
        val values = ContentValues().apply {
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_ORDER_ID,orderProduct.orderId)
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_PRODUCT_ID,orderProduct.productId)
            put(MyDbNameClass.OrderProducts.COLUMN_NAME_PRODUCT_COUNT,orderProduct.productCount)
        }
        val success = db?.insert(MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS,null,values)
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
        val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Client.TABLE_NAME_CLIENT +
                " ORDER BY " + MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME, null)
        return res
    }
    val readFromProduct : Cursor
    get() {
        val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Product.TABLE_NAME_PRODUCT +
                " ORDER BY " + MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME, null)
        return res
    }
    val readFromOrders : Cursor
        get() {
            val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.Orders.TABLE_NAME_ORDERS +
                    " ORDER BY " + MyDbNameClass.Orders.COLUMN_NAME_ORDER_ID + " DESC", null)
            return res
        }
    val readFromOrderProducts : Cursor
        get() {
            val res = db!!.rawQuery("SELECT * FROM " + MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS +
                    " ORDER BY " + MyDbNameClass.OrderProducts.COLUMN_NAME_ORDER_ID + " DESC", null)
            return res
        }

    fun getClientId(client: Client): Int {
        val clients = readFromClient

        while (clients.moveToNext()) {
            if(clients.getString(1) == client.name){
                return clients.getInt(0)
            }
        }
        return -1
    }
    fun getProductId(product: Product): Int {
        val products = readFromProduct

        while (products.moveToNext()) {
            if(products.getString(1) == product.name &&
                products.getString(2).toDouble() == product.price){
                return products.getInt(0)
            }
        }
        return -1
    }
    fun getOrderId(order: Order): Int {
        val orders = readFromOrders

        while (orders.moveToNext()) {
            if(orders.getString(1) == order.orderDate &&
                    orders.getString(2) == order.orderClient &&
                    orders.getString(3).toDouble() == order.amount){
                return orders.getInt(0)
            }
        }
        return -1
    }
    fun getOrderProductsId(orderProduct: OrederProducts): Int {
        val orderProducts = readFromOrderProducts

        while (orderProducts.moveToNext()) {
            if(orderProducts.getInt(1) == orderProduct.orderId &&
                    orderProducts.getInt(2) == orderProduct.productId &&
                    orderProducts.getString(3).toDouble() == orderProduct.productCount){
                return orderProducts.getInt(0)
            }
        }
        return -1
    }

    fun deleteClient(client: String): Boolean {
        val success = db!!.delete(MyDbNameClass.Client.TABLE_NAME_CLIENT, MyDbNameClass.Client.COLUMN_NAME_CLIENT_NAME + "=?", arrayOf(client))
        return Integer.parseInt("$success") != -1
    }
    fun deleteProduct(product: String): Boolean {
        val success = db!!.delete(MyDbNameClass.Product.TABLE_NAME_PRODUCT, MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME + "=?", arrayOf(product))
        return Integer.parseInt("$success") != -1
    }
    fun deleteOrders(orderId: Int): Boolean {
        val success = db!!.delete(MyDbNameClass.Orders.TABLE_NAME_ORDERS, MyDbNameClass.Orders.COLUMN_NAME_ORDER_ID + "=?", arrayOf(orderId.toString()))
        return Integer.parseInt("$success") != -1
    }
    fun deleteOrderProducts(orderProductId: Int): Boolean {
        val success = db!!.delete(MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS, MyDbNameClass.OrderProducts.COLUMN_NAME_ORDER_PRODUCTS_ID + "=?", arrayOf(orderProductId.toString()))
        return Integer.parseInt("$success") != -1
    }

    fun clearOrders(): Boolean {
        val success = db!!.delete(MyDbNameClass.Orders.TABLE_NAME_ORDERS, null,null)
        return Integer.parseInt("$success") != -1
    }
    fun clearOrderProducts(): Boolean {
        val success = db!!.delete(MyDbNameClass.OrderProducts.TABLE_NAME_ORDER_PRODUCTS, null,null)
        return Integer.parseInt("$success") != -1
    }

    fun getOrderProducts(orderId: Int): ArrayList<OrderProduct> {
        val allOP = readFromOrderProducts
        val op = ArrayList<OrderProduct>()

        while (allOP.moveToNext()){
            if(allOP.getInt(1) == orderId){
                val columns = Array<String?>(3) { null }
                columns[0] = MyDbNameClass.Product.COLUMN_NAME_PRODUCT_ID
                columns[1] = MyDbNameClass.Product.COLUMN_NAME_PRODUCT_NAME
                columns[2] = MyDbNameClass.Product.COLUMN_NAME_PRODUCT_PRICE

                val p = db!!.query(MyDbNameClass.Product.TABLE_NAME_PRODUCT, columns,
                    MyDbNameClass.Product.COLUMN_NAME_PRODUCT_ID + " = ${allOP.getString(2)}", null, null, null, null, null)

                while (p.moveToNext()){
                    val product = Product(p.getString(1),p.getDouble(2))
                    val pair = OrderProduct(product,allOP.getDouble(3),allOP.getInt(0))
                    op.add(pair)
                }
            }
        }
        return op
    }
    fun getClientOrders(client: String): ArrayList<Order> {
        var allOrders = readFromOrders
        var clientOrders = ArrayList<Order>()

        while (allOrders.moveToNext()) {
            if(allOrders.getString(2) == client) {
                clientOrders.add(Order(allOrders.getString(1),client,allOrders.getDouble(3),allOrders.getInt(0)))
            }
        }
        return  clientOrders
    }
}


