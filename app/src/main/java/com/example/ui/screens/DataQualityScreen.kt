package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodItem
import com.example.model.VerificationStatus
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.QualityErrorRed
import com.example.ui.theme.QualityGoodGreen
import com.example.ui.theme.QualityWarningOrange
import com.example.viewmodel.AppNavDestination
import com.example.viewmodel.NutriSmartViewModel

@Composable
fun DataQualityScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val qualityAudit by viewModel.qualityAudit.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Rule,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Data Integrity & Verification Engine",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Real-time auditing across the 30 Bangladeshi food candidates. Checks for primary key collisions (duplicate food IDs), unverified entries, demonstration records, and unpriced items.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Summary Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AuditMetricCard(
                    title = "Verified",
                    count = "${qualityAudit.verifiedCount}",
                    color = QualityGoodGreen,
                    icon = Icons.Default.Verified,
                    modifier = Modifier.weight(1f)
                )

                AuditMetricCard(
                    title = "Duplicate IDs",
                    count = "${qualityAudit.duplicateIdCount}",
                    color = if (qualityAudit.duplicateIdCount > 0) QualityErrorRed else QualityGoodGreen,
                    icon = Icons.Default.ContentCopy,
                    modifier = Modifier.weight(1f)
                )

                AuditMetricCard(
                    title = "Missing Price",
                    count = "${qualityAudit.missingPriceCount}",
                    color = QualityWarningOrange,
                    icon = Icons.Default.PriceCheck,
                    modifier = Modifier.weight(1f)
                )

                AuditMetricCard(
                    title = "Missing Nutrient",
                    count = "${qualityAudit.missingNutrientCount}",
                    color = AcademicGold,
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Duplicate Food IDs Section
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (qualityAudit.duplicateIdCount > 0) QualityErrorRed else QualityGoodGreen.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (qualityAudit.duplicateIdCount > 0) Icons.Default.ReportProblem else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (qualityAudit.duplicateIdCount > 0) QualityErrorRed else QualityGoodGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Duplicate Food ID Validation",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = if (qualityAudit.duplicateIdCount > 0) "COLLISION DETECTED" else "NO DUPLICATES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (qualityAudit.duplicateIdCount > 0) QualityErrorRed else QualityGoodGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (qualityAudit.duplicateIdCount > 0) {
                        Text(
                            text = "Warning: The following Food IDs appear more than once in the dataset:",
                            style = MaterialTheme.typography.bodySmall,
                            color = QualityErrorRed
                        )
                        qualityAudit.duplicateIds.forEach { id ->
                            Text("• Food ID: $id", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    } else {
                        Text(
                            text = "All ${qualityAudit.totalRecords} records have unique Food IDs (F001 to F030). No database primary key collisions found.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Strict Verification Audit Rules Panel
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = AcademicGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Strict Verification Standards",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Research Rule: Do not label a record 'Verified' unless its source, unit (g), food state, and price date have all been confirmed. If any of these 4 criteria are unconfirmed, the record is flagged as 'Unverified' or 'Requires Review'. Demonstration records are clearly marked.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DetailProgressItem(
                        label = "Fully Verified Records",
                        countText = "${qualityAudit.verifiedCount} / ${qualityAudit.totalRecords}",
                        percentage = if (qualityAudit.totalRecords > 0) qualityAudit.verifiedCount.toFloat() / qualityAudit.totalRecords else 1f,
                        color = QualityGoodGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailProgressItem(
                        label = "Market Price Coverage",
                        countText = "${qualityAudit.totalRecords - qualityAudit.missingPriceCount} / ${qualityAudit.totalRecords}",
                        percentage = if (qualityAudit.totalRecords > 0) (qualityAudit.totalRecords - qualityAudit.missingPriceCount).toFloat() / qualityAudit.totalRecords else 1f,
                        color = EmeraldPrimary
                    )
                }
            }
        }

        // Section: Records Requiring Review
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECORDS REQUIRING ATTENTION (${qualityAudit.flaggedForReviewCount})",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.secondary
                )

                OutlinedButton(
                    onClick = { viewModel.resetDatabaseToBaseline() },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Restore Baseline (30)", fontSize = 11.sp)
                }
            }
        }

        items(qualityAudit.recordsRequiringReview) { item ->
            FlaggedItemCard(
                item = item,
                onClick = {
                    viewModel.selectItemForDetail(item)
                    viewModel.navigateTo(AppNavDestination.DATABASE)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AuditMetricCard(
    title: String,
    count: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
            Text(text = title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), maxLines = 1)
        }
    }
}

@Composable
private fun DetailProgressItem(
    label: String,
    countText: String,
    percentage: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            Text(text = countText, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.LinearProgressIndicator(
            progress = { percentage.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun FlaggedItemCard(
    item: FoodItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, QualityWarningOrange.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.foodId} • ${item.foodNameEn}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QualityWarningOrange.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.verificationStatus.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = QualityWarningOrange
                    )
                }
            }

            Text(
                text = "${item.foodNameBn} (${item.foodState})",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = AcademicGold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.notes ?: if (!item.isPriceValid()) "Missing retail market price data." else "One or more nutrition fields missing.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tap to view in Food Database →",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = EmeraldPrimary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
