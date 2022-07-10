package ordinary.frostsreport.ui.helper.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ordinary.frostsreport.R
import ordinary.frostsreport.ui.helper.items.Client
import ordinary.frostsreport.ui.helper.items.Product

class ClientAdapter(private val context: Activity, private val clients: List<Client>):
    ArrayAdapter<Client>(context, R.layout.client_item, clients) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.client_item,null)

        val client_name: TextView = view.findViewById(R.id.client_name)

        client_name.text = clients[position].name
        return view
    }

}