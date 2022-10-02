package ordinary.frostsreport.ui.expenses

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ordinary.frostsreport.databinding.FragmentExpensesBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ExpenseAdapter
import ordinary.frostsreport.ui.helper.adapter.OrderAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Expense
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExpensesFragment : Fragment() {

    private var _binding: FragmentExpensesBinding? = null

    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var startDate: Date? = null
    private var endDate: Date? = null

    private val expenses_arraylist = ArrayList<Expense>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textStartDate = binding.textStartDate
        val textEndDate = binding.textEndDate
        val buttonStartDate = binding.buttonStartDate
        val buttonEndDate = binding.buttonEndDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)

        startDate = formatter.parse("$day/${month + 1}/$year") as Date
        endDate = formatter.parse("$day/${month + 1}/$year") as Date
        textStartDate.text = "$day/${month + 1}/$year"
        textEndDate.text = "$day/${month + 1}/$year"

        buttonStartDate.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textStartDate.text = "$mDay/${mMonth + 1}/$mYear"
                    startDate = formatter.parse(textStartDate.text.toString()) as Date
                    expenses_arraylist.clear()
                    onViewCreated(root,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        buttonEndDate.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textEndDate.text = "$mDay/${mMonth + 1}/$mYear"
                    endDate = formatter.parse(textEndDate.text.toString()) as Date
                    expenses_arraylist.clear()
                    onViewCreated(root,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val listViewOrders = binding.listViewExpenses
        val clearExpenses = binding.clearExpenses

        val expenses = dbManager.readFromExpenses

        while (expenses.moveToNext()) {
            val orderDate = formatter.parse(expenses.getString(1)) as Date
            if(startDate == null && endDate == null) {
                expenses_arraylist.add(Expense(expenses.getString(1),expenses.getInt(2), expenses.getDouble(3),
                    expenses.getDouble(4), expenses.getDouble(5), expenses.getInt(0)))
            }
            else if(startDate != null && endDate != null){
                if(orderDate >= startDate && orderDate <= endDate){
                    expenses_arraylist.add(Expense(expenses.getString(1),expenses.getInt(2), expenses.getDouble(3),
                        expenses.getDouble(4), expenses.getDouble(5), expenses.getInt(0)))
                }
            }
            else if (startDate != null){
                if(orderDate >= startDate){
                    expenses_arraylist.add(Expense(expenses.getString(1),expenses.getInt(2), expenses.getDouble(3),
                        expenses.getDouble(4), expenses.getDouble(5), expenses.getInt(0)))
                }
            }
            else if (endDate != null){
                if(orderDate <= endDate) {
                    expenses_arraylist.add(Expense(expenses.getString(1),expenses.getInt(2), expenses.getDouble(3),
                        expenses.getDouble(4), expenses.getDouble(5), expenses.getInt(0)))
                }
            }
        }


        listViewOrders.isClickable = true
        listViewOrders.adapter = ExpenseAdapter(MAIN,expenses_arraylist)

        listViewOrders.setOnItemClickListener { parent, view, position, id ->
            listViewOrders.getItemAtPosition(position)



        }

        clearExpenses.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(MAIN)
            // create dialog box
            dialogBuilder.setMessage("Уверены что хотите удалить все записы затрат ?!")
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton("Да", DialogInterface.OnClickListener {
                        dialog, id ->
                    if(dbManager.clearExpenses()) {
                        listViewOrders.adapter = null
                        expenses_arraylist.clear()
                    }
                    else {
                        MAIN.alert("Что-то пошло не так :(")
                        dialog.cancel()
                    }
                })
                // negative button text and action
                .setNegativeButton("Отменить", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
            val alert = dialogBuilder.create()
            // show alert dialog
            alert.show()
            //alert.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        expenses_arraylist.clear()
        dbManager.closeDb()
    }
}