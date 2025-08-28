package hr.algebra.lorena.pocketbotanist.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
                _refreshStatus.postValue("Data refreshed successfully!")
            } catch (e: Exception) {
                _refreshStatus.postValue("Error refreshing data: ${e.message}")
            }
        }
    }
}