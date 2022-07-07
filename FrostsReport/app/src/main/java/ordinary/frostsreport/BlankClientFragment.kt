package ordinary.frostsreport

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView


class BlankClientFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = view.findViewById<TextView>(R.id.client_id)
        val name = view.findViewById<EditText>(R.id.client_name)

        id.text = arguments?.getString("client_id")
        name.setText(arguments?.getString("client_name"))
    }
}