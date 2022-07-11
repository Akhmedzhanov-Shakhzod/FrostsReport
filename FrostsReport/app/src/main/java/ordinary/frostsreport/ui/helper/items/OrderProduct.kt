package ordinary.frostsreport.ui.helper.items

data class OrderProduct(var product: Product, var productCount:Double,
                        var id: Int? = null, var productAmount: Double = product.price * productCount )
