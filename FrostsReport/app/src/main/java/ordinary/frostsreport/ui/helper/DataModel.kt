package ordinary.frostsreport.ui.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ordinary.frostsreport.ui.helper.items.Product

open class DataModel: ViewModel(){
    val product: MutableLiveData<Product> by lazy {
        MutableLiveData<Product>()
    }
}