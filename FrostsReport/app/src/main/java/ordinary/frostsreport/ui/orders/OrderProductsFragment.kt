package ordinary.frostsreport.ui.orders

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.OrderProductsAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Order
import ordinary.frostsreport.ui.helper.items.Product


class OrderProductsFragment : Fragment() {

    private val dbManager = DbManager(MAIN)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_products, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val delete = view.findViewById<Button>(R.id.delete_order)
        val list_view_order_products = view.findViewById<ListView>(R.id.list_view_order_products)

        val orderId = view.findViewById<TextView>(R.id.order_id)
        val clientName = view.findViewById<TextView>(R.id.order_client_name)
        val orderAmount = view.findViewById<TextView>(R.id.order_amount)
        val orderDate = view.findViewById<TextView>(R.id.order_date)
        val isCompleted = view.findViewById<Switch>(R.id.is_completed)

        val order = Order(
            arguments?.getString("orderDate").toString(),
            arguments?.getString("clientName").toString(),
            arguments?.getFloat("orderAmount")!!.toDouble(),
            arguments?.getInt("orderId"),
            arguments?.getBoolean("isCompleted") == true,
            arguments?.getBoolean("isReported") == true
        )

        orderId.text = "№" + order.orderId.toString()
        clientName.text = order.orderClient
        orderAmount.text = order.amount.toString()
        orderDate.text = order.orderDate

        if(order.isCompleted){
            isCompleted.isChecked = true
            isCompleted.text = "Отменить"
        }
        else{
            isCompleted.isChecked = false
            isCompleted.text = "Выпольнить"
        }

        isCompleted.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                order.isCompleted = true
                dbManager.updateOrder(order.orderId!!,order)
                isCompleted.text = "Отменить"
            }
            else{
                order.isCompleted = false
                dbManager.updateOrder(order.orderId!!,order)
                isCompleted.text = "Выпольнить"
            }
        }

        val products = dbManager.getOrderProducts(order.orderId!!)

        list_view_order_products.adapter = OrderProductsAdapter(MAIN,products)

        delete.setOnClickListener {
            if(dbManager.deleteOrders(order.orderId!!)){
                var success = true
                for(i in 0 until products.count()) {
                    success = dbManager.deleteOrderProducts(products[i].id!!)
                }
                if(success) {
                    MAIN.alert("Заказ ${orderId.text} - удалено",1000)
                    findNavController().navigate(R.id.nav_orders)
                }
                else {
                    MAIN.alert("Не получилось удалить заказ ${orderId.text}",1000)
                }
            }
            else {
                MAIN.alert("Не получилось удалить заказ ${orderId.text}",1000)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }
}