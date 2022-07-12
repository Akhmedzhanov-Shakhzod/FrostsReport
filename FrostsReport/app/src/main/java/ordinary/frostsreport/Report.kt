package ordinary.frostsreport

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ordinary.frostsreport.databinding.FragmentReportBinding
import ordinary.frostsreport.ui.helper.Common
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.adapter.ReportAdapter
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Order
import ordinary.frostsreport.ui.helper.items.OrderProduct
//import ordinary.frostsreport.ui.helper.adapter.PdfReportAdapter
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Report : Fragment() {

    private var binding: FragmentReportBinding? = null

    private val file_name: String = "report.pdf"

    private val dbManager = DbManager(MAIN)
    private val formatter = SimpleDateFormat("dd/MM/yyyy")
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var reportAmount: Double = 0.0
    private val orderSummaryArrayList = ArrayList<Order>()
    private val orderProducts = HashMap<Int,ArrayList<OrderProduct>>()

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

        startDate = formatter.parse("$day/${month + 1}/$year") as Date
        endDate = formatter.parse("$day/${month + 1}/$year") as Date
        textStartDate?.text = "$day/${month + 1}/$year"
        textEndDate?.text = "$day/${month + 1}/$year"

        buttonStartDate?.setOnClickListener {
            val dpd = DatePickerDialog(
                MAIN,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    textStartDate?.text = "$mDay/${mMonth + 1}/$mYear"
                    startDate = formatter.parse(textStartDate?.text.toString()) as Date
                    orderSummaryArrayList.clear()
                    orderProducts.clear()
                    reportAmount = 0.0
                    onViewCreated(binding?.root as View,null)
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
                    orderSummaryArrayList.clear()
                    orderProducts.clear()
                    reportAmount = 0.0
                    onViewCreated(binding?.root as View,null)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showList()
        val eListView = binding?.eListView
        eListView?.setAdapter(ReportAdapter(MAIN,orderSummaryArrayList,orderProducts))

        val uploadPDF = binding?.uploadButton
        Dexter.withActivity(MAIN)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                }
            })
        uploadPDF?.setOnClickListener {
            createPdfFile(Common.getAppPath(MAIN) + file_name)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
        orderSummaryArrayList.clear()
        orderProducts.clear()
        reportAmount = 0.0
    }
    private fun showList() {
        dbManager.openDb()

        val amount = binding?.reportAmount
        val orders = dbManager.readFromOrders

        while (orders.moveToNext()) {
            val orderDate = formatter.parse(orders.getString(1)) as Date
            if(startDate == null && endDate == null) {
                orderSummaryArrayList.add(Order(orders.getString(1),orders.getString(2),
                    orders.getDouble(3),orders.getInt(0)))
                loadOrderProducts(orders.getInt(0))
                reportAmount += orders.getDouble(3)
            }
            else if(startDate != null && endDate != null){
                if(orderDate >= startDate && orderDate <= endDate){
                    orderSummaryArrayList.add(Order(orders.getString(1),orders.getString(2),
                        orders.getDouble(3),orders.getInt(0)))
                    loadOrderProducts(orders.getInt(0))
                    reportAmount += orders.getDouble(3)
                }
            }
            else if (startDate != null){
                if(orderDate >= startDate){
                    orderSummaryArrayList.add(Order(orders.getString(1),orders.getString(2),
                        orders.getDouble(3),orders.getInt(0)))
                    loadOrderProducts(orders.getInt(0))
                    reportAmount += orders.getDouble(3)
                }
            }
            else if (endDate != null){
                if(orderDate <= endDate) {
                    orderSummaryArrayList.add(Order(orders.getString(1),orders.getString(2),
                        orders.getDouble(3),orders.getInt(0)))
                    loadOrderProducts(orders.getInt(0))
                    reportAmount += orders.getDouble(3)
                }
            }
        }
        amount?.text = reportAmount.toString()

        dbManager.closeDb()
    }
    fun loadOrderProducts(orderId: Int) {
        dbManager.openDb()

        orderProducts[orderId] = dbManager.getOrderProducts(orderId)

        dbManager.closeDb()
    }

    fun createPdfFile(path: String) {
        if(File(path).exists()){
            File(path).delete()
        }
        try {
            val document = Document()
            // Save
            PdfWriter.getInstance(document,FileOutputStream(path))
            // Open to write
            document.open()

            // Setting
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("Ahmedjanov Sh. V.")
            document.addCreator("Ahmedjanov Sh. V.")

            // Font setting
            val colorAccent = BaseColor(0,153,204,255)
            val headingFontSize = 20.0f
            val valueFonSize = 26.0f

            // Custom font
            //val fontName = BaseFont.createFont("assets/fonts/brandfom_medium.otf","utf-8",BaseFont.EMBEDDED)

            // Add title to document
            //val titleStyle = Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK)
            addNewItem(document,"Отчет", Element.ALIGN_CENTER)
            //val headingStyle = Font(fontName,headingFontSize,Font.NORMAL,colorAccent)
            addNewItem(document,"Order No:",Element.ALIGN_LEFT)
            //val valueStyle = Font(fontName,valueFonSize,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document,"#123456789",Element.ALIGN_LEFT)

            addLineSeperator(document)
            addNewItem(document,"Order Date:",Element.ALIGN_LEFT)
            addNewItem(document,"11/07/2022",Element.ALIGN_LEFT)
            addLineSeperator(document)
            addNewItem(document,"Account name",Element.ALIGN_LEFT)
            addNewItem(document,"Ahmedjanov",Element.ALIGN_LEFT)

            addLineSeperator(document)
            document.close()
            Toast.makeText(MAIN,"success",Toast.LENGTH_SHORT).show()

            printPdf()
        }
        catch (e:Exception) {
            Log.e("Ahmedjanov"," " + e.message)
        }
    }

    private fun printPdf() {
        val printManager = context?.getSystemService(Context.PRINT_SERVICE) as PrintManager
        try {
//            val printAdapter = PdfReportAdapter(MAIN,Common.getAppPath(MAIN) + file_name)
//            printManager.print("Document",printAdapter,PrintAttributes.Builder().build())
        }
        catch (e:Exception) {
            Log.e("Ahmedjanov"," " + e.message)
        }
    }


    private fun addLineSeperator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int) {
        val chunk = Chunk(text)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }
}