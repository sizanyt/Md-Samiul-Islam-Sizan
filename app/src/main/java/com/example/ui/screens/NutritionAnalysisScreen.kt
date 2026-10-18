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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodItem
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.QualityErrorRed
import com.example.ui.theme.QualityGoodGreen
import com.example.ui.theme.QualityNeutralGray
import com.example.ui.theme.QualityWarningOrange
import com.example.viewmodel.NutriSmartViewModel

@Composable
fun NutritionAnalysisScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val basket by viewModel.nutritionBasket.collectAsState()
    val allItems by viewModel.filteredItems.collectAsState()
    val summary = viewModel.computeNutritionSummary()

    var showAddFoodDropdown by remember { mutableStateOf(false) }

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
                            imageVector = Icons.Default.Analytics,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nutrition Summary & Stoichiometric Formulation",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Deterministic, formula-based calculation of nutritional yield and total monetary cost across verified Bangladeshi diet rations. Zero synthetic numbers or statistical imputation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Research Protocol Compliance Box
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8)),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF90CAF9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Deterministic Math & Non-Clinical Integrity Rule", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0D47A1))
                        Text(
                            text = "• Nutrition totals use linear stoichiometric sums: Σ (nutrient_100g / 100 * portion_g).\n• This platform does NOT claim clinical validation.\n• Missing nutrition fields are never filled with fabricated guesses.",
                            fontSize = 11.sp,
                            color = Color(0xFF1565C0),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Summary Aggregates Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RATION BASKET DETERMINISTIC SUMMARY",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Meal / Ration Cost:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "${basket.size} constituent items (${summary.totalWeightGrams.toInt()}g total)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = String.format("৳%.2f BDT", summary.totalCostBdt),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Macronutrient Totals
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryMetricBox(label = "Energy", value = "${summary.totalEnergyKcal.toInt()} kcal", color = EmeraldPrimary)
                        SummaryMetricBox(label = "Protein", value = String.format("%.1fg", summary.totalProteinG), color = AcademicGold)
                        SummaryMetricBox(label = "Carbs", value = String.format("%.1fg", summary.totalCarbsG), color = Color(0xFF2E7D32))
                        SummaryMetricBox(label = "Fat", value = String.format("%.1fg", summary.totalFatG), color = Color(0xFFE65100))
                        SummaryMetricBox(label = "Fibre", value = String.format("%.1fg", summary.totalFibreG), color = Color(0xFF00838F))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Formula verification note
                    Text(
                        text = summary.formulaNote,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = if (summary.itemsWithMissingNutrientsCount > 0) QualityWarningOrange else QualityNeutralGray
                    )
                }
            }
        }

        // Constituent Food Items in Basket
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BASKET CONSTITUENTS (${basket.size})",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.secondary
                )

                Box {
                    Button(
                        onClick = { showAddFoodDropdown = true },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Food", fontSize = 11.sp)
                    }

                    DropdownMenu(
                        expanded = showAddFoodDropdown,
                        onDismissRequest = { showAddFoodDropdown = false }
                    ) {
                        allItems.forEach { food ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text("${food.foodId} • ${food.foodNameEn}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text("${food.foodNameBn} (${food.foodState})", fontSize = 10.sp, color = AcademicGold)
                                    }
                                },
                                onClick = {
                                    viewModel.addToBasket(food, 100.0)
                                    showAddFoodDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }

        itemsIndexed(basket) { index, entry ->
            BasketItemCard(
                entry = entry,
                onUpdateGrams = { g -> viewModel.updateBasketItemGrams(index, g) },
                onRemove = { viewModel.removeFromBasket(index) }
            )
        }

        // Advanced Meal Optimization Roadmap Card
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LockClock,
                            contentDescription = null,
                            tint = QualityNeutralGray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Phase 2 Roadmap: Advanced Meal Optimization",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "As instructed by research guidelines, advanced linear programming optimization (Stigler minimum cost diet modeling) is deferred until foundational data import, duplicate validation, and unit conversions are verified across the 30 Bangladeshi food candidates.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
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
private fun SummaryMetricBox(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun BasketItemCard(
    entry: com.example.model.BasketPortionItem,
    onUpdateGrams: (Double) -> Unit,
    onRemove: () -> Unit
) {
    val food = entry.foodItem
    val factor = entry.portionGrams / 100.0
    val kcal = food.energyKcal100g?.let { "${(it * factor).toInt()} kcal" } ?: "Unavailable"
    val protein = food.proteinG100g?.let { String.format("%.1fg", it * factor) } ?: "Unavailable"
    val cost = if (food.isPriceValid()) {
        val costPerGram = (food.priceBdt ?: 0.0) / (food.priceUnitG ?: 1.0)
        String.format("৳%.2f", costPerGram * entry.portionGrams)
    } else "No price"

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${food.foodId} • ${food.foodNameEn}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${food.foodNameBn} (${food.foodState})",
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldPrimary
                    )
                }

                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = QualityErrorRed, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Portion adjust buttons & yields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onUpdateGrams((entry.portionGrams - 25.0).coerceAtLeast(25.0)) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = "${entry.portionGrams.toInt()}g",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(
                        onClick = { onUpdateGrams(entry.portionGrams + 25.0) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = kcal, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Text(text = "Prot: $protein", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    Text(text = cost, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = EmeraldPrimary)
                }
            }
        }
    }
}
