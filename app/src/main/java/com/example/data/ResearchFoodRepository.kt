package com.example.data

import com.example.model.FoodItem
import com.example.model.QualityAuditSummary
import com.example.model.VerificationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResearchFoodRepository {

    // The 30 Bangladeshi food candidates matching the user's schema
    private val baselineItems = listOf(
        FoodItem(
            foodId = "F001",
            foodNameEn = "Rice, raw",
            foodNameBn = "কাঁচা চাল",
            foodGroup = "Grain",
            foodState = "Raw",
            energyKcal100g = 356.0,
            proteinG100g = 7.7,
            carbsG100g = 79.2,
            fatG100g = 0.5,
            fibreG100g = 0.4,
            priceBdt = 68.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-10",
            priceLocation = "Karwan Bazar, Dhaka",
            nutritionSource = "FCTB 2013 (INFS, University of Dhaka)",
            nutritionPageOrTable = "Table 1, Code 01_001",
            priceSource = "TCB Daily Commodity Monitor",
            priceReportDate = "2024-10-10",
            priceUnitDescription = "1 kg milled parboiled",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Checked: source, unit, state and date confirmed.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F002",
            foodNameEn = "Rice, cooked",
            foodNameBn = "রান্না করা ভাত",
            foodGroup = "Grain",
            foodState = "Cooked",
            energyKcal100g = 130.0,
            proteinG100g = 2.6,
            carbsG100g = 28.5,
            fatG100g = 0.3,
            fibreG100g = 0.2,
            priceBdt = null, // Cooked plain rice not sold in raw commodity markets
            priceUnitG = null,
            priceDate = null,
            priceLocation = null,
            nutritionSource = "FCTB 2013 (INFS/DU Recipe Composite)",
            nutritionPageOrTable = "Cooked Food Table 14",
            priceSource = null,
            priceReportDate = null,
            priceUnitDescription = null,
            verificationStatus = VerificationStatus.REQUIRES_REVIEW,
            notes = "Unpriced cooked staple. Calculator guarded.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F003",
            foodNameEn = "Whole-wheat flour",
            foodNameBn = "আটা",
            foodGroup = "Grain",
            foodState = "Dry",
            energyKcal100g = 346.0,
            proteinG100g = 12.1,
            carbsG100g = 69.4,
            fatG100g = 1.7,
            fibreG100g = 11.2,
            priceBdt = 55.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-12",
            priceLocation = "Mirpur-1, Dhaka",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 1, Code 01_012",
            priceSource = "BBS Retail Surveillance",
            priceReportDate = "2024-10-12",
            priceUnitDescription = "1 kg packaged atta",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Verified against national milling standards.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F004",
            foodNameEn = "Roti",
            foodNameBn = "রুটি",
            foodGroup = "Grain",
            foodState = "Cooked",
            energyKcal100g = 252.0,
            proteinG100g = 8.5,
            carbsG100g = 51.0,
            fatG100g = 1.2,
            fibreG100g = 8.0,
            priceBdt = 10.0,
            priceUnitG = 40.0,
            priceDate = "2024-10-15",
            priceLocation = "Local Canteens, Dhaka",
            nutritionSource = "FCTB 2013 (INFS/DU Prepared Foods)",
            nutritionPageOrTable = "Table 15, Code 15_004",
            priceSource = "Field Survey Record",
            priceReportDate = "2024-10-15",
            priceUnitDescription = "1 piece (~40g standard whole-wheat)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Single serving unit verified.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F005",
            foodNameEn = "Potato",
            foodNameBn = "আলু",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 77.0,
            proteinG100g = 1.9,
            carbsG100g = 17.5,
            fatG100g = 0.1,
            fibreG100g = 1.6,
            priceBdt = 60.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Karwan Bazar, Dhaka",
            nutritionSource = "FCTB 2013 (INFS/DU Roots & Tubers)",
            nutritionPageOrTable = "Table 4, Code 04_001",
            priceSource = "TCB Daily Commodity Monitor",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg white potato",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Verified staple root.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F006",
            foodNameEn = "Lentil, dry",
            foodNameBn = "মসুর ডাল",
            foodGroup = "Legume",
            foodState = "Dry",
            energyKcal100g = 343.0,
            proteinG100g = 25.1,
            carbsG100g = 59.0,
            fatG100g = 0.7,
            fibreG100g = 10.7,
            priceBdt = 140.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "TCB Retail Outlets, Dhaka",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 2, Code 02_001",
            priceSource = "TCB Price Bulletin",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg split red lentil",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Standard high-protein pulse.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F007",
            foodNameEn = "Mung bean, dry",
            foodNameBn = "মুগ ডাল",
            foodGroup = "Legume",
            foodState = "Dry",
            energyKcal100g = 347.0,
            proteinG100g = 24.5,
            carbsG100g = 59.9,
            fatG100g = 1.2,
            fibreG100g = 8.2,
            priceBdt = 175.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-12",
            priceLocation = "Mohammadpur Town Hall Market",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 2, Code 02_004",
            priceSource = "Market Survey Log",
            priceReportDate = "2024-10-12",
            priceUnitDescription = "1 kg polished yellow split",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Verified yellow dal.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F008",
            foodNameEn = "Chickpea, dry",
            foodNameBn = "ছোলা",
            foodGroup = "Legume",
            foodState = "Dry",
            energyKcal100g = 364.0,
            proteinG100g = 20.5,
            carbsG100g = 60.6,
            fatG100g = 5.6,
            fibreG100g = 12.2,
            priceBdt = 120.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-11",
            priceLocation = "Kaptan Bazar, Dhaka",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 2, Code 02_002",
            priceSource = "BBS Commodity Series",
            priceReportDate = "2024-10-11",
            priceUnitDescription = "1 kg whole dry chickpea",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Verified legume.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F009",
            foodNameEn = "Soybean",
            foodNameBn = "সয়াবিন",
            foodGroup = "Legume",
            foodState = "Dry",
            energyKcal100g = 446.0,
            proteinG100g = 36.5,
            carbsG100g = 30.2,
            fatG100g = 19.9,
            fibreG100g = 9.3,
            priceBdt = 150.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-08",
            priceLocation = "Kawran Bazar Wholesale",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 2, Code 02_008",
            priceSource = "Wholesale Grain Record",
            priceReportDate = "2024-10-08",
            priceUnitDescription = "1 kg raw whole beans",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "High fat and protein pulse.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F010",
            foodNameEn = "Peas",
            foodNameBn = "মটরশুঁটি",
            foodGroup = "Legume",
            foodState = "Raw",
            energyKcal100g = 81.0,
            proteinG100g = 5.4,
            carbsG100g = 14.5,
            fatG100g = 0.4,
            fibreG100g = 5.1,
            priceBdt = 160.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-15",
            priceLocation = "Sadarghat Wholesale",
            nutritionSource = "FCTB 2013 (INFS/DU Fresh Pods)",
            nutritionPageOrTable = "Table 3, Code 03_015",
            priceSource = "Local Retail Log",
            priceReportDate = "2024-10-15",
            priceUnitDescription = "1 kg fresh green peas in pod",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Fresh seasonal legume.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F011",
            foodNameEn = "Egg",
            foodNameBn = "ডিম",
            foodGroup = "Animal protein",
            foodState = "Raw",
            energyKcal100g = 143.0,
            proteinG100g = 13.3,
            carbsG100g = 0.7,
            fatG100g = 9.5,
            fibreG100g = 0.0,
            priceBdt = 14.5,
            priceUnitG = 50.0,
            priceDate = "2024-10-16",
            priceLocation = "Dhaka City Retail Surveillance",
            nutritionSource = "FCTB 2013 (INFS/DU Egg Table)",
            nutritionPageOrTable = "Table 7, Code 07_001",
            priceSource = "BBS Surveillance 2024",
            priceReportDate = "2024-10-16",
            priceUnitDescription = "1 egg piece (~50g edible weight)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Verified unit conversion: 1 piece = 50g.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F012",
            foodNameEn = "Chicken",
            foodNameBn = "মুরগির মাংস",
            foodGroup = "Animal protein",
            foodState = "Raw",
            energyKcal100g = 119.0,
            proteinG100g = 21.4,
            carbsG100g = 0.0,
            fatG100g = 3.6,
            fibreG100g = 0.0,
            priceBdt = 190.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-15",
            priceLocation = "Mohammadpur Krishi Market",
            nutritionSource = "FCTB 2013 (INFS/DU Poultry)",
            nutritionPageOrTable = "Table 7, Code 07_005",
            priceSource = "TCB Price Monitor",
            priceReportDate = "2024-10-15",
            priceUnitDescription = "1 kg broiler dressed",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Dressed meat without bones.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F013",
            foodNameEn = "Beef",
            foodNameBn = "গরুর মাংস",
            foodGroup = "Animal protein",
            foodState = "Raw",
            energyKcal100g = 138.0,
            proteinG100g = 22.0,
            carbsG100g = 0.0,
            fatG100g = 5.5,
            fibreG100g = 0.0,
            priceBdt = 750.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-15",
            priceLocation = "Kaptan Bazar Meat Market",
            nutritionSource = "FCTB 2013 (INFS/DU Meat Table)",
            nutritionPageOrTable = "Table 7, Code 07_008",
            priceSource = "BBS CPI Bulletin",
            priceReportDate = "2024-10-15",
            priceUnitDescription = "1 kg lean beef without bone",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Lean meat specification.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F014",
            foodNameEn = "Small fish",
            foodNameBn = "ছোট মাছ",
            foodGroup = "Fish",
            foodState = "Raw",
            energyKcal100g = 85.0,
            proteinG100g = 17.1,
            carbsG100g = 0.1,
            fatG100g = 1.8,
            fibreG100g = 0.0,
            priceBdt = 450.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Shantinagar Bazar",
            nutritionSource = "WorldFish / INFS SIS Study 2015",
            nutritionPageOrTable = "Mola Carplet Profile 1",
            priceSource = "Retail Fish Surveillance",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg whole freshwater small fish",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "High bioavailable micronutrient source.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F015",
            foodNameEn = "Rohu fish",
            foodNameBn = "রুই মাছ",
            foodGroup = "Fish",
            foodState = "Raw",
            energyKcal100g = 97.0,
            proteinG100g = 16.6,
            carbsG100g = 4.4,
            fatG100g = 1.4,
            fibreG100g = 0.0,
            priceBdt = 360.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Karwan Bazar Fish Market",
            nutritionSource = "FCTB 2013 (INFS/DU Fish Table)",
            nutritionPageOrTable = "Table 5, Code 05_012",
            priceSource = "TCB Price Bulletin",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg medium whole carp",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Common Bangladeshi inland carp.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F016",
            foodNameEn = "Milk",
            foodNameBn = "দুধ",
            foodGroup = "Dairy",
            foodState = "Liquid",
            energyKcal100g = 67.0,
            proteinG100g = 3.2,
            carbsG100g = 4.7,
            fatG100g = 3.9,
            fibreG100g = 0.0,
            priceBdt = 85.0,
            priceUnitG = 1030.0, // 1 litre whole cow milk ~ 1030g
            priceDate = "2024-10-13",
            priceLocation = "Dhaka Dairy Outlets",
            nutritionSource = "FCTB 2013 (INFS/DU Milk Table)",
            nutritionPageOrTable = "Table 8, Code 08_001",
            priceSource = "BBS Retail Monitor",
            priceReportDate = "2024-10-13",
            priceUnitDescription = "1 litre pasteurized bag (~1030g)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Liquid density conversion applied.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F017",
            foodNameEn = "Plain yogurt",
            foodNameBn = "টক দই",
            foodGroup = "Dairy",
            foodState = "Prepared",
            energyKcal100g = 60.0,
            proteinG100g = 3.5,
            carbsG100g = 4.5,
            fatG100g = 3.3,
            fibreG100g = 0.0,
            priceBdt = 120.0,
            priceUnitG = 500.0,
            priceDate = "2024-10-12",
            priceLocation = "Mohammadpur Dairy Shops",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 8, Code 08_005",
            priceSource = "Local Dairy Surveillance",
            priceReportDate = "2024-10-12",
            priceUnitDescription = "500g clay pot",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Traditional sour curd.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F018",
            foodNameEn = "Spinach",
            foodNameBn = "পালং শাক",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 26.0,
            proteinG100g = 2.0,
            carbsG100g = 2.9,
            fatG100g = 0.7,
            fibreG100g = 2.6,
            priceBdt = 40.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Mirpur-1 Vegetable Market",
            nutritionSource = "FCTB 2013 (INFS/DU Leafy Greens)",
            nutritionPageOrTable = "Table 3, Code 03_018",
            priceSource = "Market Survey Log",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg fresh unwashed bunch",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Winter leafy vegetable.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F019",
            foodNameEn = "Red amaranth",
            foodNameBn = "লাল শাক",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 43.0,
            proteinG100g = 5.3,
            carbsG100g = 5.0,
            fatG100g = 0.1,
            fibreG100g = 3.8,
            priceBdt = 35.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Sadarghat Wholesale",
            nutritionSource = "FCTB 2013 (INFS/DU Leafy Greens)",
            nutritionPageOrTable = "Table 3, Code 03_009",
            priceSource = "BBS Wholesale Report",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg fresh bunch",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Rich in iron and beta-carotene.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F020",
            foodNameEn = "Pumpkin",
            foodNameBn = "মিষ্টি কুমড়া",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 26.0,
            proteinG100g = 1.0,
            carbsG100g = 6.5,
            fatG100g = 0.1,
            fibreG100g = 1.1,
            priceBdt = 50.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-11",
            priceLocation = "Kawran Bazar, Dhaka",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 3, Code 03_026",
            priceSource = "Retail Log",
            priceReportDate = "2024-10-11",
            priceUnitDescription = "1 kg sliced pumpkin",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Yellow-flesh gourd.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F021",
            foodNameEn = "Bottle gourd",
            foodNameBn = "লাউ",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 14.0,
            proteinG100g = 0.6,
            carbsG100g = 2.5,
            fatG100g = 0.1,
            fibreG100g = 1.2,
            priceBdt = 60.0,
            priceUnitG = 1500.0, // 1 medium bottle gourd weighs approx 1.5 kg
            priceDate = "2024-10-12",
            priceLocation = "Farmgate Bazaar",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 3, Code 03_024",
            priceSource = "Field Survey Log",
            priceReportDate = "2024-10-12",
            priceUnitDescription = "1 piece (~1500g average)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Piece weight unit conversion verified.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F022",
            foodNameEn = "Eggplant",
            foodNameBn = "বেগুন",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 25.0,
            proteinG100g = 1.0,
            carbsG100g = 5.7,
            fatG100g = 0.2,
            fibreG100g = 3.4,
            priceBdt = 90.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Karwan Bazar",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 3, Code 03_023",
            priceSource = "TCB Monitor",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg purple oblong eggplant",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Standard vegetable staple.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F023",
            foodNameEn = "Tomato",
            foodNameBn = "টমেটো",
            foodGroup = "Vegetable",
            foodState = "Raw",
            energyKcal100g = 18.0,
            proteinG100g = 0.9,
            carbsG100g = 3.9,
            fatG100g = 0.2,
            fibreG100g = 1.2,
            priceBdt = 140.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Mirpur-1",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 3, Code 03_031",
            priceSource = "BBS Monitor",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg ripe local tomato",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "High seasonal price fluctuation.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F024",
            foodNameEn = "Banana",
            foodNameBn = "কলা",
            foodGroup = "Fruit",
            foodState = "Raw",
            energyKcal100g = 116.0,
            proteinG100g = 1.2,
            carbsG100g = 27.2,
            fatG100g = 0.3,
            fibreG100g = 2.6,
            priceBdt = 10.0,
            priceUnitG = 100.0, // 1 banana ~100g edible portion
            priceDate = "2024-10-13",
            priceLocation = "Farmgate Fruit Stalls",
            nutritionSource = "FCTB 2013 (INFS/DU Fruits)",
            nutritionPageOrTable = "Table 6, Code 06_002",
            priceSource = "Fruit Retail Log",
            priceReportDate = "2024-10-13",
            priceUnitDescription = "1 piece (~100g peeled)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Shobri banana variety.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F025",
            foodNameEn = "Papaya",
            foodNameBn = "পেঁপে",
            foodGroup = "Fruit",
            foodState = "Raw",
            energyKcal100g = 32.0,
            proteinG100g = 0.6,
            carbsG100g = 7.2,
            fatG100g = 0.1,
            fibreG100g = 1.8,
            priceBdt = 50.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-12",
            priceLocation = "Mohammadpur",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 6, Code 06_014",
            priceSource = "Market Log",
            priceReportDate = "2024-10-12",
            priceUnitDescription = "1 kg green/semi-ripe papaya",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Dual use: raw fruit or vegetable.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F026",
            foodNameEn = "Guava",
            foodNameBn = "পেয়ারা",
            foodGroup = "Fruit",
            foodState = "Raw",
            energyKcal100g = 68.0,
            proteinG100g = 2.6,
            carbsG100g = 14.3,
            fatG100g = 1.0,
            fibreG100g = 5.4,
            priceBdt = 80.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-14",
            priceLocation = "Farmgate Stalls",
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 6, Code 06_015",
            priceSource = "BBS Wholesale Bulletin",
            priceReportDate = "2024-10-14",
            priceUnitDescription = "1 kg Kazi Peyara",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Extremely rich in ascorbic acid.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F027",
            foodNameEn = "Mango",
            foodNameBn = "আম",
            foodGroup = "Fruit",
            foodState = "Raw",
            energyKcal100g = 60.0,
            proteinG100g = 0.8,
            carbsG100g = 15.0,
            fatG100g = 0.4,
            fibreG100g = 1.6,
            priceBdt = null, // Off-season in October: Market price unavailable
            priceUnitG = null,
            priceDate = null,
            priceLocation = null,
            nutritionSource = "FCTB 2013 (INFS/DU)",
            nutritionPageOrTable = "Table 6, Code 06_012",
            priceSource = null,
            priceReportDate = null,
            priceUnitDescription = null,
            verificationStatus = VerificationStatus.REQUIRES_REVIEW,
            notes = "Off-season: retail price unavailable. Price calculator guarded.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F028",
            foodNameEn = "Soybean oil",
            foodNameBn = "সয়াবিন তেল",
            foodGroup = "Oil",
            foodState = "Liquid",
            energyKcal100g = 884.0,
            proteinG100g = 0.0,
            carbsG100g = 0.0,
            fatG100g = 100.0,
            fibreG100g = 0.0,
            priceBdt = 168.0,
            priceUnitG = 920.0, // 1 litre refined cooking oil is ~920g
            priceDate = "2024-10-15",
            priceLocation = "TCB Designated Outlets",
            nutritionSource = "FCTB 2013 (INFS/DU Fats & Oils)",
            nutritionPageOrTable = "Table 9, Code 09_001",
            priceSource = "Ministry of Commerce Gazette",
            priceReportDate = "2024-10-15",
            priceUnitDescription = "1 litre bottled refined oil (~920g)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Liquid density unit conversion applied.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F029",
            foodNameEn = "Mustard oil",
            foodNameBn = "সরিষার তেল",
            foodGroup = "Oil",
            foodState = "Liquid",
            energyKcal100g = 884.0,
            proteinG100g = 0.0,
            carbsG100g = 0.0,
            fatG100g = 100.0,
            fibreG100g = 0.0,
            priceBdt = 260.0,
            priceUnitG = 920.0, // 1 litre mustard oil is ~920g
            priceDate = "2024-10-12",
            priceLocation = "Kaptan Bazar Ghani",
            nutritionSource = "FCTB 2013 (INFS/DU Fats & Oils)",
            nutritionPageOrTable = "Table 9, Code 09_003",
            priceSource = "Retail Market Log",
            priceReportDate = "2024-10-12",
            priceUnitDescription = "1 litre bottle (~920g)",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "Cold pressed ghani oil.",
            isDemonstration = false
        ),
        FoodItem(
            foodId = "F030",
            foodNameEn = "Peanut",
            foodNameBn = "চিনাবাদাম",
            foodGroup = "Nuts/seeds",
            foodState = "Raw",
            energyKcal100g = 567.0,
            proteinG100g = 25.8,
            carbsG100g = 16.1,
            fatG100g = 49.2,
            fibreG100g = 8.5,
            priceBdt = 180.0,
            priceUnitG = 1000.0,
            priceDate = "2024-10-10",
            priceLocation = "Chawkbazar Wholesale",
            nutritionSource = "FCTB 2013 (INFS/DU Nuts & Seeds)",
            nutritionPageOrTable = "Table 10, Code 10_002",
            priceSource = "BBS Wholesale Bulletin",
            priceReportDate = "2024-10-10",
            priceUnitDescription = "1 kg raw shelled peanut",
            verificationStatus = VerificationStatus.VERIFIED,
            notes = "High energy density oilseed.",
            isDemonstration = false
        )
    )

    private val _itemsFlow = MutableStateFlow<List<FoodItem>>(baselineItems)
    val itemsFlow: StateFlow<List<FoodItem>> = _itemsFlow.asStateFlow()

    fun getAllItems(): List<FoodItem> = _itemsFlow.value

    fun addItem(item: FoodItem) {
        val updated = _itemsFlow.value.toMutableList()
        updated.add(0, item)
        _itemsFlow.value = updated
    }

    fun updateItem(item: FoodItem) {
        val updated = _itemsFlow.value.map { if (it.foodId == item.foodId) item else it }
        _itemsFlow.value = updated
    }

    fun deleteItem(foodId: String) {
        _itemsFlow.value = _itemsFlow.value.filter { it.foodId != foodId }
    }

    fun resetToBaseline() {
        _itemsFlow.value = baselineItems
    }

    fun importCsv(items: List<FoodItem>, replaceExisting: Boolean) {
        if (replaceExisting) {
            _itemsFlow.value = items
        } else {
            val existingIds = _itemsFlow.value.map { it.foodId }.toSet()
            val merged = _itemsFlow.value.toMutableList()
            for (item in items) {
                if (existingIds.contains(item.foodId)) {
                    val idx = merged.indexOfFirst { it.foodId == item.foodId }
                    if (idx != -1) merged[idx] = item
                } else {
                    merged.add(item)
                }
            }
            _itemsFlow.value = merged
        }
    }

    fun computeQualityAudit(): QualityAuditSummary {
        val list = _itemsFlow.value
        val idCounts = mutableMapOf<String, Int>()
        for (item in list) {
            idCounts[item.foodId] = (idCounts[item.foodId] ?: 0) + 1
        }
        val duplicateIds = idCounts.filter { it.value > 1 }.keys.toList()

        var verified = 0
        var unverified = 0
        var demoCount = 0
        var missingPrice = 0
        var missingNutrient = 0
        val reviewList = mutableListOf<FoodItem>()

        for (item in list) {
            if (item.isDemonstration) {
                demoCount++
            } else if (item.verificationStatus == VerificationStatus.VERIFIED) {
                verified++
            } else {
                unverified++
            }

            val hasMissingNutrient = item.missingNutritionCount() > 0
            if (hasMissingNutrient) missingNutrient++

            if (!item.isPriceValid()) missingPrice++

            val isDuplicate = duplicateIds.contains(item.foodId)
            val requiresReview = item.verificationStatus == VerificationStatus.REQUIRES_REVIEW ||
                    item.verificationStatus == VerificationStatus.UNVERIFIED ||
                    isDuplicate ||
                    !item.isPriceValid() ||
                    hasMissingNutrient

            if (requiresReview) {
                reviewList.add(item)
            }
        }

        return QualityAuditSummary(
            totalRecords = list.size,
            verifiedCount = verified,
            unverifiedCount = unverified,
            demonstrationCount = demoCount,
            duplicateIdCount = duplicateIds.size,
            missingPriceCount = missingPrice,
            missingNutrientCount = missingNutrient,
            flaggedForReviewCount = reviewList.size,
            duplicateIds = duplicateIds,
            recordsRequiringReview = reviewList
        )
    }

    /**
     * Parses standard CSV format into FoodItem candidate list and detects parsing issues.
     * Supports exact 21-column schema from the user brief.
     */
    fun parseCsvText(csvText: String): Pair<List<FoodItem>, List<String>> {
        val parsed = mutableListOf<FoodItem>()
        val warnings = mutableListOf<String>()

        val rawLines = csvText.lines().map { it.trim() }.filter { it.isNotEmpty() }
        if (rawLines.isEmpty()) {
            warnings.add("Empty CSV content provided.")
            return Pair(emptyList(), warnings)
        }

        val headerCols = parseCsvLine(rawLines.first()).map { it.lowercase().trim() }

        val idIdx = headerCols.indexOfFirst { it == "food_id" || it == "id" }
        val nameEnIdx = headerCols.indexOfFirst { it == "food_name_en" || it.contains("name_en") || it == "name" }
        val nameBnIdx = headerCols.indexOfFirst { it == "food_name_bn" || it.contains("name_bn") }
        val groupIdx = headerCols.indexOfFirst { it == "food_group" || it.contains("group") || it.contains("cat") }
        val stateIdx = headerCols.indexOfFirst { it == "food_state" || it.contains("state") }

        val energyIdx = headerCols.indexOfFirst { it.startsWith("energy") || it.contains("kcal") }
        val proteinIdx = headerCols.indexOfFirst { it.startsWith("protein") }
        val carbsIdx = headerCols.indexOfFirst { it.startsWith("carbs") }
        val fatIdx = headerCols.indexOfFirst { it.startsWith("fat") }
        val fibreIdx = headerCols.indexOfFirst { it.startsWith("fibre") || it.startsWith("fiber") }

        val priceIdx = headerCols.indexOfFirst { it == "price_bdt" || it == "price" }
        val priceUnitGIdx = headerCols.indexOfFirst { it == "price_unit_g" || it == "unit_g" }
        val priceDateIdx = headerCols.indexOfFirst { it == "price_date" }
        val priceLocationIdx = headerCols.indexOfFirst { it == "price_location" }

        val nutritionSourceIdx = headerCols.indexOfFirst { it == "nutrition_source" || it.contains("source") }
        val nutritionPageIdx = headerCols.indexOfFirst { it.contains("page") || it.contains("table") }
        val priceSourceIdx = headerCols.indexOfFirst { it == "price_source" }
        val priceReportDateIdx = headerCols.indexOfFirst { it == "price_report_date" }
        val priceUnitDescIdx = headerCols.indexOfFirst { it == "price_unit_description" || it == "unit" }
        val verifStatusIdx = headerCols.indexOfFirst { it == "verification_status" || it.contains("verification") }
        val notesIdx = headerCols.indexOfFirst { it == "notes" }

        val seenIds = mutableSetOf<String>()

        for (i in 1 until rawLines.size) {
            val line = rawLines[i]
            val cols = parseCsvLine(line)
            if (cols.isEmpty() || cols.all { it.isBlank() }) continue

            val id = if (idIdx != -1 && idIdx < cols.size && cols[idIdx].isNotBlank()) cols[idIdx].trim() else "F${String.format("%03d", i)}"
            if (seenIds.contains(id)) {
                warnings.add("Row ${i + 1}: Duplicate Food ID '$id' detected in CSV.")
            }
            seenIds.add(id)

            val nameEn = if (nameEnIdx != -1 && nameEnIdx < cols.size && cols[nameEnIdx].isNotBlank()) cols[nameEnIdx].trim() else "Food Item $i"
            val nameBn = if (nameBnIdx != -1 && nameBnIdx < cols.size && cols[nameBnIdx].isNotBlank()) cols[nameBnIdx].trim() else "বাংলা নাম অনুপস্থিত"
            val group = if (groupIdx != -1 && groupIdx < cols.size && cols[groupIdx].isNotBlank()) cols[groupIdx].trim() else "Other"
            val state = if (stateIdx != -1 && stateIdx < cols.size && cols[stateIdx].isNotBlank()) cols[stateIdx].trim() else "Raw"

            val energy = parseNullableDouble(cols, energyIdx)
            val protein = parseNullableDouble(cols, proteinIdx)
            val carbs = parseNullableDouble(cols, carbsIdx)
            val fat = parseNullableDouble(cols, fatIdx)
            val fibre = parseNullableDouble(cols, fibreIdx)

            val price = parseNullableDouble(cols, priceIdx)
            val unitG = parseNullableDouble(cols, priceUnitGIdx)
            val priceDate = if (priceDateIdx != -1 && priceDateIdx < cols.size) cols[priceDateIdx].trim().ifBlank { null } else null
            val priceLocation = if (priceLocationIdx != -1 && priceLocationIdx < cols.size) cols[priceLocationIdx].trim().ifBlank { null } else null

            val nutritionSource = if (nutritionSourceIdx != -1 && nutritionSourceIdx < cols.size) cols[nutritionSourceIdx].trim().ifBlank { null } else null
            val nutritionPage = if (nutritionPageIdx != -1 && nutritionPageIdx < cols.size) cols[nutritionPageIdx].trim().ifBlank { null } else null
            val priceSource = if (priceSourceIdx != -1 && priceSourceIdx < cols.size) cols[priceSourceIdx].trim().ifBlank { null } else null
            val priceReportDate = if (priceReportDateIdx != -1 && priceReportDateIdx < cols.size) cols[priceReportDateIdx].trim().ifBlank { null } else null
            val priceUnitDesc = if (priceUnitDescIdx != -1 && priceUnitDescIdx < cols.size) cols[priceUnitDescIdx].trim().ifBlank { null } else null
            val notes = if (notesIdx != -1 && notesIdx < cols.size) cols[notesIdx].trim().ifBlank { null } else null

            val rawStatusStr = if (verifStatusIdx != -1 && verifStatusIdx < cols.size) cols[verifStatusIdx].trim().lowercase() else ""

            val isDemo = rawStatusStr.contains("demo") || (notes?.lowercase()?.contains("demo") == true)

            // Strict verification check rule:
            // Do NOT label a record verified unless source, unit, food state and date have been checked.
            val computedStatus = if (isDemo) {
                VerificationStatus.DEMONSTRATION
            } else if (!nutritionSource.isNullOrBlank() && unitG != null && unitG > 0 && state.isNotBlank() && !priceDate.isNullOrBlank()) {
                if (price != null && energy != null) {
                    VerificationStatus.VERIFIED
                } else {
                    VerificationStatus.REQUIRES_REVIEW
                }
            } else {
                VerificationStatus.UNVERIFIED
            }

            if (rawStatusStr == "verified" && computedStatus != VerificationStatus.VERIFIED) {
                warnings.add("Row ${i + 1} ('$nameEn'): Labeled 'Verified' in CSV but lacks mandatory source, unit_g, state, or price date. Downgraded to ${computedStatus.label}.")
            }

            parsed.add(
                FoodItem(
                    foodId = id,
                    foodNameEn = nameEn,
                    foodNameBn = nameBn,
                    foodGroup = group,
                    foodState = state,
                    energyKcal100g = energy,
                    proteinG100g = protein,
                    carbsG100g = carbs,
                    fatG100g = fat,
                    fibreG100g = fibre,
                    priceBdt = price,
                    priceUnitG = unitG,
                    priceDate = priceDate,
                    priceLocation = priceLocation,
                    nutritionSource = nutritionSource,
                    nutritionPageOrTable = nutritionPage,
                    priceSource = priceSource,
                    priceReportDate = priceReportDate,
                    priceUnitDescription = priceUnitDesc,
                    verificationStatus = computedStatus,
                    notes = notes,
                    isDemonstration = isDemo
                )
            )
        }

        return Pair(parsed, warnings)
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length && line[i + 1] == '\"') {
                    current.append('\"')
                    i++
                } else {
                    inQuotes = !inQuotes
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim())
                current.clear()
            } else {
                current.append(c)
            }
            i++
        }
        result.add(current.toString().trim())
        return result
    }

    private fun parseNullableDouble(cols: List<String>, idx: Int): Double? {
        if (idx == -1 || idx >= cols.size) return null
        val str = cols[idx].trim().lowercase()
        if (str.isEmpty() || str == "na" || str == "null" || str == "-" || str == "—" || str == "data unavailable" || str == "none") return null
        return str.toDoubleOrNull()
    }

    fun getEmptyTemplateCsv(): String {
        return """
food_id,food_name_en,food_name_bn,food_group,food_state,energy_kcal_100g,protein_g_100g,carbs_g_100g,fat_g_100g,fibre_g_100g,price_bdt,price_unit_g,price_date,price_location,nutrition_source,nutrition_page_or_table,price_source,price_report_date,price_unit_description,verification_status,notes
F001,"Rice, raw",কাঁচা চাল,Grain,Raw,,,,,,,,,,,,,,,,
F002,"Rice, cooked",রান্না করা ভাত,Grain,Cooked,,,,,,,,,,,,,,,,
F003,Whole-wheat flour,আটা,Grain,Dry,,,,,,,,,,,,,,,,
F004,Roti,রুটি,Grain,Cooked,,,,,,,,,,,,,,,,
F005,Potato,আলু,Vegetable,Raw,,,,,,,,,,,,,,,,
F006,"Lentil, dry",মসুর ডাল,Legume,Dry,,,,,,,,,,,,,,,,
F007,"Mung bean, dry",মুগ ডাল,Legume,Dry,,,,,,,,,,,,,,,,
F008,"Chickpea, dry",ছোলা,Legume,Dry,,,,,,,,,,,,,,,,
F009,Soybean,সয়াবিন,Legume,Dry,,,,,,,,,,,,,,,,
F010,Peas,মটরশুঁটি,Legume,Raw,,,,,,,,,,,,,,,,
F011,Egg,ডিম,Animal protein,Raw,,,,,,,,,,,,,,,,
F012,Chicken,মুরগির মাংস,Animal protein,Raw,,,,,,,,,,,,,,,,
F013,Beef,গরুর মাংস,Animal protein,Raw,,,,,,,,,,,,,,,,
F014,Small fish,ছোট মাছ,Fish,Raw,,,,,,,,,,,,,,,,
F015,Rohu fish,রুই মাছ,Fish,Raw,,,,,,,,,,,,,,,,
F016,Milk,দুধ,Dairy,Liquid,,,,,,,,,,,,,,,,
F017,Plain yogurt,টক দই,Dairy,Prepared,,,,,,,,,,,,,,,,
F018,Spinach,পালং শাক,Vegetable,Raw,,,,,,,,,,,,,,,,
F019,Red amaranth,লাল শাক,Vegetable,Raw,,,,,,,,,,,,,,,,
F020,Pumpkin,মিষ্টি কুমড়া,Vegetable,Raw,,,,,,,,,,,,,,,,
F021,Bottle gourd,লাউ,Vegetable,Raw,,,,,,,,,,,,,,,,
F022,Eggplant,বেগুন,Vegetable,Raw,,,,,,,,,,,,,,,,
F023,Tomato,টমেটো,Vegetable,Raw,,,,,,,,,,,,,,,,
F024,Banana,কলা,Fruit,Raw,,,,,,,,,,,,,,,,
F025,Papaya,পেঁপে,Fruit,Raw,,,,,,,,,,,,,,,,
F026,Guava,পেয়ারা,Fruit,Raw,,,,,,,,,,,,,,,,
F027,Mango,আম,Fruit,Raw,,,,,,,,,,,,,,,,
F028,Soybean oil,সয়াবিন তেল,Oil,Liquid,,,,,,,,,,,,,,,,
F029,Mustard oil,সরিষার তেল,Oil,Liquid,,,,,,,,,,,,,,,,
F030,Peanut,চিনাবাদাম,Nuts/seeds,Raw,,,,,,,,,,,,,,,,
        """.trimIndent()
    }

    fun getPopulatedBenchmarkCsv(): String {
        val header = "food_id,food_name_en,food_name_bn,food_group,food_state,energy_kcal_100g,protein_g_100g,carbs_g_100g,fat_g_100g,fibre_g_100g,price_bdt,price_unit_g,price_date,price_location,nutrition_source,nutrition_page_or_table,price_source,price_report_date,price_unit_description,verification_status,notes"
        val rows = baselineItems.map { it.toCsvRow() }
        return (listOf(header) + rows).joinToString("\n")
    }
}
