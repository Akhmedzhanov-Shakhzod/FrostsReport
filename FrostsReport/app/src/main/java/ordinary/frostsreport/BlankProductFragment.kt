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
import ordinary.frostsreport.ui.helper.items.Product


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

        val productNameText = arguments?.getString("product_name").toString()
        val productPriceText = arguments?.getString("product_price").toString()

        name.setText(productNameText)
        price.setText(productPriceText)

        delete.setOnClickListener {
            if(dbManager.deleteProduct(productNameText)){
                MAIN.alert("${productNameText} - удалено",1000)
                MAIN.onProduct()
            }
            else {
                MAIN.alert("Не получилось удалить ${productNameText}",1000)
            }
        }
        save.setOnClickListener {
            if(dbManager.updateProduct(productNameText, Product(name.text.toString(),price.text.toString().toDouble()))){
                MAIN.alert("${productNameText} обновлено на ${name.text}",1000)
                MAIN.onProduct()
            }
            else {
                MAIN.alert("Не получилось обновить ${productNameText} - ${productPriceText} на ${name.text} - ${price.text}",1500)
                name.setText(productNameText)
                price.setText(productPriceText)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }
}