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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.QualityErrorRed
import com.example.ui.theme.QualityGoodGreen
import com.example.ui.theme.QualityNeutralGray
import com.example.viewmodel.NutriSmartViewModel

@Composable
fun PriceCalculatorScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.filteredItems.collectAsState()
    val selectedItem by viewModel.calcSelectedItem.collectAsState()
    val portionInput by viewModel.calcPortionInput.collectAsState()

    var foodDropdownExpanded by remember { mutableStateOf(false) }

    val calcResult = viewModel.computePriceCalculation()

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
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Food Price Calculator with Explicit Unit Conversion",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Converts physical wholesale/retail reference packaging (e.g. 1000g kg, 50g egg piece, 920g litre bottle) to exact portion costs and affordability metrics without price fabrication.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Food Item Selector Dropdown
        item {
            Text(
                text = "1. SELECT FOOD ITEM FROM DATABASE",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { foodDropdownExpanded = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("select_food_for_price_calc_button"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                            Text(
                                text = selectedItem?.let { "${it.foodId} - ${it.foodNameEn}" } ?: "Select a Food Item...",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            selectedItem?.let {
                                Text(
                                    text = "${it.foodNameBn} • ${it.foodGroup} (${it.foodState})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldPrimary
                                )
                            }
                        }
                        Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
                    }
                }

                DropdownMenu(
                    expanded = foodDropdownExpanded,
                    onDismissRequest = { foodDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${item.foodId} • ${item.foodNameEn}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(
                                            if (item.isPriceValid()) "৳${item.priceBdt} / ${item.priceUnitG?.toInt()}g" else "No Price",
                                            fontSize = 11.sp,
                                            color = if (item.isPriceValid()) QualityGoodGreen else QualityErrorRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text("${item.foodNameBn} (${item.foodState})", fontSize = 11.sp, color = AcademicGold)
                                }
                            },
                            onClick = {
                                viewModel.setCalcSelectedItem(item)
                                foodDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Portion Input & Presets
        item {
            Text(
                text = "2. ENTER DESIRED CONSUMPTION PORTION (GRAMS)",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = portionInput,
                onValueChange = { viewModel.setCalcPortionInput(it) },
                label = { Text("Portion in Grams (g)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("portion_input_field"),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Quick household unit conversion chips
            Text("Household Serving Presets:", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    Pair("100g", "100"),
                    Pair("50g (1 Egg)", "50"),
                    Pair("40g (1 Roti)", "40"),
                    Pair("150g (Cooked Serving)", "150"),
                    Pair("200g (1 Bowl Dal)", "200"),
                    Pair("14g (1 Tbsp Oil)", "14"),
                    Pair("1000g (1 kg)", "1000")
                ).forEach { (label, grams) ->
                    item {
                        FilterChip(
                            selected = portionInput == grams,
                            onClick = { viewModel.setCalcPortionInput(grams) },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // Calculation Results or STRICT VALIDATION ERROR
        item {
            Text(
                text = "3. DETERMINISTIC CONVERSION & AFFORDABILITY REPORT",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (!calcResult.isValid) {
                // Strict validation guard triggered
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, QualityErrorRed.copy(alpha = 0.7f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("price_calc_error_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                tint = QualityErrorRed,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Calculation Blocked by Research Protocol",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = QualityErrorRed
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = calcResult.errorMessage ?: "Price or unit data is unavailable for this item.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFB71C1C),
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Research Rule: Never fill missing nutrition values or prices with invented numbers. Use actual imported data and display an unavailable state when required fields are missing.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = QualityNeutralGray
                        )
                    }
                }
            } else {
                // Successful Calculation View
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, QualityGoodGreen.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("price_calc_success_card")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = QualityGoodGreen, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Price & Reference Unit Verified",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = QualityGoodGreen
                                )
                            }
                            Text(
                                text = "৳${calcResult.basePriceBdt} / ${calcResult.baseUnitGrams?.toInt()}g",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium,
                                color = EmeraldPrimary
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )

                        // Explicit Unit Conversion Steps Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Functions, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Explicit Unit Conversion Steps:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "1. Base Unit Price: ৳${calcResult.basePriceBdt} BDT per ${calcResult.baseUnitGrams?.toInt()} grams (${calcResult.unitDescription})\n" +
                                            "2. Cost per Gram: ৳${calcResult.basePriceBdt} ÷ ${calcResult.baseUnitGrams?.toInt()}g = ৳${String.format("%.4f", calcResult.costPerGramBdt)} / gram\n" +
                                            "3. Portion Cost (${calcResult.portionGrams.toInt()}g): ৳${String.format("%.4f", calcResult.costPerGramBdt)} × ${calcResult.portionGrams.toInt()}g = ৳${String.format("%.2f", calcResult.portionCostBdt)} BDT",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp
                                    ),
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Portion Cost Metric Display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Portion Cost (${calcResult.portionGrams.toInt()}g):",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${selectedItem?.foodNameEn} (${selectedItem?.foodNameBn})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = String.format("৳%.2f BDT", calcResult.portionCostBdt),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Affordability Metrics
                        Text(
                            text = "Nutrient-to-Cost Affordability Ratios",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = AcademicGold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Cost per 100 kcal
                            Card(
                                colors = CardDefaults.cardColors(containerColor = EmeraldPrimaryContainer.copy(alpha = 0.35f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Cost per 100 kcal", style = MaterialTheme.typography.labelSmall)
                                    Text(
                                        text = calcResult.costPer100KcalBdt?.let { String.format("৳%.2f", it) } ?: "Data unavailable",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldPrimary
                                    )
                                    Text("Energy affordability", style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp), color = QualityNeutralGray)
                                }
                            }

                            // Cost per 10g protein
                            Card(
                                colors = CardDefaults.cardColors(containerColor = AcademicGold.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Cost per 10g Protein", style = MaterialTheme.typography.labelSmall)
                                    Text(
                                        text = calcResult.costPer10gProteinBdt?.let { String.format("৳%.2f", it) } ?: "Data unavailable",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AcademicGold
                                    )
                                    Text("Protein affordability", style = MaterialTheme.typography.bodySmall.copy(fontSize = 9.sp), color = QualityNeutralGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Provenance metadata
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Store, contentDescription = null, tint = QualityNeutralGray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Market Provenance: ${selectedItem?.priceLocation ?: "Dhaka Surveillance"} on ${selectedItem?.priceDate ?: "Date Unavailable"} (${selectedItem?.priceSource ?: "Official Price Bulletin"})",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = QualityNeutralGray
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
