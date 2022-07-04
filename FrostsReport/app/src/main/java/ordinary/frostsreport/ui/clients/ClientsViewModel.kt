package ordinary.frostsreport.ui.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ClientsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is clients Fragment"
    }
    val text: LiveData<String> = _text
}