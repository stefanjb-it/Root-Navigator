package at.fh.mappdev.rootnavigator.home

// routes for compose navigation
sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Reminder : NavRoutes("reminder")
    object NewReminder : NavRoutes("new_reminder")
    object Alarm : NavRoutes("alarm")
    object Timetable : NavRoutes("timetable")
    object Settings : NavRoutes("settings")
}