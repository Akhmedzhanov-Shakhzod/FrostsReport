package ordinary.frostsreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager


class BlankProductFragment : Fragment() {

    private val dbManager = DbManager(MAIN)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank_product, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val delete = view.findViewById<Button>(R.id.delete_product)
        val save = view.findViewById<Button>(R.id.save_product)

        val name = view.findViewById<EditText>(R.id.product_name)
        val price = view.findViewById<EditText>(R.id.product_price)

        name.setText(arguments?.getString("product_name"))
        price.setText(arguments?.getString("product_price"))

        delete.setOnClickListener {
            val d = dbManager.deleteProduct(arguments?.getString("product_name").toString())
            if(d == -1){
                MAIN.alert("Не получилось удалить ${arguments?.getString("product_name")}",1000)
            }
            else {
                MAIN.alert("${arguments?.getString("product_name")} - удалено",1000)
                MAIN.onProduct()
            }
        }
        save.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }
}