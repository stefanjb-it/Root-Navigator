package at.fh.mappdev.rootnavigator.bars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import at.fh.mappdev.rootnavigator.bars.BarItem
import at.fh.mappdev.rootnavigator.R

@Composable
fun TopBar(navController: NavHostController, bottomBarState: MutableState<Boolean>, topBarState: MutableState<Boolean>){
    val settingItem = BarItem(
        title = "Settings",
        image = Icons.Outlined.Settings,
        route = "settings"
    )

    AnimatedVisibility(
        visible = topBarState.value,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        content = {
            CenterTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "/",
                            color = MaterialTheme.colors.secondary,
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier
                                .offset(y = 2.dp)
                        )
                        Text(
                            text = "Navigator",
                            //color = MaterialTheme.colors.primary,
                            color = MaterialTheme.colors.surface,
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier
                                .offset(y = 2.dp)
                        )
                    }
                },

                backgroundColor = MaterialTheme.colors.primary,
                //backgroundColor = MaterialTheme.colors.primaryVariant,
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_no_text),
                        contentDescription = "Logo",
                        modifier = Modifier.size(48.dp)
                    )
                },
                actions = {
                    /* Logout Button
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }
                    */

                    IconButton(
                        onClick = {
                            navController.navigate(settingItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = settingItem.image,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                }
            )

        }
    )
}