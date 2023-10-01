
data class SpendingItem(
    val name: String,
    val category: String,
    val cost: Float,
    val cost1: Float = 0F
)

val listOfSpendingItems = mutableListOf(
    SpendingItem(
        "Burger",
        "żywność",
        45.00F
    ),
    SpendingItem(
        "Klocki Lego",
        "zabawki",
        62.00F
    ),
    SpendingItem(
        "Klawiatura",
        "elektronika",
        10.00F
    ),
    SpendingItem(
        "Lipton",
        "żywność",
        4.50F
    ),
    SpendingItem(
        "Bolt",
        "transport",
        17.00F
    ),
    SpendingItem(
        "Bilet MPK",
        "transport",
        3.50F
    ),
    SpendingItem(
        "Energetyk",
        "żywność",
        3.50F
    ),
    SpendingItem(
        "adidas Ultraboost",
        "moda",
        750.00F
    ),
    SpendingItem(
        "Mieszkanie",
        "czynsz",
        1550.00F
    ),
)

data class LimitItem(
    val limitName: String,
    val currentExpenses: Int,
    val plannedExpenses: Int
)

val listOfLimitItem = listOf(
    LimitItem(
        "żywność",
        13,
        100
    ),

    LimitItem(
        "rozrywka",
        20,
        100
    ),

    LimitItem(
        "transport",
        50,
        100
    )
)