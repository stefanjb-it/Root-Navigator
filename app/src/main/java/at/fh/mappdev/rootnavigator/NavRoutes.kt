package at.fh.mappdev.rootnavigator

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Reminder : NavRoutes("reminder")
    object Alarm : NavRoutes("alarm")
    object Timetable : NavRoutes("timetable")
    object Settings : NavRoutes("settings")
}