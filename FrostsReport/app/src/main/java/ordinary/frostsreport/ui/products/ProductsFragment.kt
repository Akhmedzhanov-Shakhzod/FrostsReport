package ordinary.frostsreport.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentProductsBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ProductAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client
import ordinary.frostsreport.ui.helper.items.Product

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null

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
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()
        val product = dbManager.readFromProduct

        while (product.moveToNext()) {
            products_arrayList.add(Product(product.getString(0),product.getString(1).toDouble()))
        }

        binding.listViewProduct.isClickable = true
        binding.listViewProduct.adapter = ProductAdapter(MAIN,products_arrayList)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()

        binding.listViewProduct.setOnItemClickListener { parent, view, position, id ->

            val name:String = products_arrayList[position].name
            val price:Double = products_arrayList[position].price

            bundle.putString("product_name", name)
            bundle.putString("product_price", price.toString())

            findNavController().navigate(R.id.blankProductFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        products_arrayList.clear()
    }
}