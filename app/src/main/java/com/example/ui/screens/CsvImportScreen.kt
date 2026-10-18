package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
import com.example.viewmodel.NutriSmartViewModel

@Composable
fun CsvImportScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val csvText by viewModel.csvInputText.collectAsState()
    val previewItems by viewModel.csvPreviewItems.collectAsState()
    val warnings by viewModel.csvWarnings.collectAsState()

    var showConfirmReplaceDialog by remember { mutableStateOf(false) }

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
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "30-Item Research CSV Staging Engine",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Import and validate CSV datasets adhering strictly to the 21-column schema (food_id, food_name_en, food_name_bn, food_group, food_state, energy_kcal_100g, protein_g_100g, carbs_g_100g, fat_g_100g, fibre_g_100g, price_bdt, price_unit_g, price_date, price_location, nutrition_source, nutrition_page_or_table, price_source, price_report_date, price_unit_description, verification_status, notes).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Action Buttons: Load Template or Benchmark
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.loadEmptyTemplateCsv() },
                        colors = ButtonDefaults.buttonColors(containerColor = AcademicGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("load_empty_template_button")
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Load 30-Candidate Template", fontSize = 11.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.loadPopulatedBenchmarkCsv() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("load_populated_benchmark_button")
                    ) {
                        Icon(Icons.Default.DataObject, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Load Populated FCTB (30)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.setCsvInputText("") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Text Area", fontSize = 12.sp)
                }
            }
        }

        // CSV Text Input Area
        item {
            OutlinedTextField(
                value = csvText,
                onValueChange = { viewModel.setCsvInputText(it) },
                label = { Text("CSV Text (paste your dataset here)") },
                placeholder = {
                    Text(
                        "Paste CSV matching the 21-column schema here...",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("csv_input_textfield"),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }

        // Parse Button
        item {
            Button(
                onClick = { viewModel.parseCurrentCsv() },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("parse_csv_button"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Inspect & Parse CSV Records", fontWeight = FontWeight.Bold)
            }
        }

        // Warnings or validation notices
        if (warnings.isNotEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = QualityWarningOrange)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Validation & Integrity Warnings", fontWeight = FontWeight.Bold, color = QualityWarningOrange, fontSize = 12.sp)
                        }
                        warnings.forEach { w ->
                            Text("• $w", fontSize = 11.sp, color = Color.Black)
                        }
                    }
                }
            }
        }

        // Import Preview Section
        if (previewItems.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = QualityGoodGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Preview: ${previewItems.size} Records Staged",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Commit Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.commitCsvImport(replaceExisting = false) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("commit_merge_csv_button")
                            ) {
                                Text("Merge with DB", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { showConfirmReplaceDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = QualityErrorRed),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("commit_replace_csv_button")
                            ) {
                                Text("Replace Entire DB", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Preview Table of Parsed Records
            item {
                Text(
                    text = "STAGED IMPORT TABLE",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            items(previewItems) { item ->
                PreviewItemRow(item)
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    if (showConfirmReplaceDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showConfirmReplaceDialog = false },
            title = { Text("Replace Entire Database?") },
            text = {
                Text("This will replace current database items with the ${previewItems.size} parsed records from your CSV staging buffer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmReplaceDialog = false
                        viewModel.commitCsvImport(replaceExisting = true)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QualityErrorRed)
                ) {
                    Text("Confirm Replace")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmReplaceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PreviewItemRow(item: FoodItem) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.foodId} • ${item.foodNameEn}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when (item.verificationStatus) {
                                VerificationStatus.VERIFIED -> QualityGoodGreen.copy(alpha = 0.2f)
                                VerificationStatus.DEMONSTRATION -> AcademicGold.copy(alpha = 0.2f)
                                else -> QualityWarningOrange.copy(alpha = 0.2f)
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.verificationStatus.label,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        fontWeight = FontWeight.Bold,
                        color = when (item.verificationStatus) {
                            VerificationStatus.VERIFIED -> QualityGoodGreen
                            VerificationStatus.DEMONSTRATION -> Color(0xFF684E00)
                            else -> QualityWarningOrange
                        }
                    )
                }
            }
            Text(
                text = "${item.foodNameBn} • ${item.foodGroup} (${item.foodState})",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = EmeraldPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Energy: ${item.energyKcal100g?.let { "${it.toInt()} kcal" } ?: "Unavailable"}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                )
                Text(
                    text = "Protein: ${item.proteinG100g?.let { "${it}g" } ?: "Unavailable"}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                )
                Text(
                    text = "Price: ${item.priceBdt?.let { "৳$it" } ?: "Unavailable"} / ${item.priceUnitG?.let { "${it.toInt()}g" } ?: "Unavailable"}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
