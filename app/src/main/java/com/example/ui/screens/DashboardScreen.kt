package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.AcademicGoldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.QualityErrorRed
import com.example.ui.theme.QualityGoodGreen
import com.example.ui.theme.QualityWarningOrange
import com.example.viewmodel.AppNavDestination
import com.example.viewmodel.NutriSmartViewModel

@Composable
fun DashboardScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val qualityAudit by viewModel.qualityAudit.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // University Research Welcome Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = EmeraldPrimaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Policy,
                                contentDescription = null,
                                tint = AcademicGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "University Research Working Group",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                            Text(
                                text = "Food Composition & Affordability Modeling for Bangladesh",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "NutriSmart BD establishes an empirical baseline for nutritional epidemiology and food security economics. All nutrient datasets originate from laboratory assays (INFS/DU FCTB) paired with retail price surveillance across divisional markets.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Live Research Metrics Summary Bar
        item {
            Text(
                text = "SURVEILLANCE & REPOSITORY STATUS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricMiniCard(
                    label = "Total Records",
                    value = "${qualityAudit.totalRecords}",
                    subtext = "FCTB baseline",
                    color = EmeraldPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricMiniCard(
                    label = "Verified",
                    value = "${qualityAudit.verifiedCount}",
                    subtext = "Confirmed",
                    color = QualityGoodGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricMiniCard(
                    label = "Missing Price",
                    value = "${qualityAudit.missingPriceCount}",
                    subtext = "Guarded",
                    color = QualityWarningOrange,
                    modifier = Modifier.weight(1f)
                )
                MetricMiniCard(
                    label = "Audit Flags",
                    value = "${qualityAudit.flaggedForReviewCount}",
                    subtext = "Under review",
                    color = if (qualityAudit.flaggedForReviewCount > 0) QualityErrorRed else QualityGoodGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4 Core Sections Required in User Prompt
        item {
            Text(
                text = "CORE RESEARCH MODULES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // 1. Food Database Card
        item {
            DashboardModuleCard(
                title = "1. Food Composition Database",
                banglaTitle = "খাদ্য উপাদান তথ্যভাণ্ডার",
                description = "Search and inspect lab-tested nutritional compositions across raw, cooked, and prepared Bangladeshi foodstuffs. Supports bilingual English and Bangla script search.",
                icon = Icons.Default.TableChart,
                badge = "${qualityAudit.totalRecords} Entries",
                badgeColor = EmeraldPrimary,
                testTag = "nav_food_database_card",
                onClick = { viewModel.navigateTo(AppNavDestination.DATABASE) }
            )
        }

        // 2. Data Quality Card
        item {
            DashboardModuleCard(
                title = "2. Data Quality & Audit Engine",
                banglaTitle = "উপাত্তের গুণগত মান ও যাচাই",
                description = "Inspect data completeness, audit duplicate Food IDs, review unrecorded nutrients, and track records flagged for laboratory or market re-survey.",
                icon = Icons.Default.ReportProblem,
                badge = "${qualityAudit.flaggedForReviewCount} Flags",
                badgeColor = if (qualityAudit.flaggedForReviewCount > 0) QualityWarningOrange else QualityGoodGreen,
                testTag = "nav_data_quality_card",
                onClick = { viewModel.navigateTo(AppNavDestination.DATA_QUALITY) }
            )
        }

        // 3. Price Calculator Card
        item {
            DashboardModuleCard(
                title = "3. Empirical Price & Unit Calculator",
                banglaTitle = "বাজারমূল্য ও পুষ্টিব্যয় হিসাবক",
                description = "Portion cost calculator and nutrient density economic analysis (BDT per 100 kcal, BDT per 10g protein). Operates strictly when retail market prices and units are confirmed.",
                icon = Icons.Default.Calculate,
                badge = "Strictly Guarded",
                badgeColor = AcademicGold,
                testTag = "nav_price_calc_card",
                onClick = { viewModel.navigateTo(AppNavDestination.PRICE_CALCULATOR) }
            )
        }

        // 4. Nutrition Summary Card
        item {
            DashboardModuleCard(
                title = "4. Nutrition Summary & Formulation",
                banglaTitle = "পুষ্টি সারসংক্ষেপ ও হিসাব",
                description = "Deterministic meal basket totals (energy, protein, carbs, fat, fibre) and total expenditures based on validated laboratory and market price data.",
                icon = Icons.Default.Analytics,
                badge = "Deterministic",
                badgeColor = EmeraldPrimary,
                testTag = "nav_nutrition_summary_card",
                onClick = { viewModel.navigateTo(AppNavDestination.NUTRITION_SUMMARY) }
            )
        }

        // Supporting research actions (CSV import & Source citations)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(AppNavDestination.CSV_IMPORT) }
                        .testTag("nav_csv_import_card")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.UploadFile, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("CSV Upload", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Import & preview external research spreadsheets.", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                    }
                }

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.navigateTo(AppNavDestination.SOURCE_INFO) }
                        .testTag("nav_source_info_card")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = AcademicGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Source Records", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("INFS/DU FCTB, BBS dates, and bibliography.", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp)
                    }
                }
            }
        }

        // Beginner Guide: How to Run and Test
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How to Run & Test This Research Foundation",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    TestingStepItem(
                        number = "1",
                        title = "Test Bilingual Food Database & Search",
                        desc = "Navigate to 'Food Database'. Search 'Rice' or 'ভাত', 'Lentil' or 'ডাল'. Filter by Raw vs Cooked state to observe distinct entries."
                    )
                    TestingStepItem(
                        number = "2",
                        title = "Test Data Quality Audit & 'Data Unavailable' Flags",
                        desc = "Open 'Data Quality'. Notice how records lacking zinc assays or market prices are isolated rather than filled with guess numbers."
                    )
                    TestingStepItem(
                        number = "3",
                        title = "Test Strictly Guarded Price Calculator",
                        desc = "Open 'Price Calculator'. Select 'Parboiled Rice' or 'Chicken Egg' to see portion cost & BDT/100 kcal. Then select 'Boiled Rice (Cooked)' to verify how the calculator safely blocks speculative calculation."
                    )
                    TestingStepItem(
                        number = "4",
                        title = "Test CSV Import with Real-Time Preview",
                        desc = "Go to 'CSV Import' and click 'Load Sample Research CSV' to inspect column mapping and preview warnings before applying."
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MetricMiniCard(
    label: String,
    value: String,
    subtext: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Text(
                text = subtext,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DashboardModuleCard(
    title: String,
    banglaTitle: String,
    description: String,
    icon: ImageVector,
    badge: String,
    badgeColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = banglaTitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = AcademicGold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Open Module",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun TestingStepItem(
    number: String,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(EmeraldPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}
