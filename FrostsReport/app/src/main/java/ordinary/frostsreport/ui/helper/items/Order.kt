package ordinary.frostsreport.ui.helper.items

data class Order(var orderDate: String, var orderClient: String, var amount:Double, var orderId: Int? = null)