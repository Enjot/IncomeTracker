package ui.rightcontent

data class LimitItem(
    val limitName: String,
    val currentExpenses: Int,
    val plannedExpenses: Int
)

val listOfLimitItem = listOf(
    LimitItem(
        "żywność",
        13,
        1000
    ),

    LimitItem(
        "rozrywka",
        20,
        300
    ),

    LimitItem(
        "transport",
        30,
        150
    )
)