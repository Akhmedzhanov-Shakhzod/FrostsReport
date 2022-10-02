package ordinary.frostsreport.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentNotCompletedOrdersBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.OrderAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Order
import kotlin.collections.ArrayList

class NotCompletedOrdersFragment : Fragment() {

    private var _binding: FragmentNotCompletedOrdersBinding? = null

    private val dbManager = DbManager(MAIN)

    private val orders_arraylist = ArrayList<Order>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotCompletedOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()
        val listViewOrders = binding.listViewNotCompletedOrders
        val orders = dbManager.readFromOrders

        while (orders.moveToNext()) {
            if(orders.getInt(4) == 0){
                orders_arraylist.add(
                    Order(
                        orders.getString(1), orders.getString(2),
                        orders.getDouble(3), orders.getInt(0),
                        (orders.getInt(4) == 1)
                    )
                )
            }
        }

        listViewOrders.isClickable = true
        listViewOrders.adapter = OrderAdapter(MAIN,orders_arraylist)

        listViewOrders.setOnItemClickListener { parent, view, position, id ->
            listViewOrders.getItemAtPosition(position)

            val bundle = Bundle()

            orders_arraylist[position].orderId?.let { bundle.putInt("orderId", it) }
            bundle.putString("clientName",orders_arraylist[position].orderClient)
            bundle.putFloat("orderAmount", orders_arraylist[position].amount.toFloat())
            bundle.putString("orderDate",orders_arraylist[position].orderDate)
            bundle.putBoolean("isCompleted",orders_arraylist[position].isCompleted)

            findNavController().navigate(R.id.orderProductsFragment,bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        orders_arraylist.clear()
        dbManager.closeDb()
    }
}