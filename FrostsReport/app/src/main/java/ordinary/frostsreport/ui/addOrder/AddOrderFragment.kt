package ordinary.frostsreport.ui.addOrder

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentAddOrderBinding
import ordinary.frostsreport.ui.helper.CHOSENPRODUCTSDATAMODEL
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ChoseProductAdapter
import ordinary.frostsreport.ui.helper.adapter.ClientAdapter

class AddOrderFragment : Fragment() {

    private var _binding: FragmentAddOrderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textData = binding.textDate
        val buttonDate = binding.buttonDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)

        textData.text = "$day/${month+1}/$year"

        buttonDate.setOnClickListener {
            val dpd = DatePickerDialog(MAIN,DatePickerDialog.OnDateSetListener{
                view,mYear,mMonth,mDay ->
                textData.text = "$mDay/${mMonth+1}/$mYear"
            },year,month,day)
            dpd.show()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var amount:Double = 0.00

        val amoutText = binding.amountText
        val choseShop = binding.choseShop
        val choseProduct = binding.choseProduct
        val addOrder = binding.addOrderButton
        val clear = binding.clear

        val clientNameText = CHOSENPRODUCTSDATAMODEL.client
        choseShop.text = clientNameText

        binding.listChosenViewProduct.isClickable = true

        if(CHOSENPRODUCTSDATAMODEL.product.isNotEmpty()){
            binding.listChosenViewProduct.adapter = ChoseProductAdapter(MAIN,CHOSENPRODUCTSDATAMODEL.product)
            for (i in 0 until CHOSENPRODUCTSDATAMODEL.product.count()){
                amount += CHOSENPRODUCTSDATAMODEL.productAmount[i]
            }
        }
        binding.listChosenViewProduct.setOnItemClickListener { parent, view, position, id ->
            val bundle = Bundle()

            val name:String = CHOSENPRODUCTSDATAMODEL.product[position].first.name
            val price:Double = CHOSENPRODUCTSDATAMODEL.product[position].first.price
            val count:Double = CHOSENPRODUCTSDATAMODEL.product[position].second

            bundle.putString("product_name",name)
            bundle.putString("product_price",price.toString())
            bundle.putString("product_count",count.toString())

            findNavController().navigate(R.id.productFromAddOrderBlankFragment,bundle)
        }

        amoutText.text = amount.toString()
        choseShop.setOnClickListener {
            findNavController().navigate(R.id.choseClientFragment)
        }

        choseProduct.setOnClickListener {
            findNavController().navigate(R.id.choseProductsFragment)
        }
        addOrder.setOnClickListener {
            if(choseShop.text.isNotEmpty() && CHOSENPRODUCTSDATAMODEL.product.isNotEmpty()){
                CHOSENPRODUCTSDATAMODEL.client = ""
                CHOSENPRODUCTSDATAMODEL.product.clear()
                CHOSENPRODUCTSDATAMODEL.productAmount.clear()


                findNavController().navigate(R.id.nav_add_order)
            }
            else if (choseShop.text.isEmpty()){
                MAIN.alert("Выберите магазин :) ",1000)
            }
            else {
                MAIN.alert("Выберите хотя бы один продукт :) ",1000)
            }
        }

        clear.setOnClickListener {
            CHOSENPRODUCTSDATAMODEL.client = ""
            CHOSENPRODUCTSDATAMODEL.product.clear()
            CHOSENPRODUCTSDATAMODEL.productAmount.clear()

            findNavController().navigate(R.id.nav_add_order)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


