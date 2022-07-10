package ordinary.frostsreport.ui.addOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ordinary.frostsreport.R
import ordinary.frostsreport.databinding.FragmentAddOrderChoseClientsBinding
import ordinary.frostsreport.ui.helper.CHOSENPRODUCTSDATAMODEL
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ClientAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client

class ChoseClientFragment : Fragment() {

    private var _binding: FragmentAddOrderChoseClientsBinding? = null

    private val client_arrayList:ArrayList<Client> = ArrayList()
    private val dbManager = DbManager(MAIN)
    private var searchText:String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddOrderChoseClientsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val searchClient = binding.searchClient

        searchClient.addTextChangedListener {
            searchText = searchClient.text.toString()
            binding.listViewChoseClient.adapter = null
            client_arrayList.clear()
            onViewCreated(root,null)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()
        binding.listViewChoseClient.isClickable = true
        val clients = dbManager.readFromClient

        while (clients.moveToNext()) {
            client_arrayList.add(Client(clients.getString(1)))
        }
        if(searchText != null) {
            val tempArrayList = client_arrayList.filter {
                it.name.uppercase().contains(searchText.toString().uppercase())
            }
            binding.listViewChoseClient.adapter = ClientAdapter(MAIN,
                tempArrayList as ArrayList<Client>
            )
        }
        else {
            binding.listViewChoseClient.adapter = ClientAdapter(MAIN,client_arrayList)
        }
        binding.listViewChoseClient.setOnItemClickListener { parent, view, position, id ->

            val name:String = client_arrayList[position].name

            CHOSENPRODUCTSDATAMODEL.client = name

            findNavController().navigate(R.id.nav_add_order)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dbManager.closeDb()
        client_arrayList.clear()
    }
}