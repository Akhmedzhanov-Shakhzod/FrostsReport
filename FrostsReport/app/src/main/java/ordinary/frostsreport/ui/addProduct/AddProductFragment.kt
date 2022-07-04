package ordinary.frostsreport.ui.addProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ordinary.frostsreport.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null

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

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}