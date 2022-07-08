package ordinary.frostsreport.ui.addOrder

import ordinary.frostsreport.ui.helper.items.Product

class DataModel {
   var client: String = ""
   val product = ArrayList<Pair<Product,Double>>()
   val productAmount = ArrayList<Double>()
}