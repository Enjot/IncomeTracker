object Validator {

    fun isValidAmount(enteredValue: String): Boolean {
        return try {
            enteredValue.toDouble()
            true
        } catch (_: Exception) {
            false
        }
    }
    
}

