package at.fh.mappdev.rootnavigator.home

import android.app.AlarmManager
import android.content.SharedPreferences
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import at.fh.mappdev.rootnavigator.bars.BottomBar
import at.fh.mappdev.rootnavigator.bars.TopBar

// implements screen layout
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyScaffold(preferences: SharedPreferences, alarmManager: AlarmManager){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val navController = rememberNavController()

    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    Scaffold (
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState) },
        content = { padding -> Column(modifier = Modifier.padding(padding))
        {NavigationHost(navController = navController, alarmManager, preferences, bottomBarState, topBarState)} },
        bottomBar = { BottomBar(navController = navController, bottomBarState = bottomBarState, topBarState = topBarState, preferences = preferences) }
    )
}