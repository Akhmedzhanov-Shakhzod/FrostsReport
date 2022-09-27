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

        binding.editNameProduct.requestFocus()
        binding.addPoduct.setOnClickListener {
            val productNameText = binding.editNameProduct.text.toString()
            val productPriceText = binding.editPriceProduct.text.toString()

            val countName = productNameText.trim(' ')
            val countPrice = productPriceText.trim(' ')
            if(countName.isNotEmpty() && countPrice.isNotEmpty()) {
                val iresult = dbManager.insertProductToDb(countName,countPrice.toDouble())

                if (iresult == 0) {
                    MAIN.alert("${countName} - добавлено")
                    binding.editNameProduct.setText("")
                    binding.editPriceProduct.setText("")
                }
                else if (iresult == 1) {
                    MAIN.alert("${countName} - уже существует :)", 1000)
                }
                else if (iresult == 2) {
                    MAIN.alert("Что-то пошло не так :(", 1000)
                }
            }
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
    }
}