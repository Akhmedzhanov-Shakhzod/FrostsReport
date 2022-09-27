package ordinary.frostsreport.ui.addClient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ordinary.frostsreport.databinding.FragmentAddClientBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager

class AddClientFragment : Fragment() {

    private var _binding: FragmentAddClientBinding? = null

    private val dbManager = DbManager(MAIN)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentAddClientBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()

        binding.addNameClient.requestFocus()
        binding.addClient.setOnClickListener {
            val clientNameText = binding.addNameClient.text.toString()
            val count = clientNameText.trim(' ')
            if(count.isNotEmpty()) {
                val iresult = dbManager.insertClientToDb(count)

                if (iresult == 0) {
                    MAIN.alert("${count} - добавлено")
                    binding.addNameClient.setText("")
                }
                else if (iresult == 1) {
                    MAIN.alert("${count} - уже существует :)", 1000)
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