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

        val clientNameText = arguments?.getString("client_name").toString()
        name.setText(clientNameText)

        delete.setOnClickListener {
            if(dbManager.deleteClient(clientNameText)){
                MAIN.alert("${clientNameText} - удалено",1000)
                MAIN.onClient()
            }
            else {
                MAIN.alert("Не получилось удалить ${arguments?.getString("client_name")}",1000)
            }
        }
        save.setOnClickListener {
            if(dbManager.updateClient(clientNameText,name.text.toString())){
                MAIN.alert("${clientNameText} обновлено на ${name.text}",1000)
                MAIN.onClient()
            }
            else {
                MAIN.alert("Не получилось обновить ${clientNameText} на ${name.text}",1500)
                name.setText(clientNameText)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dbManager.closeDb()
    }
}