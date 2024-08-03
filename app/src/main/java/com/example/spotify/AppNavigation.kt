package com.example.spotify

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotify.ui.screens.HomeScreen
import com.example.spotify.ui.screens.TrackDetailsScreen
import com.example.spotify.viewmodel.TrackViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val trackViewModel: TrackViewModel = hiltViewModel() // Shared ViewModel instance

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, trackViewModel)
        }
        composable("trackDetails/{trackId}") { backStackEntry ->
            val trackId = backStackEntry.arguments?.getString("trackId")
            TrackDetailsScreen(trackId, trackViewModel)
        }
    }
}
