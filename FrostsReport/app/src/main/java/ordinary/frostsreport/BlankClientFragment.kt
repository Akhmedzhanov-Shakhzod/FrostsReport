package ordinary.frostsreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Client


class BlankClientFragment : Fragment() {

    private val dbManager = DbManager(MAIN)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbManager.openDb()

        val delete = view.findViewById<Button>(R.id.delete_client)
        val save = view.findViewById<Button>(R.id.save_client)

        val name = view.findViewById<EditText>(R.id.client_name)

        name.setText(arguments?.getString("client_name"))

        delete.setOnClickListener {
            val d = dbManager.deleteClient(arguments?.getString("client_name").toString())
            if(d == -1){
                MAIN.alert("Не получилось удалить ${arguments?.getString("client_name")}",1000)
            }
            else {
                MAIN.alert("${arguments?.getString("client_name")} - удалено",1000)
                MAIN.onClient()
            }
        }
        save.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }
}