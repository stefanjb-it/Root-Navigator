package at.fh.mappdev.rootnavigator.database

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderViewModel(application : Application) : AndroidViewModel(application), ReminderModel {
    // the list of Reminders
    override val reminders = MutableLiveData<List<ReminderItemRoom>>()

    // refresh Data
    override val refreshReminder: () -> Unit = { viewModelScope.launch {
        withContext(Dispatchers.IO) {
            ReminderRepository.getReminders(getApplication())
        }
    }
        localRefresh()
    }

    val localRefresh: () -> Unit = {
        reminders.value = ReminderRepository.allReminders
    }
}

interface ReminderModel{
    val reminders : LiveData<List<ReminderItemRoom>>
    val refreshReminder : ()->Unit
}