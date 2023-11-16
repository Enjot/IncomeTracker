class Validator {
    companion object {
        fun amountOfMoney(enteredValue: String): Boolean {
            return try {
                enteredValue.toDouble()
                true
            } catch (_: Exception) {
                false
            }
        }

        fun quantityOf(enteredValue: String): Boolean {
            return try {
                enteredValue.toInt()
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}

