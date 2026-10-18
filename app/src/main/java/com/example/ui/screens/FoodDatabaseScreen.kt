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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FoodItem
import com.example.model.VerificationStatus
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.QualityErrorRed
import com.example.ui.theme.QualityGoodGreen
import com.example.ui.theme.QualityNeutralGray
import com.example.ui.theme.QualityWarningOrange
import com.example.viewmodel.NutriSmartViewModel

@Composable
fun FoodDatabaseScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.filteredItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGroup by viewModel.selectedGroup.collectAsState()
    val selectedState by viewModel.selectedState.collectAsState()
    val foodGroups by viewModel.foodGroups.collectAsState()
    val foodStates by viewModel.foodStates.collectAsState()
    val selectedItemForDetail by viewModel.selectedItemForDetail.collectAsState()
    val showAddDialog by viewModel.showAddDialog.collectAsState()

    var groupDropdownExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar (Bilingual English & Bangla script)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        "Search English or বাংলা (e.g. 'Rice', 'চাল', 'ডাল', 'F001')",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = EmeraldPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("food_search_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // State & Group Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Food State Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    foodStates.forEach { st ->
                        item {
                            FilterChip(
                                selected = selectedState == st,
                                onClick = { viewModel.setStateFilter(st) },
                                label = { Text(st, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Food Group selector button
                Box {
                    OutlinedButton(
                        onClick = { groupDropdownExpanded = true },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (selectedGroup == "All") "Food Group" else selectedGroup,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    DropdownMenu(
                        expanded = groupDropdownExpanded,
                        onDismissRequest = { groupDropdownExpanded = false }
                    ) {
                        foodGroups.forEach { grp ->
                            DropdownMenuItem(
                                text = { Text(grp, fontSize = 13.sp) },
                                onClick = {
                                    viewModel.setGroup(grp)
                                    groupDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Results count and Table Header indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${items.size} food records",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Tap row to inspect complete laboratory profile",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = AcademicGold
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            // Food Items List / Table View
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = QualityNeutralGray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No matching research records found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try clearing search keywords or resetting filters.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(onClick = {
                            viewModel.setSearchQuery("")
                            viewModel.setGroup("All")
                            viewModel.setStateFilter("All")
                        }) {
                            Text("Reset All Filters")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
                ) {
                    items(items, key = { it.foodId }) { item ->
                        FoodRecordRowCard(
                            item = item,
                            onClick = { viewModel.selectItemForDetail(item) },
                            onAddToSummary = { viewModel.addToBasket(item, 100.0) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }

        // Floating Action Button to Add Custom Food Record
        FloatingActionButton(
            onClick = { viewModel.openAddDialog() },
            containerColor = EmeraldPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_food_record_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Food Record")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Record", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Modal: Detailed Food Record Inspector
    selectedItemForDetail?.let { item ->
        FoodDetailModal(
            item = item,
            onDismiss = { viewModel.selectItemForDetail(null) },
            onAddToSummary = {
                viewModel.addToBasket(item, 100.0)
                viewModel.selectItemForDetail(null)
            }
        )
    }

    // Modal: Add Custom Food Record Dialog
    if (showAddDialog) {
        AddFoodRecordDialog(
            onDismiss = { viewModel.closeAddDialog() },
            onSave = { newItem -> viewModel.addCustomRecord(newItem) }
        )
    }
}

@Composable
private fun FoodRecordRowCard(
    item: FoodItem,
    onClick: () -> Unit,
    onAddToSummary: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (item.verificationStatus != VerificationStatus.VERIFIED) QualityWarningOrange.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("food_row_${item.foodId}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header: ID, Bilingual Name, State badge, Verification badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.foodId,
                            style = MaterialTheme.typography.labelSmall,
                            color = AcademicGold,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${item.foodGroup}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.foodNameEn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.foodNameBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    // Food State Badge (Raw / Cooked / Dry / Liquid / Prepared)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (item.foodState.lowercase()) {
                                    "raw" -> Color(0xFFE8F5E9)
                                    "cooked" -> Color(0xFFFFF3E0)
                                    "dry" -> Color(0xFFEFEBE9)
                                    "liquid" -> Color(0xFFE1F5FE)
                                    else -> Color(0xFFEDE7F6)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.foodState,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (item.foodState.lowercase()) {
                                "raw" -> QualityGoodGreen
                                "cooked" -> QualityWarningOrange
                                else -> Color(0xFF37474F)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Verification Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (item.verificationStatus) {
                                    VerificationStatus.VERIFIED -> QualityGoodGreen.copy(alpha = 0.15f)
                                    VerificationStatus.DEMONSTRATION -> AcademicGold.copy(alpha = 0.2f)
                                    else -> QualityWarningOrange.copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.verificationStatus.label,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (item.verificationStatus) {
                                VerificationStatus.VERIFIED -> QualityGoodGreen
                                VerificationStatus.DEMONSTRATION -> Color(0xFF684E00)
                                else -> QualityWarningOrange
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nutrient composition values or "Data unavailable"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientPill(label = "Energy", value = item.energyKcal100g?.let { "${it.toInt()} kcal" })
                NutrientPill(label = "Protein", value = item.proteinG100g?.let { "${it}g" })
                NutrientPill(label = "Carbs", value = item.carbsG100g?.let { "${it}g" })
                NutrientPill(label = "Fat", value = item.fatG100g?.let { "${it}g" })
                NutrientPill(label = "Fibre", value = item.fibreG100g?.let { "${it}g" })
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price & Quick Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.isPriceValid()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(EmeraldPrimaryContainer)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "৳${item.priceBdt} / ${item.priceUnitDescription ?: "${item.priceUnitG?.toInt()}g"}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFEEEEEE))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Price unavailable",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = QualityNeutralGray
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = onAddToSummary,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Summary", fontSize = 10.sp)
                    }
                }
            }

            if (item.notes != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Note: ${item.notes}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun NutrientPill(label: String, value: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = "Data unavailable",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                color = QualityWarningOrange,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FoodDetailModal(
    item: FoodItem,
    onDismiss: () -> Unit,
    onAddToSummary: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.foodNameEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.foodNameBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = EmeraldPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Verification banner
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when (item.verificationStatus) {
                            VerificationStatus.VERIFIED -> Color(0xFFE8F5E9)
                            VerificationStatus.DEMONSTRATION -> Color(0xFFFFF8E1)
                            else -> Color(0xFFFFF3E0)
                        }
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Verification: ${item.verificationStatus.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = when (item.verificationStatus) {
                                VerificationStatus.VERIFIED -> QualityGoodGreen
                                VerificationStatus.DEMONSTRATION -> Color(0xFF8D6E00)
                                else -> QualityWarningOrange
                            }
                        )
                    }
                }

                // 21-column metadata
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        DetailFieldRow("Food ID", item.foodId)
                        DetailFieldRow("Food Group", item.foodGroup)
                        DetailFieldRow("Food State", item.foodState)
                        DetailFieldRow("Nutrition Source", item.nutritionSource ?: "Data unavailable")
                        DetailFieldRow("Table / Page", item.nutritionPageOrTable ?: "Data unavailable")
                        DetailFieldRow("Price (BDT)", item.priceBdt?.let { "৳$it" } ?: "Data unavailable")
                        DetailFieldRow("Price Unit (g)", item.priceUnitG?.let { "${it.toInt()}g" } ?: "Data unavailable")
                        DetailFieldRow("Unit Description", item.priceUnitDescription ?: "Data unavailable")
                        DetailFieldRow("Price Date", item.priceDate ?: "Data unavailable")
                        DetailFieldRow("Price Location", item.priceLocation ?: "Data unavailable")
                        DetailFieldRow("Price Source", item.priceSource ?: "Data unavailable")
                        DetailFieldRow("Price Report Date", item.priceReportDate ?: "Data unavailable")
                        DetailFieldRow("Notes", item.notes ?: "None")
                    }
                }

                Text(
                    text = "Nutritional Composition (per 100g edible portion)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        DetailNutrientRow("Energy", item.energyKcal100g?.let { "$it kcal" })
                        DetailNutrientRow("Protein", item.proteinG100g?.let { "$it g" })
                        DetailNutrientRow("Carbohydrate", item.carbsG100g?.let { "$it g" })
                        DetailNutrientRow("Fat (Lipids)", item.fatG100g?.let { "$it g" })
                        DetailNutrientRow("Dietary Fibre", item.fibreG100g?.let { "$it g" })
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAddToSummary) {
                    Text("+ Add to Nutrition Summary")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
fun DetailFieldRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DetailNutrientRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        if (value != null) {
            Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        } else {
            Text(
                text = "Data unavailable",
                style = MaterialTheme.typography.bodySmall,
                color = QualityWarningOrange,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AddFoodRecordDialog(
    onDismiss: () -> Unit,
    onSave: (FoodItem) -> Unit
) {
    var id by remember { mutableStateOf("F0${(31..99).random()}") }
    var nameEn by remember { mutableStateOf("") }
    var nameBn by remember { mutableStateOf("") }
    var foodGroup by remember { mutableStateOf("Vegetable") }
    var foodState by remember { mutableStateOf("Raw") }
    var energy by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var fibre by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var priceUnitG by remember { mutableStateOf("1000") }
    var priceDate by remember { mutableStateOf("2024-10-15") }
    var priceLocation by remember { mutableStateOf("Karwan Bazar, Dhaka") }
    var sourceCitation by remember { mutableStateOf("INFS / DU FCTB 2013") }
    var isDemonstration by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Research Food Record", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Fill laboratory fields. If any value is unmeasured, leave blank to mark 'Data unavailable'.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = AcademicGold
                )

                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("Food ID (e.g. F031)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text("English Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nameBn,
                    onValueChange = { nameBn = it },
                    label = { Text("Bangla Name (বাংলা নাম) *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = foodGroup,
                        onValueChange = { foodGroup = it },
                        label = { Text("Food Group") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = foodState,
                        onValueChange = { foodState = it },
                        label = { Text("Food State (Raw/Cooked/Dry)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = energy,
                        onValueChange = { energy = it },
                        label = { Text("Energy (kcal/100g)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Protein (g/100g)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { carbs = it },
                        label = { Text("Carbs (g/100g)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { fat = it },
                        label = { Text("Fat (g/100g)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price (BDT)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = priceUnitG,
                        onValueChange = { priceUnitG = it },
                        label = { Text("Unit Weight (g)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = sourceCitation,
                    onValueChange = { sourceCitation = it },
                    label = { Text("Nutrition Source Citation *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDemonstration = !isDemonstration }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = isDemonstration,
                        onCheckedChange = { isDemonstration = it }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mark as 'Demonstration Record' (not an official FCTB citation)",
                        fontSize = 11.sp
                    )
                }

                errorMessage?.let { err ->
                    Text(text = err, color = QualityErrorRed, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameEn.isBlank() || nameBn.isBlank() || sourceCitation.isBlank()) {
                        errorMessage = "English name, Bangla name, and source citation are mandatory."
                        return@Button
                    }
                    val item = FoodItem(
                        foodId = id.trim(),
                        foodNameEn = nameEn.trim(),
                        foodNameBn = nameBn.trim(),
                        foodGroup = foodGroup.trim(),
                        foodState = foodState.trim(),
                        energyKcal100g = energy.toDoubleOrNull(),
                        proteinG100g = protein.toDoubleOrNull(),
                        carbsG100g = carbs.toDoubleOrNull(),
                        fatG100g = fat.toDoubleOrNull(),
                        fibreG100g = fibre.toDoubleOrNull(),
                        priceBdt = price.toDoubleOrNull(),
                        priceUnitG = priceUnitG.toDoubleOrNull(),
                        priceDate = priceDate.trim().ifBlank { null },
                        priceLocation = priceLocation.trim().ifBlank { null },
                        nutritionSource = sourceCitation.trim(),
                        nutritionPageOrTable = "Custom Entry",
                        priceSource = "Field Survey Log",
                        priceReportDate = priceDate.trim(),
                        priceUnitDescription = "${priceUnitG}g retail",
                        verificationStatus = if (isDemonstration) VerificationStatus.DEMONSTRATION else VerificationStatus.REQUIRES_REVIEW,
                        notes = if (isDemonstration) "Manually entered demonstration record." else "Custom surveyed entry.",
                        isDemonstration = isDemonstration
                    )
                    onSave(item)
                }
            ) {
                Text("Save to Database")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
