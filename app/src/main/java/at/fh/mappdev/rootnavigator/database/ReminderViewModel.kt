package at.fh.mappdev.rootnavigator.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    val allReminders: LiveData<List<ReminderItemRoom>>
    private  val repository: ReminderRepository

    init {
        val reminderDao = ReminderDatabase.getInstace(application).reminderDao()
        repository = ReminderRepository(reminderDao)
        allReminders = repository.AllReminder
    }

    fun addReminder(Reminder: ReminderItemRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.newReminder(Reminder)
        }
    }

    fun updateReminder(Reminder: ReminderItemRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReminder(Reminder)
        }
    }

    fun deleteReminder(Reminder: ReminderItemRoom) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(Reminder)
        }
    }

    // TODO: Implement LiveData into UI -> https://youtu.be/4fzbxnzIJsI?t=233

}