package ordinary.frostsreport.ui.helper.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Expense


class ExpenseReportAdapter(private val context: Activity, private val expense: ArrayList<Expense>):
    ArrayAdapter<Expense>(context, R.layout.expenses_report_item, expense) {

    private val dbManager = DbManager(MAIN)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.expenses_report_item,null)

        val expense_date: TextView = view.findViewById(R.id.expense_date)
        val expense_product: TextView = view.findViewById(R.id.expense_product)
        val expense_product_count: TextView = view.findViewById(R.id.expense_product_count)

        val expense_spending_amount: TextView = view.findViewById(R.id.expense_spending_amount)
        val expense_sale_amount: TextView = view.findViewById(R.id.expense_sale_amount)
        val expense_amount: TextView = view.findViewById(R.id.expense_amount)

        dbManager.openDb()

        val productName = dbManager.getProduct(expense[position].productId).name
        val spendAmount = expense[position].spendingAmount
        val count = expense[position].productCount
        val price = expense[position].productPrice
        val saleAmount = price * count
        val amount = saleAmount - spendAmount

        expense_date.text = expense[position].spendingDate
        expense_product.text = productName
        expense_product_count.text = count.toString()

        expense_spending_amount.text = spendAmount.toString()
        expense_sale_amount.text = saleAmount.toString()
        expense_amount.text = amount.toString()

        dbManager.closeDb()
        return view
    }
}