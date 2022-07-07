package ordinary.frostsreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import ordinary.frostsreport.databinding.FragmentBlankProductBinding
import ordinary.frostsreport.ui.helper.DataModel
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.PRODUCTBLANK


class BlankProductFragment : Fragment() {


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

        val id = view.findViewById<TextView>(R.id.product_id)
        val name = view.findViewById<EditText>(R.id.product_name)
        val price = view.findViewById<EditText>(R.id.product_price)

        id.text = arguments?.getString("product_id")
        name.setText(arguments?.getString("product_name"))
        price.setText(arguments?.getString("product_price"))
    }
}