package ordinary.frostsreport.ui.clients

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentClientsBinding
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
        val clients = dbManager.readFromClient

        while (clients.moveToNext()) {
            client_arrayList.add(Client(clients.getString(1)))
        }
        binding.listViewClient.isClickable = true
        binding.listViewClient.adapter = ClientAdapter(MAIN,client_arrayList)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()

        binding.listViewClient.setOnItemLongClickListener { parent, view, position, id ->

            val name:String = client_arrayList[position].name

            bundle.putString("client_name", name)

            findNavController().navigate(R.id.blankClientFragment, bundle)
            return@setOnItemLongClickListener true
        }

        binding.listViewClient.setOnItemClickListener { parent, view, position, id ->
            val name:String = client_arrayList[position].name

            bundle.putString("client_name", name)

            findNavController().navigate(R.id.report_clients_orders, bundle)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        client_arrayList.clear()
    }
}