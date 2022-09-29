package ordinary.frostsreport.ui.helper.adapter

import android.app.Activity
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Order
import ordinary.frostsreport.ui.helper.items.OrderProduct
import ordinary.frostsreport.ui.helper.items.Product

class OrderAdapter(private val context: Activity, private val order: ArrayList<Order>):
    ArrayAdapter<Order>(context, R.layout.order_item, order) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.order_item,null)

        val order_id: TextView = view.findViewById(R.id.order_id)
        val order_client: TextView = view.findViewById(R.id.order_client_name)
        val order_date: TextView = view.findViewById(R.id.order_date)
        val order_amount: TextView = view.findViewById(R.id.order_amount)
        val is_completed: TextView = view.findViewById(R.id.is_completed)

        order_id.text = "№" + order[position].orderId.toString()
        order_client.text = order[position].orderClient
        order_date.text = order[position].orderDate
        order_amount.text = order[position].amount.toString()
        if(order[position].isCompleted)
            is_completed.text = "Выполнено"

        return view
    }
}