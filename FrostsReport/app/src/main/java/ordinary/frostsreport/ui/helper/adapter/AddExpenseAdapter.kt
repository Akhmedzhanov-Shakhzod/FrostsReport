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


class AddExpenseAdapter(private val context: Activity, private val expense: ArrayList<Expense>):
    ArrayAdapter<Expense>(context, R.layout.add_expenses_item, expense) {

    private val dbManager = DbManager(MAIN)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.add_expenses_item,null)

        val expense_product: TextView = view.findViewById(R.id.expense_product)
        val expense_product_count: TextView = view.findViewById(R.id.expense_product_count)
        val expense_product_price: TextView = view.findViewById(R.id.expense_product_price)
        val expense_amount: EditText = view.findViewById(R.id.expense_amount)

        dbManager.openDb()

        val productName = dbManager.getProduct(expense[position].productId).name

        expense_product.text = productName
        expense_product_count.text = expense[position].productCount.toString()
        expense_product_price.text = expense[position].productPrice.toString()

        expense_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.length != 0) expense[position].spendingAmount = expense_amount.text.toString().toDouble()
            }
        })

        dbManager.closeDb()
        return view
    }
}