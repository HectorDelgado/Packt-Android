package com.soundbite.packt.util

sealed class ValidationError(msg: String) : Throwable(msg) {
    class DateOutOfRangeError(minDate: String, maxDate: String) :
        ValidationError("Date must be between $minDate and $maxDate")
    class IllegalCharacterError(character: Char) :
        ValidationError("Forbidden character $character not allowed!")
    class MaxLengthReachedError(maxLength: Int) :
        ValidationError("Max characters allowed is $maxLength!")
    class MissingFieldError(fieldName: String) :
        ValidationError("Field $fieldName is required.")
    class OutOfRangeError(minValue: Number, maxValue: Number) :
        ValidationError("Value must be between $minValue and $maxValue!")
}
