object Validator {

    fun isValidAmount(enteredValue: String): Boolean {
        return try {
            enteredValue.toDouble()
            true
        } catch (_: Exception) {
            false
        }
    }

    fun isEmptyString(enteredValue: String): Boolean {
        return if (enteredValue.isEmpty()){
            false
        }else{
            true
        }
    }
}
