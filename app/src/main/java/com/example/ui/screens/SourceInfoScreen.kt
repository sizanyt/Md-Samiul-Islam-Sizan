package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.EmeraldPrimaryContainer
import com.example.ui.theme.QualityGoodGreen
import com.example.viewmodel.NutriSmartViewModel

data class ResearchCitationRecord(
    val id: String,
    val title: String,
    val authorOrInstitution: String,
    val year: String,
    val publicationType: String,
    val marketDateRange: String,
    val geographicalScope: String,
    val sampleDescription: String,
    val citationText: String
)

@Composable
fun SourceInfoScreen(
    viewModel: NutriSmartViewModel,
    modifier: Modifier = Modifier
) {
    val baselineCitations = remember {
        mutableStateListOf(
            ResearchCitationRecord(
                id = "REF-INFS-2013",
                title = "Food Composition Table for Bangladesh (FCTB)",
                authorOrInstitution = "Institute of Nutrition and Food Science (INFS), University of Dhaka; Centre for Advanced Research in Sciences (CARS)",
                year = "2013",
                publicationType = "Peer-Reviewed Institutional Monograph (381 foods)",
                marketDateRange = "2010–2012 Multi-Division Field Sampling",
                geographicalScope = "All 7 Divisions of Bangladesh (National Composite)",
                sampleDescription = "Proximate analysis, AAS for minerals, HPLC for carotenoids and vitamins.",
                citationText = "INFS. (2013). Food Composition Table for Bangladesh. Institute of Nutrition and Food Science, University of Dhaka, Bangladesh."
            ),
            ResearchCitationRecord(
                id = "REF-BBS-2024",
                title = "Monthly Consumer Price Index (CPI) & Food Price Surveillance",
                authorOrInstitution = "Bangladesh Bureau of Statistics (BBS), Ministry of Planning, Government of Bangladesh",
                year = "2024",
                publicationType = "National Economic Statistical Bulletin",
                marketDateRange = "October 2024 Periodic Survey",
                geographicalScope = "Dhaka, Chittagong, Rajshahi, Khulna, Sylhet, Barisal, Rangpur, Mymensingh",
                sampleDescription = "Daily retail price collection across 64 districts for essential food basket commodities.",
                citationText = "BBS. (2024). Consumer Price Index, Inflation Rate and Wage Rate Index in Bangladesh (October 2024). Statistics and Informatics Division, Dhaka."
            ),
            ResearchCitationRecord(
                id = "REF-WORLDFISH-2015",
                title = "Nutrient Profiling of Small Indigenous Fish Species (SIS) in Bangladesh",
                authorOrInstitution = "WorldFish, INFS/University of Dhaka & FAO INFOODS",
                year = "2015",
                publicationType = "Aquaculture Nutrition Technical Report",
                marketDateRange = "2014–2015 Seasonal Harvesting",
                geographicalScope = "Floodplains, Haors and Beels of Mymensingh and Sunamganj",
                sampleDescription = "Vitamin A (retinol), calcium, and iron quantification in edible parts of Mola, Dhela, and Puti fish.",
                citationText = "Bogard, J. R., et al. (2015). Nutrient composition of important fish species in Bangladesh and potential contribution to recommended nutrient intakes. PLOS ONE."
            )
        )
    }

    var showAddSourceDialog by remember { mutableStateOf(false) }

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
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Source Information & Methodology Registry",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Transparent documentation of all laboratory food composition tables and physical market price surveillance dates used across NutriSmart BD.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRIMARY REFERENCES & CITATIONS (${baselineCitations.size})",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = MaterialTheme.colorScheme.secondary
                )

                OutlinedButton(
                    onClick = { showAddSourceDialog = true },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("add_source_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Citation", fontSize = 11.sp)
                }
            }
        }

        items(baselineCitations) { citation ->
            CitationCard(citation)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAddSourceDialog) {
        AddCitationDialog(
            onDismiss = { showAddSourceDialog = false },
            onSave = { newCit ->
                baselineCitations.add(0, newCit)
                showAddSourceDialog = false
            }
        )
    }
}

@Composable
private fun CitationCard(record: ResearchCitationRecord) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.id,
                        style = MaterialTheme.typography.labelSmall,
                        color = AcademicGold,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = record.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = record.authorOrInstitution,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(EmeraldPrimaryContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = record.year,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metadata pills
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Market Survey Range: ${record.marketDateRange}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, contentDescription = null, tint = AcademicGold, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Scope: ${record.geographicalScope}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // APA Citation block
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("APA Citation Format:", style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = record.citationText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun AddCitationDialog(
    onDismiss: () -> Unit,
    onSave: (ResearchCitationRecord) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("2024") }
    var marketDate by remember { mutableStateOf("2024-10") }
    var scope by remember { mutableStateOf("Dhaka Division, Bangladesh") }
    var citationText by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record New Research Reference", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Study / Report Title *") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author / Institution *") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = marketDate,
                        onValueChange = { marketDate = it },
                        label = { Text("Price Date Range") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = scope,
                    onValueChange = { scope = it },
                    label = { Text("Geographical Scope") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = citationText,
                    onValueChange = { citationText = it },
                    label = { Text("Full Citation Text") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank()) {
                        val record = ResearchCitationRecord(
                            id = "REF-CUSTOM-${(100..999).random()}",
                            title = title.trim(),
                            authorOrInstitution = author.trim(),
                            year = year.trim(),
                            publicationType = "Custom Field Entry",
                            marketDateRange = marketDate.trim(),
                            geographicalScope = scope.trim(),
                            sampleDescription = "Added via NutriSmart BD Source Panel",
                            citationText = if (citationText.isNotBlank()) citationText.trim() else "$author ($year). $title."
                        )
                        onSave(record)
                    }
                }
            ) {
                Text("Add Record")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
