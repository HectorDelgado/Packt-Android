package com.soundbite.packt.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbite.packt.db.Dog
import com.soundbite.packt.db.DogOwner
import com.soundbite.packt.db.OwnerDogDao
import com.soundbite.packt.domain.RetrofitServiceBuilder
import com.soundbite.packt.network.DogApi
import com.soundbite.packt.util.ValidationError
import com.soundbite.packt.util.WeightConversions
import com.soundbite.packt.util.singleValueEvent
import java.text.DateFormatSymbols
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class UserViewModel(private val ownerDogDao: OwnerDogDao) : ViewModel() {
    companion object {
        const val DATABASE_PATH_USERS   = "users"
        const val DATABASE_PATH_DOGS    = "dogs"
    }

    private var _firstName: String = ""
    private var _lastName: String = ""
    private var _username: String = ""
    private var _bio: String = ""
    private var _birthDay: Int = 0
    private var _birthMonth: Int = 0
    private var _birthYear: Int = 0

    private var dogName: String = ""
    private var dogBreed: String = ""
    private var dogBreedID: Int = 0
    private var dogBirthDay: Int = 0
    private var dogBirthMonth: Int = 0
    private var dogBirthYear: Int = 0
    private var dogWeightInPounds: Double = 0.0
    private var dogWeightInKilograms: Double = 0.0
    private var dogSex: String = ""
    private var dogBio: String = ""

    private val _dogs = mutableListOf<Dog>()

    // Regex for first/last name to only allow letters and spaces
    private val nameRegex = Regex("^[a-zA-Z\\s]+$")

    // Regex for username that only allows alphanumeric characters
    // and underscores, but consecutive underscores and strings
    // that begin or end with underscores are not allowed.
    private val usernameRegex = Regex("^[a-zA-Z0-9]+(_[a-zA-Z0-9]+)*\$")

    // Regex for bio that only allows alphanumeric characters, spaces and a period.
    private val bioRegex = Regex("^[.a-zA-Z0-9\\s]+\$")

    private val calendar = Calendar.getInstance()
    private val birthDayRange: IntRange by lazy {
        IntRange(1, getMaxDay())
    }
    private val birthMonthRange: IntRange by lazy {
        IntRange(1, getMaxMonth())
    }
    private val birthYearRange: IntRange by lazy {
        IntRange(1950, getMaxYear())
    }
    private val weightInPoundsLowerLimit = 1.0
    private val weightInPoundsUpperLimit = 400.0
    private val weightInKilogramsLowerLimit = 0.453592
    private val weightInKilogramsUpperLimit = 181.437

    // Reference to the Firebase realtime database
    val remoteDateBase = Firebase.database.reference

    // Entry point of the Firebase authentication SDK
    private val auth = Firebase.auth

    // Base URL for TheDogApi
    private val dogApiBaseUrl = "https://api.thedogapi.com/v1/"

    // Creates a single builder for instantiating our API
    private val serviceBuilder = RetrofitServiceBuilder.getInstance(dogApiBaseUrl)

    // Creates an implementation of the API endpoints defined by the interface
    private val dogApiService = serviceBuilder.createService(DogApi::class.java)

    private val TAG = "T-${javaClass.simpleName}"

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser
    val firstName: String
        get() = _firstName
    val lastName: String
        get() = _lastName
    val username: String
        get() = _username
    val bio: String
        get() = _bio
    val birthDay: Int
        get() = _birthDay
    val birthMonth: Int
        get() = _birthMonth
    val birthYear: Int
        get() = _birthYear
    val dogs: List<Dog>
        get() = _dogs

    ////////////////////////////
    //  Room database operations
    ////////////////////////////

    fun getDogOwner(): Flow<DogOwner> = flow {
        emit(ownerDogDao.getDogOwner())
    }
    fun getDogs(): Flow<List<Dog>> = flow {
        emit(ownerDogDao.getDogs())
    }
    fun insertDogOwner(dogOwner: DogOwner) = viewModelScope.launch {
        ownerDogDao.insertOwner(dogOwner)
    }
    fun insertDog(dog: Dog) = viewModelScope.launch {
        ownerDogDao.insertDog(dog)
    }
    fun insertDogs(dogs: List<Dog>) = viewModelScope.launch {
        ownerDogDao.insertDogs(dogs)
    }
    fun insertDogOwnerAndDogs(dogOwner: DogOwner, dogs: List<Dog>) = viewModelScope.launch {
        ownerDogDao.insertOwner(dogOwner)
        ownerDogDao.insertDogs(dogs)
    }
    fun clearDatabase(onCompletion: () -> Unit) = viewModelScope.launch {
        ownerDogDao.deleteUser()
        ownerDogDao.deleteAllDogs()
        onCompletion()
    }

    ////////////////////////////
    //  User validation
    ////////////////////////////

    fun setFirstName(name: String, maxCharacters: Int, validationResult: (Result<String>) -> Unit) {
        Timber.tag(TAG).d("Entering setFistName")
        validateStringField(name, maxCharacters, nameRegex) { result ->
            result.onSuccess {
                Timber.tag(TAG).d("setFistName success")
                _firstName = it
                validationResult(Result.success(it))
            }
            result.onFailure {
                Timber.tag(TAG).d("Entering setFistName fail")
                validationResult(Result.failure(it))
            }
        }
    }

    fun setLastName(name: String, maxCharacters: Int, validationResult: (Result<String>) -> Unit) {
        validateStringField(name, maxCharacters, nameRegex) { result ->
            result.onSuccess {
                _lastName = it
                validationResult(Result.success(it))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    fun setUsername(name: String, maxCharacters: Int, validationResult: (Result<String>) -> Unit) {
        Timber.tag(TAG).d("Enter setUsername!")
        validateStringField(name, maxCharacters, usernameRegex) { result ->
            result.onSuccess {
                Timber.tag(TAG).d("successful setUsername!")
                _username = it
                validationResult(Result.success(it.trim()))
            }
            result.onFailure {
                Timber.tag(TAG).d("unsuccessful setUsername!")
                validationResult(Result.failure(it))
            }
        }
    }

    fun setBio(bio: String, maxCharacters: Int, validationResult: (Result<String>) -> Unit) {
        validateStringField(bio, maxCharacters, null) { result ->
            result.onSuccess {
                _bio = it
                validationResult(Result.success(it.trim()))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    /**
     * Attempts to validate and set the date of birth.
     * If the date passes validation, a formatted data is passed into the Result lambda.
     *
     * @param dayOfMonth The day of the month to be validated.
     * @param month The month to be validated.
     * @param year The year to be validated.
     * @param validationResult A Lambda function that is passed the success of the operation.
     */
    fun setDateOfBirth(
        dayOfMonth: Int,
        month: Int,
        year: Int,
        validationResult: (Result<String>) -> Unit
    ) {
        if (dayOfMonth in birthDayRange && month in birthMonthRange && year in birthYearRange) {
            val formattedDate =
                "${"%02d".format(dayOfMonth)}/${"%02d".format(month)}/${"%04d".format(year)}"
            _birthDay = dayOfMonth
            _birthMonth = month
            _birthYear = year
            validationResult(Result.success(formattedDate))
        } else {
            val minDate = "${DateFormatSymbols().months[birthMonthRange.first - 1]} " +
                "${birthDayRange.first}, ${birthYearRange.first}"
            val maxDate = "${DateFormatSymbols().months[birthMonthRange.last - 1]} " +
                "${birthDayRange.last} ${birthYearRange.last}"
            validationResult(Result.failure(ValidationError.DateOutOfRangeError(minDate, maxDate)))
        }
    }

    ////////////////////////////
    //  Dog validation
    ////////////////////////////

    fun setDogName(
        name: String,
        maxCharacters: Int,
        validationResult: (Result<String>) -> Unit
    ) {
        if (name.isNotEmpty()) {
            validateStringField(name, maxCharacters, nameRegex) { result ->
                result.onSuccess {
                    dogName = it
                    validationResult(Result.success(it))
                }
                result.onFailure {
                    validationResult(Result.failure(it))
                }
            }
        }
    }

    fun setDogBreed(
        breed: String,
        breedID: Int,
        maxCharacters: Int,
        validationResult: (Result<String>) -> Unit
    ) {
        validateStringField(breed, maxCharacters) { result ->
            result.onSuccess {
                dogBreed = it
                dogBreedID = breedID
                validationResult(Result.success(it))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    fun setDogDateOfBirth(
        day: Int,
        month: Int,
        year: Int,
        validationResult: (Result<String>) -> Unit
    ) {
        // TODO create a date validation method
        if (day in birthDayRange && month in birthMonthRange && year in birthYearRange) {
            val formattedDate = "${"%02d".format(day)}/${"%02d".format(month)}/${"%04d".format(year)}"
            dogBirthDay = day
            dogBirthMonth = month
            dogBirthYear = year
            validationResult(Result.success(formattedDate))
        } else {
            val minDate = "${DateFormatSymbols().months[birthMonthRange.first - 1]} " +
                    "${birthDayRange.first}, ${birthYearRange.first}"
            val maxDate = "${DateFormatSymbols().months[birthMonthRange.last - 1]} " +
                    "${birthDayRange.last} ${birthYearRange.last}"
            validationResult(Result.failure(ValidationError.DateOutOfRangeError(minDate, maxDate)))
        }
    }

    fun setDogWeight(
        weight: Double,
        inPounds: Boolean,
        validationResult: (Result<Double>) -> Unit
    ) {
        validateDoubleField(weight, 0.0, 400.0) { result ->
            result.onSuccess {
                when (inPounds) {
                    true -> {
                        dogWeightInPounds = weight
                        dogWeightInKilograms = WeightConversions.poundsToKilograms(weight)
                    }
                    false -> {
                        dogWeightInKilograms = weight
                        dogWeightInPounds = WeightConversions.kilogramsToPounds(weight)
                    }
                }
                validationResult(Result.success(it))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    fun setDogWeightInPounds(
        weight: Double,
        validationResult: (Result<Double>) -> Unit
    ) {
        validateDoubleField(weight, weightInPoundsLowerLimit, weightInPoundsUpperLimit) { result ->
            result.onSuccess {
                dogWeightInPounds = it
                dogWeightInKilograms = WeightConversions.poundsToKilograms(weight)
                validationResult(Result.success(it))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    fun setDogWeightInKilograms(
        weight: Double,
        validationResult: (Result<Double>) -> Unit
    ) {
        validateDoubleField(weight, weightInKilogramsLowerLimit, weightInKilogramsUpperLimit) { result ->
            result.onSuccess {
                dogWeightInKilograms = it
                dogWeightInPounds = WeightConversions.kilogramsToPounds(weight)
                validationResult(Result.success(it))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    fun setDogSex(sex: String) {
        dogSex = sex
    }

    fun setDogBio(bio: String, maxCharacters: Int, validationResult: (Result<String>) -> Unit) {
        validateStringField(bio, maxCharacters, bioRegex) { result ->
            result.onSuccess {
                dogBio = it
                validationResult(Result.success(it))
            }
            result.onFailure {
                validationResult(Result.failure(it))
            }
        }
    }

    ////////////////////////////
    //  Builders
    ////////////////////////////

    fun buildUser(result: (Result<DogOwner>) -> Unit) {
        when {
            currentUser == null -> {
                result(Result.failure(ValidationError.MissingFieldError("user??")))
            }
            _firstName.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("first name")))
            }
            _lastName.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("last name")))
            }
            _username.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("username")))
            }
            (_birthDay !in birthDayRange) -> {
                result(Result.failure(ValidationError.MissingFieldError("birth day")))
            }
            (_birthMonth !in birthMonthRange) -> {
                result(Result.failure(ValidationError.MissingFieldError("birth month")))
            }
            (_birthYear !in birthYearRange) -> {
                result(Result.failure(ValidationError.MissingFieldError("birth year")))
            }
            else -> {
                currentUser?.also {
                    result(
                        Result.success(
                            DogOwner(
                                it.uid,
                                _username,
                                _firstName,
                                _lastName,
                                _bio,
                                _birthDay,
                                _birthMonth,
                                _birthYear,
                                _dogs.map { dog -> dog.uid },
                                System.currentTimeMillis() / 1000,
                                System.currentTimeMillis() / 1000,
                                null
                            )
                        )
                    )
                }
            }
        }
    }

    fun buildDog(result: (Result<Dog>) -> Unit) {
        when {
            dogName.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("Name")))
            }
            dogBreedID == 0 || dogBreed.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("Breed")))
            }
            dogBirthDay !in birthDayRange || dogBirthMonth !in birthMonthRange || dogBirthYear !in birthYearRange -> {
                result(Result.failure(ValidationError.MissingFieldError("Date of Birth")))
            }
            dogWeightInPounds !in weightInPoundsLowerLimit..weightInPoundsUpperLimit
                    || dogWeightInKilograms !in weightInKilogramsLowerLimit..weightInKilogramsUpperLimit -> {
                result(Result.failure(ValidationError.MissingFieldError("Weight")))
            }
            dogSex.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("Sex")))
            }
            dogBio.isEmpty() -> {
                result(Result.failure(ValidationError.MissingFieldError("Bio")))
            }
            else -> {
                result(
                    Result.success(
                        Dog(
                            UUID.randomUUID().toString(),
                            dogBreedID,
                            System.currentTimeMillis() / 1000,
                            dogName,
                            dogBio,
                            dogBirthDay,
                            dogBirthMonth,
                            dogBirthYear,
                            dogBreed,
                            dogWeightInPounds,
                            dogWeightInKilograms,
                            dogSex
                        )
                    )
                )
            }
        }
    }

    fun addDog(dog: Dog) {
        _dogs.add(dog)
    }

    ////////////////////////////
    //  The Dog API service
    ////////////////////////////
    suspend fun getDogBreeds(): List<DogBreed> {
        return dogApiService.getAllDogs()
    }

    ////////////////////////////
    //  Firebase database
    ////////////////////////////

    /**
     * Adds data to the Firebase realtime database at /path/uniqueIdentifier if it does not exist.
     *
     * @param path The relative path from this reference to the new one that should be created.
     * @param uniqueIdentifier The uid for the data that will be written at the given path.
     * @param data The data that will be written to the server.
     * @param isSuccess A Lambda function that is passed the success of the operation.
     */
    fun <T> addNewDataToRemoteDatabase(
        path: String,
        uniqueIdentifier: String,
        data: T,
        isSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch {
        if (userSignedIn() && !dataExistsOnRemoteDatabase(path, uniqueIdentifier)) {
            writeToRemoteDatabase(path, uniqueIdentifier, data, isSuccess)
        } else {
            isSuccess(false)
        }
    }

    suspend fun <T> readDataFromDatabase(
        path: String,
        uid: String,
        clazz: Class<T>
    ): T? {
        val result = remoteDateBase
            .child(path)
            .child(uid)
            .singleValueEvent()

        return when (result) {
            is SingleValueEventResponse.Cancelled -> {
                val errorMsg = result.error.message
                Timber.e(errorMsg)
                null
            }
            is SingleValueEventResponse.DataChanged -> {
                val snapshot = result.snapshot
                val userData = snapshot.getValue(clazz)
                userData
            }
        }
    }

    suspend fun isNewUser(uid: String): Boolean {
        return !dataExistsOnRemoteDatabase(DATABASE_PATH_USERS, uid)
    }

    /**
     * Checks to see if the specific data exists on the Firebase realtime database.
     *
     * @param path The relative path from this reference.
     * @param uniqueIdentifier The uid for the data path.
     *
     * @return True if data exists, false otherwise.
     */
    private suspend fun dataExistsOnRemoteDatabase(path: String, uniqueIdentifier: String): Boolean {
        val result = remoteDateBase
            .child(path)
            .child(uniqueIdentifier)
            .singleValueEvent()

        return when (result) {
            is SingleValueEventResponse.Cancelled -> {
                val message = result.error.message
                Timber.tag(TAG).e(message)
                false
            }
            is SingleValueEventResponse.DataChanged -> {
                val snapshot = result.snapshot
                snapshot.exists()
            }
        }
    }

    private fun validateDoubleField(
        field: Double,
        lowerLimit: Double,
        upperLimit: Double,
        result: (Result<Double>) -> Unit
    ) {
        if (field in lowerLimit..upperLimit) {
            result(Result.success(field))
        } else {
            result(Result.failure(ValidationError.OutOfRangeError(lowerLimit, upperLimit)))
        }
    }

    /**
     * Validates a string field to meet specific needs.
     *
     * @param field The field to be validated.
     * @param maxLength The maximum length for this field.
     * @param regex a regular expression that the field must match.
     * @param result A lambda that passes the result of the validation.
     */
    private fun validateStringField(
        field: String,
        maxLength: Int,
        regex: Regex? = null,
        result: (Result<String>) -> Unit
    ) {
        when {
            field.length > maxLength -> {
                Timber.tag(TAG).d("null length reached")
                result(
                    kotlin.Result.failure(
                        com.soundbite.packt.util.ValidationError.MaxLengthReachedError(
                            maxLength
                        )
                    )
                )
            }
            regex != null -> {
                Timber.tag(TAG).d("forbid chars not null")
                if (!field.contains(regex)) {
                    val lastChar = field.last()
                    result(Result.failure(ValidationError.IllegalCharacterError(lastChar)))
                } else {
                    Timber.tag(TAG).d("We found no issues")
                    result(Result.success(field.trim()))
                }
            }
            else -> {
                Timber.tag(TAG).d("We found no issues")
                result(Result.success(field.trim()))
            }
        }
    }

    /**
     * Gets the latest year a user can use to create an account.
     */
    private fun getMaxYear() = calendar.get(Calendar.YEAR) - 18

    /**
     * Gets the latest month a user can use to create an account.
     */
    private fun getMaxMonth() = calendar.get(Calendar.MONTH) + 1

    /**
     * Gets the latest day a user can use to create an account.
     */
    private fun getMaxDay() = calendar.get(Calendar.DAY_OF_MONTH)

    /**
     * Checks if a user is currently signed in.
     *
     * @return True if a user is signed in, false otherwise.
     */
    private fun userSignedIn() = FirebaseAuth.getInstance().currentUser != null

    /**
     * Writes data to the Firebase realtime database at the specified path with with the given uid.
     * This operation may overwrite data if it already exist.
     *
     * @param path The relative path from this reference to the new one that should be created.
     * @param uniqueIdentifier The uid for the data that will be written at the given path.
     * @param data The data that will be written to the server.
     * @param isSuccess A Lambda function that is passed the success of the operation.
     */
    private fun <T> writeToRemoteDatabase(
        path: String,
        uniqueIdentifier: String,
        data: T,
        isSuccess: (Boolean) -> Unit
    ) {
        remoteDateBase
            .child(path)
            .child(uniqueIdentifier)
            .setValue(data)
            .addOnSuccessListener {
                isSuccess(true)
            }
            .addOnFailureListener {
                isSuccess(false)
            }
    }
}

class UserViewModelFactory(private val ownerDogDao: OwnerDogDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(ownerDogDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
