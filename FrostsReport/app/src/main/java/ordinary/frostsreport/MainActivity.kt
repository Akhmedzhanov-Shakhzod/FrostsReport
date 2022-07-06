package ordinary.frostsreport

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ordinary.frostsreport.databinding.ActivityMainBinding
import ordinary.frostsreport.ui.helper.db.DbManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    private val dbManager = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_main)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_add_order, R.id.nav_clients, R.id.nav_products, R.id.nav_orders
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()

        dbManager.openDb()

//        var temporary = findViewById<TextView>(R.id.tempory)
//        temporary.text = ""
//        val dataList = dbManager.readFromProduct()
//        for (item in dataList){
//            temporary.append(item.first)
//            temporary.append("\t")
//            temporary.append(item.second.toString())
//            temporary.append("\n")
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }
//////////////////////////////////////////////////////////////////////////////////////
    fun onClickAddClient(view:View){
        //navController.navigate(R.id.action_nav_clients_to_addClientFragment)
    }

    fun onClickAddOrder(view:View){
        navController.navigate(R.id.action_nav_orders_to_nav_add_order)
    }

    fun onClickAddProduct(view:View){
        navController.navigate(R.id.action_nav_products_to_addProductFragment)
        val intent = Intent(this, Clients::class.java)
        startActivity(intent)
    }
///////////////////////////////////////////////////////////////////////////////////////
    fun addClient(view: View){

        val clientName = findViewById<EditText>(R.id.add_name_client)
        val clientNameText = clientName.text.toString()
        dbManager.inserClientToDb(clientNameText)

        alert("${clientNameText} - добавлено")

        clientName.setText("")
    }
    fun addPoduct(view: View){
        val productName = findViewById<EditText>(R.id.add_name_product)
        val productPrice = findViewById<EditText>(R.id.edit_price_product)

        val productNameText = productName.text.toString()
        val productPriceText = productPrice.text.toString()

        dbManager.inserProductToDb(productNameText,productPriceText.toDouble())

        alert("${productNameText} - добавлено")

        productName.setText("")
        productPrice.setText("")
    }
///////////////////////////////////////////////////////////////////////////////////////
    fun alert(text:String){
    val dialogBuilder = AlertDialog.Builder(this)
    // create dialog box
    val alert = dialogBuilder.create()
    alert.setMessage(text)
    alert.setOnShowListener { d ->
        Thread.sleep(500)
        d.cancel()
    }
    // show alert dialog
    alert.show()
    //alert.cancel()
    }
}