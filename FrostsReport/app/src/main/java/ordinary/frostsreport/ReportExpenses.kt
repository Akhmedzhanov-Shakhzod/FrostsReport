package ordinary.frostsreport

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ordinary.frostsreport.databinding.FragmentExpensesReportBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ExpenseReportAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Expense
import java.text.SimpleDateFormat
import java.util.*

class ReportExpenses : Fragment() {

    private var binding: FragmentExpensesReportBinding? = null

    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var reportExpenseAmount: Double = 0.0
    private var reportSaleAmount: Double = 0.0
    private var reportAmount: Double = 0.0
    private val expenseArrayList = ArrayList<Expense>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExpensesReportBinding.inflate(inflater, container, false)

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

        startDate = formatter.parse("$day/${month + 1}/$year") as Date
        endDate = formatter.parse("$day/${month + 1}/$year") as Date
        textStartDate?.text = "$day/${month + 1}/$year"
        textEndDate?.text = "$day/${month + 1}/$year"
        
        buttonStartDate?.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textStartDate?.text = "$mDay/${mMonth + 1}/$mYear"
                    startDate = formatter.parse(textStartDate?.text.toString()) as Date
                    expenseArrayList.clear()
                    reportAmount = 0.0
                    reportSaleAmount = 0.0
                    reportExpenseAmount = 0.0
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
                    expenseArrayList.clear()
                    reportAmount = 0.0
                    reportSaleAmount = 0.0
                    reportExpenseAmount = 0.0
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

        val reportSaleAmountView = binding?.reportSaleAmount
        val reportSpendingAmountView = binding?.reportSpendingAmount
        val reportAmountView = binding?.reportAmount

        showList()
        val listViewExpensesReport = binding?.listViewExpensesReport
        listViewExpensesReport?.adapter = ExpenseReportAdapter(MAIN, expenseArrayList)

        expenseArrayList.forEach{   expense ->
            val spendAmount = expense.spendingAmount
            val count = expense.productCount
            val price = expense.productPrice
            val saleAmount = price * count
            val amount = saleAmount - spendAmount

            reportSaleAmount += saleAmount
            reportExpenseAmount += spendAmount
            reportAmount += amount
        }

        reportSaleAmountView?.text = reportSaleAmount.toString()
        reportSpendingAmountView?.text = reportExpenseAmount.toString()
        reportAmountView?.text = reportAmount.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
        expenseArrayList.clear()
        reportAmount = 0.0
        reportSaleAmount = 0.0
        reportExpenseAmount = 0.0
    }
    private fun showList() {
        dbManager.openDb()

        val expense = dbManager.readFromExpenses

        while (expense.moveToNext()) {
            val orderDate = formatter.parse(expense.getString(1)) as Date
            val tempExpense: Expense
            if(startDate == null && endDate == null) {
                tempExpense = Expense(expense.getString(1),expense.getInt(2), expense.getDouble(3),
                    expense.getDouble(4),expense.getDouble(5), expense.getInt(0))

                var isNotExist = true
                expenseArrayList.forEach{ expense ->
                    if(tempExpense.productId == expense.productId && tempExpense.spendingDate == expense.spendingDate && tempExpense.productPrice == expense.productPrice){
                        expense.productCount += tempExpense.productCount
                        expense.spendingAmount += tempExpense.spendingAmount
                        isNotExist = false
                    }
                }
                if(isNotExist){
                    expenseArrayList.add(tempExpense)
                }
            }
            else if(startDate != null && endDate != null){
                if(orderDate >= startDate && orderDate <= endDate){
                    tempExpense = Expense(expense.getString(1),expense.getInt(2), expense.getDouble(3),
                        expense.getDouble(4),expense.getDouble(5), expense.getInt(0))

                    var isNotExist = true
                    expenseArrayList.forEach{ expense ->
                        if(tempExpense.productId == expense.productId && tempExpense.spendingDate == expense.spendingDate && tempExpense.productPrice == expense.productPrice){
                            expense.productCount += tempExpense.productCount
                            expense.spendingAmount += tempExpense.spendingAmount
                            isNotExist = false
                        }
                    }
                    if(isNotExist){
                        expenseArrayList.add(tempExpense)
                    }
                }
            }
            else if (startDate != null){
                if(orderDate >= startDate){
                    tempExpense = Expense(expense.getString(1),expense.getInt(2), expense.getDouble(3),
                        expense.getDouble(4),expense.getDouble(5), expense.getInt(0))

                    var isNotExist = true
                    expenseArrayList.forEach{ expense ->
                        if(tempExpense.productId == expense.productId && tempExpense.spendingDate == expense.spendingDate && tempExpense.productPrice == expense.productPrice){
                            expense.productCount += tempExpense.productCount
                            expense.spendingAmount += tempExpense.spendingAmount
                            isNotExist = false
                        }
                    }
                    if(isNotExist){
                        expenseArrayList.add(tempExpense)
                    }
                }
            }
            else if (endDate != null){
                if(orderDate <= endDate) {
                    tempExpense = Expense(expense.getString(1),expense.getInt(2), expense.getDouble(3),
                        expense.getDouble(4),expense.getDouble(5), expense.getInt(0))

                    var isNotExist = true
                    expenseArrayList.forEach{ expense ->
                        if(tempExpense.productId == expense.productId && tempExpense.spendingDate == expense.spendingDate && tempExpense.productPrice == expense.productPrice){
                            expense.productCount += tempExpense.productCount
                            expense.spendingAmount += tempExpense.spendingAmount
                            isNotExist = false
                        }
                    }
                    if(isNotExist){
                        expenseArrayList.add(tempExpense)
                    }
                }
            }
        }

        dbManager.closeDb()
    }
}