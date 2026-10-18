package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.HeaderBanner
import com.example.ui.components.ResearchDisclaimersDialog
import com.example.ui.screens.CsvImportScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DataQualityScreen
import com.example.ui.screens.FoodDatabaseScreen
import com.example.ui.screens.NutritionAnalysisScreen
import com.example.ui.screens.PriceCalculatorScreen
import com.example.ui.screens.SourceInfoScreen
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.NutriSmartTheme
import com.example.ui.theme.QualityWarningOrange
import com.example.viewmodel.AppNavDestination
import com.example.viewmodel.NutriSmartViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NutriSmartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriSmartTheme {
                NutriSmartApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NutriSmartApp(viewModel: NutriSmartViewModel) {
    val currentDestination by viewModel.currentDestination.collectAsState()
    val showDisclaimerModal by viewModel.showDisclaimerModal.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()
    val qualityAudit by viewModel.qualityAudit.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearFeedback()
        }
    }

    // Support Android system back button to return to dashboard
    BackHandler(enabled = currentDestination != AppNavDestination.DASHBOARD) {
        viewModel.navigateTo(AppNavDestination.DASHBOARD)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 700.dp

        Scaffold(
            topBar = {
                HeaderBanner(
                    onOpenDisclaimer = { viewModel.toggleDisclaimerModal(true) }
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    AppBottomNavigationBar(
                        currentDestination = currentDestination,
                        onNavigate = { viewModel.navigateTo(it) },
                        flagCount = qualityAudit.flaggedForReviewCount
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            if (isWideScreen) {
                // Wide / Tablet / Desktop layout with Navigation Rail
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AppNavigationRail(
                        currentDestination = currentDestination,
                        onNavigate = { viewModel.navigateTo(it) },
                        flagCount = qualityAudit.flaggedForReviewCount
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        ActiveScreenContent(
                            destination = currentDestination,
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .widthIn(max = 1000.dp)
                                .align(Alignment.TopCenter)
                        )
                    }
                }
            } else {
                // Handheld Mobile Layout
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    ActiveScreenContent(
                        destination = currentDestination,
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    if (showDisclaimerModal) {
        ResearchDisclaimersDialog(
            onDismiss = { viewModel.toggleDisclaimerModal(false) }
        )
    }
}

@Composable
private fun ActiveScreenContent(
    destination: AppNavDestination,
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    when (destination) {
        AppNavDestination.DASHBOARD -> DashboardScreen(viewModel = viewModel, modifier = modifier)
        AppNavDestination.DATABASE -> FoodDatabaseScreen(viewModel = viewModel, modifier = modifier)
        AppNavDestination.CSV_IMPORT -> CsvImportScreen(viewModel = viewModel, modifier = modifier)
        AppNavDestination.DATA_QUALITY -> DataQualityScreen(viewModel = viewModel, modifier = modifier)
        AppNavDestination.PRICE_CALCULATOR -> PriceCalculatorScreen(viewModel = viewModel, modifier = modifier)
        AppNavDestination.SOURCE_INFO -> SourceInfoScreen(viewModel = viewModel, modifier = modifier)
        AppNavDestination.NUTRITION_SUMMARY -> NutritionAnalysisScreen(viewModel = viewModel, modifier = modifier)
    }
}

private data class NavItemData(
    val destination: AppNavDestination,
    val icon: ImageVector,
    val labelEn: String,
    val hasBadge: Boolean = false
)

private val primaryNavItems = listOf(
    NavItemData(AppNavDestination.DASHBOARD, Icons.Default.Dashboard, "Home"),
    NavItemData(AppNavDestination.DATABASE, Icons.Default.TableChart, "Database"),
    NavItemData(AppNavDestination.DATA_QUALITY, Icons.Default.ReportProblem, "Quality", hasBadge = true),
    NavItemData(AppNavDestination.PRICE_CALCULATOR, Icons.Default.Calculate, "Price Calc"),
    NavItemData(AppNavDestination.NUTRITION_SUMMARY, Icons.Default.Analytics, "Summary"),
    NavItemData(AppNavDestination.CSV_IMPORT, Icons.Default.UploadFile, "CSV"),
    NavItemData(AppNavDestination.SOURCE_INFO, Icons.Default.MenuBook, "Sources")
)

@Composable
private fun AppBottomNavigationBar(
    currentDestination: AppNavDestination,
    onNavigate: (AppNavDestination) -> Unit,
    flagCount: Int
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("app_bottom_nav_bar")
    ) {
        primaryNavItems.forEach { item ->
            val selected = currentDestination == item.destination
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.destination) },
                icon = {
                    if (item.hasBadge && flagCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = QualityWarningOrange,
                                    contentColor = androidx.compose.ui.graphics.Color.White
                                ) {
                                    Text("$flagCount", fontSize = 9.sp)
                                }
                            }
                        ) {
                            Icon(item.icon, contentDescription = item.labelEn)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.labelEn)
                    }
                },
                label = {
                    Text(
                        text = item.labelEn,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EmeraldPrimary,
                    selectedTextColor = EmeraldPrimary,
                    indicatorColor = EmeraldPrimaryContainer
                ),
                modifier = Modifier.testTag("nav_${item.destination.name.lowercase()}")
            )
        }
    }
}

@Composable
private fun AppNavigationRail(
    currentDestination: AppNavDestination,
    onNavigate: (AppNavDestination) -> Unit,
    flagCount: Int
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxHeight()
            .testTag("app_navigation_rail")
    ) {
        primaryNavItems.forEach { item ->
            val selected = currentDestination == item.destination
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigate(item.destination) },
                icon = {
                    if (item.hasBadge && flagCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = QualityWarningOrange,
                                    contentColor = androidx.compose.ui.graphics.Color.White
                                ) {
                                    Text("$flagCount", fontSize = 9.sp)
                                }
                            }
                        ) {
                            Icon(item.icon, contentDescription = item.labelEn)
                        }
                    } else {
                        Icon(item.icon, contentDescription = item.labelEn)
                    }
                },
                label = {
                    Text(
                        text = item.labelEn,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = EmeraldPrimary,
                    selectedTextColor = EmeraldPrimary,
                    indicatorColor = EmeraldPrimaryContainer
                ),
                modifier = Modifier.testTag("rail_${item.destination.name.lowercase()}")
            )
        }
    }
}
