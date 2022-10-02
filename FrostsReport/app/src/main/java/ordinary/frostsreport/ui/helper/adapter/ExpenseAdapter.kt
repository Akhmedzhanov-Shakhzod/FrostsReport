package ordinary.frostsreport.ui.helper.adapter

import android.app.Activity
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Expense

class ExpenseAdapter(private val context: Activity, private val expense: ArrayList<Expense>):
    ArrayAdapter<Expense>(context, R.layout.expenses_item, expense) {

    private val dbManager = DbManager(MAIN)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.expenses_item,null)

        val expense_date: TextView = view.findViewById(R.id.expense_date)
        val expense_product: TextView = view.findViewById(R.id.expense_product)
        val expense_amount: TextView = view.findViewById(R.id.expense_amount)

        dbManager.openDb()

        val productName = dbManager.getProduct(expense[position].productId).name

        expense_date.text = expense[position].spendingDate
        expense_product.text = productName
        expense_amount.text = expense[position].spendingAmount.toString()

        dbManager.closeDb()
        return view
    }
}