package ordinary.frostsreport.ui.helper.items

data class Expense(var spendingDate: String, var productId: Int, var spendingAmount: Double,
                   var productCount: Double, var productPrice: Double, var spendingId: Int? = null)