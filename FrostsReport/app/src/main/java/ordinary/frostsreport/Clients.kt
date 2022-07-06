package ordinary.frostsreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ordinary.frostsreport.databinding.FragmentClientsBinding
import ordinary.frostsreport.ui.helper.adapter.ClientAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client

class Clients : AppCompatActivity() {

    lateinit var binding: FragmentClientsBinding

    private val client_arrayList:ArrayList<Client> = ArrayList()
    private val dbManager = DbManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager.openDb()

        val products = dbManager.readFromProduct()

    }

    fun onClickAddClient(view: View) {

    }
}