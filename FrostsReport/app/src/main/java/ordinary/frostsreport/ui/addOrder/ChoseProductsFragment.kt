package ordinary.frostsreport.ui.addOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentAddOrderChoseProductsBinding
import ordinary.frostsreport.ui.helper.CHOSENPRODUCTSDATAMODEL
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ProductAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client
import ordinary.frostsreport.ui.helper.items.Product

class ChoseProductsFragment : Fragment() {

    private var _binding: FragmentAddOrderChoseProductsBinding? = null

    private val products_arrayList:ArrayList<Product> = ArrayList()
    private val dbManager = DbManager(MAIN)


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrderChoseProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()
        val product = dbManager.readFromProduct

        while (product.moveToNext()) {
            products_arrayList.add(Product(product.getString(0),product.getString(1).toDouble()))
        }

        binding.listViewChoseProduct.isClickable = true
        binding.listViewChoseProduct.adapter = ProductAdapter(MAIN,products_arrayList)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var chProduct: Pair<Product,Double>
        binding.listViewChoseProduct.setOnItemClickListener { parent, view, position, id ->

            val name:String = products_arrayList[position].name
            val price:Double = products_arrayList[position].price

            val product = Product(name,price)

            val pair = Pair(product,1.0)

            CHOSENPRODUCTSDATAMODEL.product.add(pair)

            findNavController().navigate(R.id.nav_add_order)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
    }
}