package ordinary.frostsreport

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ordinary.frostsreport.databinding.FragmentOrdersBinding
import ordinary.frostsreport.databinding.FragmentReportBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager
import java.text.SimpleDateFormat
import java.util.*

class Report : Fragment() {

    private lateinit var viewModel: ReportViewModel
    private var binding: FragmentReportBinding? = null

    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var startDate: Date? = null
    private var endDate: Date? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReportBinding.inflate(inflater, container, false)

        val textStartDate = binding?.textStartDate
        val textEndDate = binding?.textEndDate
        val buttonStartDate = binding?.buttonStartDate
        val buttonEndDate = binding?.buttonEndDate

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        var hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)


        buttonStartDate?.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textStartDate?.text = "$mDay/${mMonth + 1}/$mYear"
                    startDate = formatter.parse(textStartDate?.text.toString()) as Date
//                    orders_arraylist.clear()
//                    onViewCreated(root,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        buttonEndDate?.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textEndDate?.text = "$mDay/${mMonth + 1}/$mYear"
                    endDate = formatter.parse(textEndDate?.text.toString()) as Date
//                    orders_arraylist.clear()
//                    onViewCreated(root,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

}