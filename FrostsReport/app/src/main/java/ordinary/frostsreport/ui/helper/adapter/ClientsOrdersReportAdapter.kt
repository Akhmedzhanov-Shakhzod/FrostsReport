package ordinary.frostsreport.ui.helper.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.items.Order
import ordinary.frostsreport.ui.helper.items.OrderProduct

class ClientsOrdersReportAdapter internal constructor(private val context: Activity, private val orderSummaryArrayList: ArrayList<Order>,
                                                      private val orderProducts: HashMap<Int,ArrayList<OrderProduct>>):
    BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        return orderSummaryArrayList.count()
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return orderProducts[orderSummaryArrayList[groupPosition].orderId]?.count() ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return orderSummaryArrayList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return orderProducts[orderSummaryArrayList[groupPosition].orderId]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var conView = convertView
        val orderSummary = getGroup(groupPosition) as Order
        if(convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            conView = inflater.inflate(R.layout.report_client_orders_summary, null)
        }
        val report_order_id = conView!!.findViewById<TextView>(R.id.report_order_id)
        val report_order_amount = conView.findViewById<TextView>(R.id.report_order_amount)
        val report_order_date = conView.findViewById<TextView>(R.id.report_order_date)
        val report_order_is_completed = conView.findViewById<TextView>(R.id.report_order_is_completed)

        report_order_id.text = "№" + orderSummary.orderId.toString()
        report_order_amount.text = orderSummary.amount.toString()
        report_order_date.text = orderSummary.orderDate

        if(orderSummary.isCompleted)
            report_order_is_completed.text = "Выполнено"

        return conView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var conView = convertView
        val orderProduct = getChild(groupPosition, childPosition) as OrderProduct
        if(convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            conView = inflater.inflate(R.layout.report_order_products, null)
        }
        val order_product_name = conView!!.findViewById<TextView>(R.id.order_product_name)
        val order_product_price = conView.findViewById<TextView>(R.id.order_product_price)
        val order_product_count = conView.findViewById<TextView>(R.id.order_product_count)
        val order_product_amount = conView.findViewById<TextView>(R.id.order_product_amount)

        order_product_name.text = orderProduct.product.name
        order_product_price.text =orderProduct.product.price.toString()
        order_product_count.text = orderProduct.productCount.toString()
        order_product_amount.text = orderProduct.productAmount.toString()

        return conView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}