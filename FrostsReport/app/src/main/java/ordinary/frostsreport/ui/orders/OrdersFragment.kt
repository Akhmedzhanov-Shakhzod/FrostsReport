package ordinary.frostsreport.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentOrdersBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.OrderAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Order

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null

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
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val listViewOrders = binding.listViewOrders

        val orders = dbManager.readFromOrders

        while (orders.moveToNext()) {
            orders_arraylist.add(Order(orders.getString(1),orders.getString(2),orders.getDouble(3)))
        }

        listViewOrders.isClickable = true
        listViewOrders.adapter = OrderAdapter(MAIN,orders_arraylist)

        listViewOrders.setOnItemClickListener { parent, view, position, id ->
            listViewOrders.getItemAtPosition(position)


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        orders_arraylist.clear()
        dbManager.closeDb()
    }
}