package ordinary.frostsreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ordinary.frostsreport.databinding.ActivityClientsBinding
import ordinary.frostsreport.ui.helper.adapter.ClientAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client

class Clients : AppCompatActivity() {

    lateinit var binding: ActivityClientsBinding

    private val client_arrayList:ArrayList<Client> = ArrayList()
    private val dbManager = DbManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager.openDb()

        val clients = dbManager.readFromClient()
        val products = dbManager.readFromProduct()

        for(i in clients.indices){
            client_arrayList.add(Client(i,clients.get(i)))
        }

        binding.listViewClient.isClickable = true
        binding.listViewClient.adapter = ClientAdapter(this,client_arrayList)

    }

    fun onClickAddClient(view: View) {

    }
}