package hr.algebra.lorena.pocketbotanist.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.repository.PlantRepository
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlantRepository(application)
    private val _refreshStatus = MutableLiveData<String>()
    val refreshStatus: LiveData<String> = _refreshStatus

    fun refreshData() {
        viewModelScope.launch {
            try {
                repository.refreshPlantsFromApi()
                _refreshStatus.postValue(getApplication<Application>().getString(R.string.refresh_success))
            } catch (e: Exception) {
                val errorMessage = getApplication<Application>().getString(R.string.refresh_error, e.message)
                _refreshStatus.postValue(errorMessage)
            }
        }
    }
}