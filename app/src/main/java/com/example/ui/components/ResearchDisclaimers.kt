package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AcademicGold
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.QualityWarningOrange

@Composable
fun ResearchDisclaimersDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Research Protocol & Integrity Rules",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Rule 1: No invented data
                RuleCard(
                    icon = Icons.Default.Lock,
                    iconTint = EmeraldPrimary,
                    title = "1. Strict Zero-Fabrication Rule",
                    description = "Nutrition figures, market retail prices, and citations are strictly derived from published peer-reviewed tables (INFS/DU FCTB) or BBS reports. Under no circumstances are missing values estimated, hallucinated, or guessed."
                )

                // Rule 2: Explicit Data Unavailable
                RuleCard(
                    icon = Icons.Default.Warning,
                    iconTint = QualityWarningOrange,
                    title = "2. 'Data Unavailable' Transparency",
                    description = "When a chemical micronutrient or market price was unmeasured in the primary reference survey, the platform explicitly displays 'Data unavailable' rather than zero or an imputed average."
                )

                // Rule 3: Distinct food states
                RuleCard(
                    icon = Icons.Default.CheckCircle,
                    iconTint = EmeraldPrimary,
                    title = "3. State Distinction (Raw vs. Cooked vs. Prepared)",
                    description = "Cooking and parboiling alter moisture, retention factors, and weight. Raw, boiled, cooked, and processed items are indexed as distinct records to avoid calculation skew."
                )

                // Rule 4: Non-clinical declaration
                RuleCard(
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF1565C0),
                    title = "4. Non-Clinical Validation Notice",
                    description = "This application is an academic data foundation for economic-nutrition modeling. It is NOT clinically validated for hospital dietetics, therapeutic prescriptions, or individual medical advice."
                )

                // Rule 5: Phase Roadmap
                RuleCard(
                    icon = Icons.Default.Science,
                    iconTint = AcademicGold,
                    title = "5. Advanced Meal Optimization (Phase 2)",
                    description = "Linear programming and multi-nutrient cost optimization algorithms will be activated in subsequent research phases following validation of comprehensive food basket surveys."
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_disclaimer_button")
            ) {
                Text("Acknowledge Research Rules")
            }
        }
    )
}

@Composable
private fun RuleCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
