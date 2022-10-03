package ordinary.frostsreport.ui.expenses

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ordinary.frostsreport.databinding.FragmentAddExpensesBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.AddExpenseAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Expense
import ordinary.frostsreport.ui.helper.items.Order
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddExpensesFragment : Fragment() {

    private var _binding: FragmentAddExpensesBinding? = null
    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private val expenses_arraylist = ArrayList<Expense>()
    private var isSwitch = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textData = binding.textDate
        val buttonDate = binding.buttonDate
        val isSwitched = binding.isSwitched

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)

        textData.text = "$day/${month+1}/$year"

        dbManager.openDb()

        loadList()

        isSwitched.text = "До указанной даты"
        isSwitched.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                isSwitch = true
                isSwitched.text = "Указанная дата"
            }
            else{
                isSwitch = false
                isSwitched.text = "До указанной даты"
            }
            expenses_arraylist.clear()
            loadList()
        }
        buttonDate.setOnClickListener {
            val dpd = DatePickerDialog(MAIN,DatePickerDialog.OnDateSetListener{
                view,mYear,mMonth,mDay ->
                textData.text = "$mDay/${mMonth+1}/$mYear"
                expenses_arraylist.clear()
                loadList()
            },year,month,day)
            dpd.show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        expenses_arraylist.clear()
        dbManager.closeDb()
    }

    private fun loadList(){
        val date = formatter.parse(binding.textDate.text.toString())
        val addExpenses = binding.addExpenses

        val orders = dbManager.readFromOrders

        while (orders.moveToNext()){
            val dateOrder = formatter.parse(orders.getString(1))

            if (dateOrder != null) {
                if(orders.getInt(5) == 0 &&
                    if(isSwitch) dateOrder <= date else dateOrder == date){
                    val products = dbManager.getOrderProducts(orders.getInt(0))

                    products.forEach { productOrder ->
                        val productId = dbManager.getProductId(productOrder.product)
                        var isNotExist = true
                        expenses_arraylist.forEach { expense ->
                            if(expense.productId == productId){
                                expense.productCount += productOrder.productCount
                                isNotExist = false
                            }
                        }
                        if(isNotExist){
                            expenses_arraylist.add(Expense(orders.getString(1),productId,0.0,
                                productOrder.productCount,productOrder.product.price))
                        }
                    }
                }
            }
        }

        binding.listViewExpenses.adapter = AddExpenseAdapter(MAIN, expenses_arraylist)

        var success = false
        addExpenses.setOnClickListener {
            for(position in 0 until binding.listViewExpenses.adapter.count){
                val expense = binding.listViewExpenses.adapter.getItem(position)
                if (dbManager.insertExpenseToDb(expense as Expense) != 0)   MAIN.alert("Что-то пошло не так :(", 1000)
                else success = true
            }

            if (success){
                val orders = dbManager.readFromOrders
                while (orders.moveToNext()){
                    val dateOrder = formatter.parse(orders.getString(1))

                    if (dateOrder != null) {
                        if(orders.getInt(5) == 0 &&
                            if(isSwitch) dateOrder <= date else dateOrder == date){
                            val order = Order(orders.getString(1), orders.getString(2), orders.getDouble(3),orders.getInt(0),
                                orders.getInt(4) == 1,orders.getInt(5) == 1,orders.getInt(6))

                            order.isReported = true
                            success = dbManager.updateOrder(order.orderId!!,order)
                        }
                    }
                }

            }
            if(success)    MAIN.onExpense()
        }
    }
}


