package ordinary.frostsreport.ui.clients

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ordinary.frostsreport.databinding.FragmentReportClientsOrdersBinding
import ordinary.frostsreport.ui.helper.Common
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ClientsOrdersReportAdapter
import ordinary.frostsreport.ui.helper.adapter.ReportAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Order
import ordinary.frostsreport.ui.helper.items.OrderProduct
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class Report : Fragment() {

    private var binding: FragmentReportClientsOrdersBinding? = null

    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var reportAmount: Double = 0.0
    private val orderSummaryArrayList = ArrayList<Order>()
    private val orderProducts = HashMap<Int,ArrayList<OrderProduct>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReportClientsOrdersBinding.inflate(inflater, container, false)

        binding?.clientName?.text = arguments?.getString("client_name");

        val textStartDate = binding?.textStartDate
        val textEndDate = binding?.textEndDate
        val buttonStartDate = binding?.buttonStartDate
        val buttonEndDate = binding?.buttonEndDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)

//        startDate = formatter.parse("$day/${month + 1}/$year") as Date
//        endDate = formatter.parse("$day/${month + 1}/$year") as Date
//        textStartDate?.text = "$day/${month + 1}/$year"
//        textEndDate?.text = "$day/${month + 1}/$year"
        
        buttonStartDate?.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textStartDate?.text = "$mDay/${mMonth + 1}/$mYear"
                    startDate = formatter.parse(textStartDate?.text.toString()) as Date
                    orderSummaryArrayList.clear()
                    orderProducts.clear()
                    reportAmount = 0.0
                    onViewCreated(binding?.root as View,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        buttonEndDate?.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textEndDate?.text = "$mDay/${mMonth + 1}/$mYear"
                    endDate = formatter.parse(textEndDate?.text.toString()) as Date
                    orderSummaryArrayList.clear()
                    orderProducts.clear()
                    reportAmount = 0.0
                    onViewCreated(binding?.root as View,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showList()
        val eListView = binding?.eListView
        eListView?.setAdapter(ClientsOrdersReportAdapter(MAIN,orderSummaryArrayList,orderProducts))

        val uploadPdfButton = binding?.uploadButton

        uploadPdfButton?.setOnClickListener {
            MAIN.alert("Возможна дальнейшее добавление функции импорт в пдф",1500)
//            writeToFile("Cool")

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
        orderSummaryArrayList.clear()
        orderProducts.clear()
        reportAmount = 0.0
    }
    private fun showList() {
        dbManager.openDb()

        val amount = binding?.reportAmount
        val orders = dbManager.getClientOrders(arguments?.getString("client_name").toString())

        orders.forEach { order ->
            val orderDate = formatter.parse(order.orderDate) as Date
            if(startDate == null && endDate == null) {
                orderSummaryArrayList.add(order)
                loadOrderProducts(order.orderId!!)
                reportAmount += order.amount
            }
            else if(startDate != null && endDate != null){
                if(orderDate >= startDate && orderDate <= endDate){
                    orderSummaryArrayList.add(order)
                    loadOrderProducts(order.orderId!!)
                    reportAmount += order.amount

//                    writeToFile("№${orders.getInt(0)}  ${orders.getString(1)} " +
//                            "${orders.getString(2)} ${orders.getString(3)}")

                }
            }
            else if (startDate != null){
                if(orderDate >= startDate){
                    orderSummaryArrayList.add(order)
                    loadOrderProducts(order.orderId!!)
                    reportAmount += order.amount
                }
            }
            else if (endDate != null){
                if(orderDate <= endDate) {
                    orderSummaryArrayList.add(order)
                    loadOrderProducts(order.orderId!!)
                    reportAmount += order.amount
                }
            }
        }
        amount?.text = reportAmount.toString()

        dbManager.closeDb()
    }
    private fun loadOrderProducts(orderId: Int) {
        dbManager.openDb()

        orderProducts[orderId] = dbManager.getOrderProducts(orderId)

        dbManager.closeDb()
    }
}