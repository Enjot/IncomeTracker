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