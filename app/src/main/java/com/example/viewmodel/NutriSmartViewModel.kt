package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ResearchFoodRepository
import com.example.model.BasketPortionItem
import com.example.model.FoodItem
import com.example.model.QualityAuditSummary
import com.example.model.VerificationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class AppNavDestination(val titleEn: String, val titleBn: String) {
    DASHBOARD("Dashboard", "ড্যাশবোর্ড"),
    DATABASE("Food Database", "খাদ্য ডাটাবেজ"),
    CSV_IMPORT("CSV Import", "সিএসভি আপলোড"),
    DATA_QUALITY("Data Quality", "উপাত্ত মান"),
    PRICE_CALCULATOR("Price Calculator", "মূল্য ক্যালকুলেটর"),
    NUTRITION_SUMMARY("Nutrition Summary", "পুষ্টি সামারি"),
    SOURCE_INFO("Source Info", "উৎস ও তথ্যসূত্র")
}

data class ExplicitPriceCalculation(
    val isValid: Boolean,
    val errorMessage: String? = null,
    val portionGrams: Double = 100.0,
    val basePriceBdt: Double? = null,
    val baseUnitGrams: Double? = null,
    val costPerGramBdt: Double = 0.0,
    val portionCostBdt: Double = 0.0,
    val costPer100KcalBdt: Double? = null,
    val costPer10gProteinBdt: Double? = null,
    val unitDescription: String = "",
    val formulaExplanation: String = ""
)

data class DeterministicNutritionSummary(
    val totalWeightGrams: Double = 0.0,
    val totalCostBdt: Double = 0.0,
    val totalEnergyKcal: Double = 0.0,
    val totalProteinG: Double = 0.0,
    val totalCarbsG: Double = 0.0,
    val totalFatG: Double = 0.0,
    val totalFibreG: Double = 0.0,
    val itemsWithMissingNutrientsCount: Int = 0,
    val itemsWithMissingPriceCount: Int = 0,
    val formulaNote: String = ""
)

