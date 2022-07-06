package ordinary.frostsreport.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentAddProductBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null

    private val dbManager = DbManager(MAIN)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()

        binding.addPoduct.setOnClickListener {
            val productNameText = binding.editNameProduct.text.toString()
            val productPriceText = binding.editPriceProduct.text.toString()

            dbManager.inserProductToDb(productNameText,productPriceText.toDouble())

            MAIN.alert("${productNameText} - добавлено")

            binding.editNameProduct.setText("")
            binding.editPriceProduct.setText("")
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
    }
}