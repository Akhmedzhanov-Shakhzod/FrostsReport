package ordinary.frostsreport.ui.helper.adapter

import android.app.Activity
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.items.Order
import ordinary.frostsreport.ui.helper.items.OrderProduct
import ordinary.frostsreport.ui.helper.items.OrederProducts
import ordinary.frostsreport.ui.helper.items.Product

class OrderProductsAdapter(private val context: Activity, private val product: ArrayList<OrderProduct>):
    ArrayAdapter<OrderProduct>(context, R.layout.order_product_item, product) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.order_product_item,null)

        val product_name: TextView = view.findViewById(R.id.product_name)
        val product_price: TextView = view.findViewById(R.id.product_price)
        val product_count: TextView = view.findViewById(R.id.product_count)
        val product_amount: TextView = view.findViewById(R.id.product_amount)

        product_name.text = product[position].product.name
        product_price.text = product[position].product.price.toString()
        product_count.text = product[position].productCount.toString()
        product_amount.text = product[position].productAmount.toString()
        return view
    }
}