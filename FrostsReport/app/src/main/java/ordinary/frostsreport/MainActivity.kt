package ordinary.frostsreport

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ordinary.frostsreport.databinding.ActivityMainBinding
import ordinary.frostsreport.ui.helper.MAIN
import ordinary.frostsreport.ui.helper.db.DbManager
import ordinary.frostsreport.ui.helper.items.Product

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    lateinit var navController: NavController

    private val dbManager = DbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MAIN = this
        navController = Navigation.findNavController(this,R.id.nav_host_fragment_content_main)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_add_order, R.id.nav_clients, R.id.nav_products, R.id.nav_orders, R.id.nav_report
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

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }
//////////////////////////////////////////////////////////////////////////////////////
    fun onClickAddClient(view:View){
        navController.navigate(R.id.action_nav_clients_to_nav_add_client)
    }
    fun onClickAddOrder(view:View){
        navController.navigate(R.id.action_nav_orders_to_nav_add_order)
    }
    fun onClickAddProduct(view:View){
        navController.navigate(R.id.action_nav_products_to_addProductFragment)
    }
    fun onClient(){
        navController.navigate(R.id.action_blankClientFragment_to_nav_clients)
    }
    fun onProduct(){
        navController.navigate(R.id.action_blankProductFragment_to_nav_products)
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    fun alert(text:String, delay:Long = 500){
    val dialogBuilder = AlertDialog.Builder(this)
    // create dialog box
    val alert = dialogBuilder.create()
    alert.setMessage(text)
    alert.setOnShowListener { d ->
        Thread.sleep(delay)
        d.cancel()
    }
    // show alert dialog
    alert.show()
    //alert.cancel()
    }
}