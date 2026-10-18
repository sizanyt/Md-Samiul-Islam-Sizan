package com.example

import com.example.data.ResearchFoodRepository
import com.example.model.FoodItem
import com.example.model.VerificationStatus
import com.example.viewmodel.NutriSmartViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testThirtyCandidatesBaseline() {
        val repo = ResearchFoodRepository()
        val items = repo.getAllItems()
        assertEquals("Baseline should contain exactly 30 food items", 30, items.size)

        // Verify ID sequence F001 to F030
        assertEquals("F001", items.first().foodId)
        assertEquals("F030", items.last().foodId)

        // Verify distinct raw vs cooked items
        val rawRice = items.find { it.foodId == "F001" }
        val cookedRice = items.find { it.foodId == "F002" }
        assertNotNull(rawRice)
        assertNotNull(cookedRice)
        assertEquals("Raw", rawRice?.foodState)
        assertEquals("Cooked", cookedRice?.foodState)
    }

    @Test
    fun testExplicitPriceCalculatorUnitConversion() {
        val repo = ResearchFoodRepository()
        val viewModel = NutriSmartViewModel(repo)

        // 1. Test standard 1 kg unit conversion (Raw Rice F001: 68 BDT / 1000g)
        val rawRice = repo.getAllItems().first { it.foodId == "F001" }
        viewModel.setCalcSelectedItem(rawRice)
        viewModel.setCalcPortionInput("100") // 100g portion
        val riceResult = viewModel.computePriceCalculation()

        assertTrue(riceResult.isValid)
        assertEquals(0.068, riceResult.costPerGramBdt, 0.0001)
        assertEquals(6.80, riceResult.portionCostBdt, 0.01)
        assertNotNull(riceResult.costPer100KcalBdt)

        // 2. Test piece unit conversion (Egg F011: 14.50 BDT / 50g piece)
        val egg = repo.getAllItems().first { it.foodId == "F011" }
        viewModel.setCalcSelectedItem(egg)
        viewModel.setCalcPortionInput("50") // 1 egg serving = 50g
        val eggResult = viewModel.computePriceCalculation()

        assertTrue(eggResult.isValid)
        assertEquals(0.29, eggResult.costPerGramBdt, 0.001)
        assertEquals(14.50, eggResult.portionCostBdt, 0.01)

        // 3. Test unpriced item guard (Cooked Rice F002: null price)
        val cookedRice = repo.getAllItems().first { it.foodId == "F002" }
        viewModel.setCalcSelectedItem(cookedRice)
        val blockedResult = viewModel.computePriceCalculation()

        assertFalse("Calculation must be blocked when price is missing", blockedResult.isValid)
        assertNotNull("Must return an explanatory error message", blockedResult.errorMessage)
    }

    @Test
    fun testVerificationIntegrityRule() {
        val verifiedItem = FoodItem(
            foodId = "F999",
            foodNameEn = "Test Verified Item",
            foodNameBn = "যাচাইকৃত খাদ্য",
            foodGroup = "Grain",
            foodState = "Raw",
            energyKcal100g = 300.0,
            proteinG100g = 10.0,
            carbsG100g = 60.0,
            fatG100g = 1.0,
            fibreG100g = 2.0,
            priceBdt = 50.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-15",
            priceLocation = "Dhaka",
            nutritionSource = "FCTB 2013",
            nutritionPageOrTable = "Table 1",
            priceSource = "TCB",
            priceReportDate = "2024-10-15",
            priceUnitDescription = "1 kg",
            verificationStatus = VerificationStatus.VERIFIED
        )
        assertEquals(VerificationStatus.VERIFIED, verifiedItem.checkVerificationIntegrity())

        // Missing priceDate must prevent Verified status
        val itemWithoutDate = verifiedItem.copy(priceDate = null)
        assertEquals(VerificationStatus.UNVERIFIED, itemWithoutDate.checkVerificationIntegrity())

        // Missing nutritionSource must prevent Verified status
        val itemWithoutSource = verifiedItem.copy(nutritionSource = null)
        assertEquals(VerificationStatus.UNVERIFIED, itemWithoutSource.checkVerificationIntegrity())
    }

    @Test
    fun testTwentyOneColumnCsvParsingAndDuplicates() {
        val repo = ResearchFoodRepository()
        val csv = """
food_id,food_name_en,food_name_bn,food_group,food_state,energy_kcal_100g,protein_g_100g,carbs_g_100g,fat_g_100g,fibre_g_100g,price_bdt,price_unit_g,price_date,price_location,nutrition_source,nutrition_page_or_table,price_source,price_report_date,price_unit_description,verification_status,notes
F001,"Rice, raw",কাঁচা চাল,Grain,Raw,356.0,7.7,79.2,0.5,0.4,68.0,1000.0,2024-10-10,Karwan Bazar,FCTB 2013,Table 1,TCB,2024-10-10,1 kg,Verified,Valid
F001,"Rice, raw duplicate",কাঁচা চাল ডুপ্লিকেট,Grain,Raw,356.0,7.7,79.2,0.5,0.4,68.0,1000.0,2024-10-10,Karwan Bazar,FCTB 2013,Table 1,TCB,2024-10-10,1 kg,Verified,Duplicate
        """.trimIndent()

        val (items, warnings) = repo.parseCsvText(csv)
        assertEquals(2, items.size)
        assertTrue("Warnings should alert duplicate Food ID F001", warnings.any { it.contains("Duplicate Food ID 'F001'") })
    }

    @Test
    fun testDeterministicNutritionSummary() {
        val repo = ResearchFoodRepository()
        val viewModel = NutriSmartViewModel(repo)

        val summary = viewModel.computeNutritionSummary()
        assertTrue("Basket should compute positive total weight", summary.totalWeightGrams > 0.0)
        assertTrue("Basket should compute positive energy", summary.totalEnergyKcal > 0.0)
        assertTrue("Basket should compute positive protein", summary.totalProteinG > 0.0)
        assertTrue("Basket should compute positive cost", summary.totalCostBdt > 0.0)
    }
}
