package at.fh.mappdev.rootnavigator.home

import android.app.AlarmManager
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import at.fh.mappdev.rootnavigator.alarm.AlarmUi
import at.fh.mappdev.rootnavigator.SettingUi
import at.fh.mappdev.rootnavigator.TimetableUI
import at.fh.mappdev.rootnavigator.hafas.Connections
import at.fh.mappdev.rootnavigator.reminder.NewReminderUI
import at.fh.mappdev.rootnavigator.reminder.ReminderOverviewUI

@Composable
fun NavigationHost(navController: NavHostController, alarmManager: AlarmManager, preferences : SharedPreferences, bottomBarState: MutableState<Boolean>, topBarState:MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route){
            Connections(preferences = preferences, bottomBarState, topBarState)
        }

        composable(NavRoutes.Reminder.route) {
            ReminderOverviewUI(navController)
        }

        composable(NavRoutes.NewReminder.route){
            NewReminderUI(navController, alarmManager, preferences, bottomBarState)
        }

        composable(NavRoutes.Alarm.route) {
            AlarmUi(alarmManager = alarmManager, preferences = preferences)
        }

        composable(NavRoutes.Timetable.route) {
            TimetableUI()
        }

        composable(NavRoutes.Settings.route) {
            SettingUi(navController, preferences)
        }
    }
}