package ordinary.frostsreport.ui.clients

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ordinary.frostsreport.databinding.FragmentClientsBinding
import ordinary.frostsreport.databinding.FragmentOrdersBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ClientAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client

class ClientsFragment : Fragment() {

    private var _binding: FragmentClientsBinding? = null

    private val client_arrayList:ArrayList<Client> = ArrayList()
    private val dbManager = DbManager(MAIN)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbManager.openDb()
        val clients = dbManager.readFromClient()

        for(i in clients.indices){
            client_arrayList.add(Client(i,clients.get(i)))
        }
        binding.listViewClient.isClickable = true
        binding.listViewClient.adapter = ClientAdapter(MAIN,client_arrayList)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}