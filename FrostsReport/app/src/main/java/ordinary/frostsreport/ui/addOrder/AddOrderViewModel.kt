package ordinary.frostsreport.ui.addOrder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddOrderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is addOrder Fragment"
    }
    val text: LiveData<String> = _text
}