package ordinary.frostsreport.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ordinary.frostsreport.databinding.FragmentProductsBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ClientAdapter
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
        val product = dbManager.readFromProduct()

        for(i in product.indices){
            products_arrayList.add(Product(i+1, product[i].first,product[i].second))
        }
        binding.listViewProduct.adapter = null
        binding.listViewProduct.isClickable = true
        binding.listViewProduct.adapter = ProductAdapter(MAIN,products_arrayList)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        products_arrayList.clear()
    }
}