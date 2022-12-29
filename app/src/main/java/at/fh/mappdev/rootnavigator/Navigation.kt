package at.fh.mappdev.rootnavigator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(navController: NavHostController, startRoute: String){
        //val navController = rememberNavController()
        NavHost(
            navController = navController,
            // startDestination = NavRoutes.Home.route
            startDestination = startRoute
        ) {
            composable(NavRoutes.Home.route){
                // Home Screen
                Connections()
            }

            composable(NavRoutes.Reminder.route) {
                // Reminder
                // LoginUI()
                // NewReminderUI()
                ReminderOverviewUI()
            }

            composable(NavRoutes.NewReminder.route){
                NewReminderUI()
            }

            composable(NavRoutes.Alarm.route) {
                // Alarm
                // RegistrationUIMode()
                AlarmUi()
            }

            composable(NavRoutes.Timetable.route) {
                // Timetable
                TimetableUI()
            }

            composable(NavRoutes.Settings.route) {
                // Settings
                SettingUi()
            }
        }
}