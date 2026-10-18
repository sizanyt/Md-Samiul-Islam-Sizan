package com.example.model

enum class VerificationStatus(val label: String) {
    VERIFIED("Verified"),
    UNVERIFIED("Unverified"),
    REQUIRES_REVIEW("Requires Review"),
    DEMONSTRATION("Demonstration Record")
}

data class FoodItem(
    val foodId: String,
    val foodNameEn: String,
    val foodNameBn: String,
    val foodGroup: String,
    val foodState: String, // Raw, Cooked, Dry, Liquid, Prepared
    val energyKcal100g: Double?,
    val proteinG100g: Double?,
    val carbsG100g: Double?,
    val fatG100g: Double?,
    val fibreG100g: Double?,
    val priceBdt: Double?,
    val priceUnitG: Double?, // e.g. 1000 for 1kg, 50 for 1 egg, 920 for 1L oil
    val priceDate: String?,
    val priceLocation: String?,
    val nutritionSource: String?,
    val nutritionPageOrTable: String?,
    val priceSource: String?,
    val priceReportDate: String?,
    val priceUnitDescription: String?,
    val verificationStatus: VerificationStatus,
    val notes: String? = null,
    val isDemonstration: Boolean = false
) {
    // Backward compatibility helper
    val id: String get() = foodId
    val nameEn: String get() = foodNameEn
    val nameBn: String get() = foodNameBn
    val category: String get() = foodGroup
    val stateLabel: String get() = foodState

    /**
     * Strict Research Validation:
     * A record CANNOT be labeled Verified unless:
     * 1. Nutrition source is present.
     * 2. Price unit weight (in grams) is present and > 0.
     * 3. Food state is specified.
     * 4. Price date is present.
     */
    fun checkVerificationIntegrity(): VerificationStatus {
        if (isDemonstration) return VerificationStatus.DEMONSTRATION

        val hasSource = !nutritionSource.isNullOrBlank()
        val hasUnitG = priceUnitG != null && priceUnitG > 0
        val hasState = foodState.isNotBlank()
        val hasDate = !priceDate.isNullOrBlank()

        return if (hasSource && hasUnitG && hasState && hasDate) {
            if (priceBdt != null && energyKcal100g != null) {
                VerificationStatus.VERIFIED
            } else {
                VerificationStatus.REQUIRES_REVIEW
            }
        } else {
            VerificationStatus.UNVERIFIED
        }
    }

    fun isPriceValid(): Boolean {
        return priceBdt != null && priceBdt > 0.0 && priceUnitG != null && priceUnitG > 0.0
    }

    fun missingNutritionCount(): Int {
        var count = 0
        if (energyKcal100g == null) count++
        if (proteinG100g == null) count++
        if (carbsG100g == null) count++
        if (fatG100g == null) count++
        if (fibreG100g == null) count++
        return count
    }

    fun toCsvRow(): String {
        fun escape(s: String?): String {
            if (s == null) return ""
            return if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
                "\"${s.replace("\"", "\"\"")}\""
            } else s
        }
        return listOf(
            foodId,
            escape(foodNameEn),
            escape(foodNameBn),
            escape(foodGroup),
            escape(foodState),
            energyKcal100g?.toString() ?: "",
            proteinG100g?.toString() ?: "",
            carbsG100g?.toString() ?: "",
            fatG100g?.toString() ?: "",
            fibreG100g?.toString() ?: "",
            priceBdt?.toString() ?: "",
            priceUnitG?.toString() ?: "",
            escape(priceDate),
            escape(priceLocation),
            escape(nutritionSource),
            escape(nutritionPageOrTable),
            escape(priceSource),
            escape(priceReportDate),
            escape(priceUnitDescription),
            verificationStatus.label,
            escape(notes)
        ).joinToString(",")
    }
}

data class QualityAuditSummary(
    val totalRecords: Int,
    val verifiedCount: Int,
    val unverifiedCount: Int,
    val demonstrationCount: Int,
    val duplicateIdCount: Int,
    val missingPriceCount: Int,
    val missingNutrientCount: Int,
    val flaggedForReviewCount: Int,
    val duplicateIds: List<String>,
    val recordsRequiringReview: List<FoodItem>
)

data class BasketPortionItem(
    val foodItem: FoodItem,
    val portionGrams: Double
)