class NutriSmartViewModel(
    private val repository: ResearchFoodRepository = ResearchFoodRepository()
) : ViewModel() {

    private val _currentDestination = MutableStateFlow(AppNavDestination.DASHBOARD)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGroup = MutableStateFlow("All")
    val selectedGroup: StateFlow<String> = _selectedGroup.asStateFlow()

    private val _selectedState = MutableStateFlow("All")
    val selectedState: StateFlow<String> = _selectedState.asStateFlow()

    private val _selectedVerificationFilter = MutableStateFlow<VerificationStatus?>(null)
    val selectedVerificationFilter: StateFlow<VerificationStatus?> = _selectedVerificationFilter.asStateFlow()

    private val _selectedItemForDetail = MutableStateFlow<FoodItem?>(null)
    val selectedItemForDetail: StateFlow<FoodItem?> = _selectedItemForDetail.asStateFlow()

    // Price Calculator state
    private val _calcSelectedItem = MutableStateFlow<FoodItem?>(null)
    val calcSelectedItem: StateFlow<FoodItem?> = _calcSelectedItem.asStateFlow()

    private val _calcPortionInput = MutableStateFlow("100")
    val calcPortionInput: StateFlow<String> = _calcPortionInput.asStateFlow()

    // Nutrition Formulation / Basket state
    private val _nutritionBasket = MutableStateFlow<List<BasketPortionItem>>(
        listOf(
            // Pre-seed with a standard Bengali meal (Cooked Rice + Cooked Dal + Boiled/Raw Egg + Spinach)
            BasketPortionItem(repository.getAllItems().first { it.foodId == "F001" }, 150.0), // Raw Rice reference
            BasketPortionItem(repository.getAllItems().first { it.foodId == "F006" }, 50.0),  // Lentil dry
            BasketPortionItem(repository.getAllItems().first { it.foodId == "F011" }, 50.0),  // 1 Egg
            BasketPortionItem(repository.getAllItems().first { it.foodId == "F018" }, 100.0)  // Spinach
        )
    )
    val nutritionBasket: StateFlow<List<BasketPortionItem>> = _nutritionBasket.asStateFlow()

    // CSV Staging state
    private val _csvInputText = MutableStateFlow("")
    val csvInputText: StateFlow<String> = _csvInputText.asStateFlow()

    private val _csvPreviewItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val csvPreviewItems: StateFlow<List<FoodItem>> = _csvPreviewItems.asStateFlow()

    private val _csvWarnings = MutableStateFlow<List<String>>(emptyList())
    val csvWarnings: StateFlow<List<String>> = _csvWarnings.asStateFlow()

    // Feedback message
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    // Dialog state for adding a record
    private val _showAddDialog = MutableStateFlow(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog.asStateFlow()

    // Dialog for Info / Clinical disclaimer details
    private val _showDisclaimerModal = MutableStateFlow(false)
    val showDisclaimerModal: StateFlow<Boolean> = _showDisclaimerModal.asStateFlow()

    // Filtered items
    val filteredItems: StateFlow<List<FoodItem>> = combine(
        repository.itemsFlow,
        _searchQuery,
        _selectedGroup,
        _selectedState
    ) { items, query, group, state ->
        val q = query.trim().lowercase()
        items.filter { item ->
            val matchesQuery = q.isEmpty() ||
                    item.foodNameEn.lowercase().contains(q) ||
                    item.foodNameBn.contains(q) ||
                    item.foodId.lowercase().contains(q) ||
                    item.foodGroup.lowercase().contains(q)

            val matchesGroup = group == "All" || item.foodGroup.equals(group, ignoreCase = true)
            val matchesState = state == "All" || item.foodState.equals(state, ignoreCase = true)

            matchesQuery && matchesGroup && matchesState
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getAllItems())

    val qualityAudit: StateFlow<QualityAuditSummary> = repository.itemsFlow.combine(_searchQuery) { _, _ ->
        repository.computeQualityAudit()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.computeQualityAudit())

    val foodGroups: StateFlow<List<String>> = repository.itemsFlow.combine(_searchQuery) { items, _ ->
        listOf("All") + items.map { it.foodGroup }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    val foodStates: StateFlow<List<String>> = repository.itemsFlow.combine(_searchQuery) { items, _ ->
        listOf("All") + items.map { it.foodState }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    init {
        val firstValid = repository.getAllItems().firstOrNull { it.isPriceValid() }
        _calcSelectedItem.value = firstValid
    }

    fun navigateTo(destination: AppNavDestination) {
        _currentDestination.value = destination
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGroup(group: String) {
        _selectedGroup.value = group
    }

    fun setStateFilter(state: String) {
        _selectedStateFilter(state)
    }

    private fun _selectedStateFilter(state: String) {
        _selectedState.value = state
    }

    fun setVerificationFilter(status: VerificationStatus?) {
        _selectedVerificationFilter.value = status
    }

    fun selectItemForDetail(item: FoodItem?) {
        _selectedItemForDetail.value = item
    }

    fun setCalcSelectedItem(item: FoodItem) {
        _calcSelectedItem.value = item
    }

    fun setCalcPortionInput(input: String) {
        _calcPortionInput.value = input.filter { it.isDigit() || it == '.' }
    }

    fun computePriceCalculation(): ExplicitPriceCalculation {
        val item = _calcSelectedItem.value
            ?: return ExplicitPriceCalculation(
                isValid = false,
                errorMessage = "No food item selected."
            )

        val price = item.priceBdt
        val unitG = item.priceUnitG

        if (price == null || price <= 0.0 || unitG == null || unitG <= 0.0) {
            return ExplicitPriceCalculation(
                isValid = false,
                errorMessage = "Price calculation unavailable: Missing valid price (BDT) or reference unit weight (g) for '${item.foodNameEn}'. In accordance with research integrity rules, missing values are never filled with invented numbers.",
                unitDescription = item.priceUnitDescription ?: "Unavailable"
            )
        }

        val grams = _calcPortionInput.value.toDoubleOrNull()
        if (grams == null || grams <= 0.0) {
            return ExplicitPriceCalculation(
                isValid = false,
                errorMessage = "Please enter a valid positive portion size (e.g., 100 grams).",
                basePriceBdt = price,
                baseUnitGrams = unitG,
                unitDescription = item.priceUnitDescription ?: ""
            )
        }

        val costPerGram = price / unitG
        val portionCost = costPerGram * grams

        val costPer100Kcal = if (item.energyKcal100g != null && item.energyKcal100g > 0.0) {
            val costPerKcal = costPerGram / (item.energyKcal100g / 100.0)
            costPerKcal * 100.0
        } else null

        val costPer10gProtein = if (item.proteinG100g != null && item.proteinG100g > 0.0) {
            val costPerGProtein = costPerGram / (item.proteinG100g / 100.0)
            costPerGProtein * 10.0
        } else null

        val explanation = "Explicit conversion: ৳${price} per ${unitG}g = ৳${String.format("%.4f", costPerGram)}/g. For ${grams}g: ৳${String.format("%.2f", portionCost)}."

        return ExplicitPriceCalculation(
            isValid = true,
            portionGrams = grams,
            basePriceBdt = price,
            baseUnitGrams = unitG,
            costPerGramBdt = costPerGram,
            portionCostBdt = portionCost,
            costPer100KcalBdt = costPer100Kcal,
            costPer10gProteinBdt = costPer10gProtein,
            unitDescription = item.priceUnitDescription ?: "${unitG}g",
            formulaExplanation = explanation
        )
    }

    // Nutrition Summary Deterministic Calculations
    fun computeNutritionSummary(): DeterministicNutritionSummary {
        val basket = _nutritionBasket.value
        if (basket.isEmpty()) {
            return DeterministicNutritionSummary(formulaNote = "Ration basket is empty. Add food items to calculate nutrition summary.")
        }

        var totalWeight = 0.0
        var totalCost = 0.0
        var totalKcal = 0.0
        var totalProtein = 0.0
        var totalCarbs = 0.0
        var totalFat = 0.0
        var totalFibre = 0.0
        var missingNutrientCount = 0
        var missingPriceCount = 0

        for (entry in basket) {
            val food = entry.foodItem
            val grams = entry.portionGrams
            val factor = grams / 100.0

            totalWeight += grams

            if (food.isPriceValid()) {
                val costPerGram = (food.priceBdt ?: 0.0) / (food.priceUnitG ?: 1.0)
                totalCost += costPerGram * grams
            } else {
                missingPriceCount++
            }

            if (food.energyKcal100g != null) totalKcal += food.energyKcal100g * factor else missingNutrientCount++
            if (food.proteinG100g != null) totalProtein += food.proteinG100g * factor
            if (food.carbsG100g != null) totalCarbs += food.carbsG100g * factor
            if (food.fatG100g != null) totalFat += food.fatG100g * factor
            if (food.fibreG100g != null) totalFibre += food.fibreG100g * factor
        }

        val note = if (missingNutrientCount > 0 || missingPriceCount > 0) {
            "Note: $missingNutrientCount item(s) missing nutrition and $missingPriceCount item(s) missing price. Missing values were NOT fabricated or replaced by zeros."
        } else {
            "All items calculated deterministically from validated FCTB lab records."
        }

        return DeterministicNutritionSummary(
            totalWeightGrams = totalWeight,
            totalCostBdt = totalCost,
            totalEnergyKcal = totalKcal,
            totalProteinG = totalProtein,
            totalCarbsG = totalCarbs,
            totalFatG = totalFat,
            totalFibreG = totalFibre,
            itemsWithMissingNutrientsCount = missingNutrientCount,
            itemsWithMissingPriceCount = missingPriceCount,
            formulaNote = note
        )
    }

    fun addToBasket(food: FoodItem, grams: Double = 100.0) {
        val current = _nutritionBasket.value.toMutableList()
        current.add(BasketPortionItem(food, grams))
        _nutritionBasket.value = current
        _feedbackMessage.value = "Added ${food.foodNameEn} (${grams.toInt()}g) to Nutrition Summary."
    }

    fun updateBasketItemGrams(index: Int, grams: Double) {
        val current = _nutritionBasket.value.toMutableList()
        if (index in current.indices) {
            current[index] = current[index].copy(portionGrams = grams.coerceAtLeast(1.0))
            _nutritionBasket.value = current
        }
    }

    fun removeFromBasket(index: Int) {
        val current = _nutritionBasket.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _nutritionBasket.value = current
        }
    }

    fun clearBasket() {
        _nutritionBasket.value = emptyList()
    }

    fun setCsvInputText(text: String) {
        _csvInputText.value = text
    }

    fun parseCurrentCsv() {
        if (_csvInputText.value.isBlank()) {
            _csvWarnings.value = listOf("Please paste CSV content or load sample dataset first.")
            return
        }
        val (items, warnings) = repository.parseCsvText(_csvInputText.value)
        _csvPreviewItems.value = items
        _csvWarnings.value = warnings
    }

    fun loadEmptyTemplateCsv() {
        _csvInputText.value = repository.getEmptyTemplateCsv()
        parseCurrentCsv()
    }

    fun loadPopulatedBenchmarkCsv() {
        _csvInputText.value = repository.getPopulatedBenchmarkCsv()
        parseCurrentCsv()
    }

    fun commitCsvImport(replaceExisting: Boolean) {
        if (_csvPreviewItems.value.isEmpty()) {
            _feedbackMessage.value = "No parsed records to import. Please parse CSV first."
            return
        }
        val count = _csvPreviewItems.value.size
        repository.importCsv(_csvPreviewItems.value, replaceExisting)
        _csvPreviewItems.value = emptyList()
        _csvInputText.value = ""
        _feedbackMessage.value = "Successfully imported $count food records into database."
        _currentDestination.value = AppNavDestination.DATABASE
    }

    fun resetDatabaseToBaseline() {
        repository.resetToBaseline()
        _feedbackMessage.value = "Database restored to authenticated FCTB baseline dataset (30 items)."
    }

    fun addCustomRecord(item: FoodItem) {
        repository.addItem(item)
        _feedbackMessage.value = "New research entry '${item.foodNameEn}' successfully added."
        _showAddDialog.value = false
    }

    fun openAddDialog() {
        _showAddDialog.value = true
    }

    fun closeAddDialog() {
        _showAddDialog.value = false
    }

    fun toggleDisclaimerModal(show: Boolean) {
        _showDisclaimerModal.value = show
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }
}
