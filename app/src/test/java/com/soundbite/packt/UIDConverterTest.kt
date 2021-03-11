package com.soundbite.packt

import com.soundbite.packt.db.DogUIDConverter
import java.util.regex.Pattern
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class UIDConverterTest {
    private val converter = DogUIDConverter()
    private val uuidPattern = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"
    private val uuidsAsList = listOf(
        "11029f09-257b-40f2-aeb5-7fc2ca2a549a",
        "096727d5-66df-4d7c-9a15-7d7219873341",
        "4e59b4fe-b9ef-4741-94d7-3631ff3d393f",
        "93ba31f2-a3a1-4414-9355-65a0ca8a1463",
        "f62bc93f-8972-4d27-a389-aa8b55fd8d1a",
        "c2854eb1-2cb4-47bd-83b2-9c77272d9a33",
        "fe7fe8ad-3266-42a7-b400-8d79fc329b04",
        "deff291c-8654-49b1-accb-ea6741df2213",
        "3966684d-4b7d-4d25-97ee-55aae2dea7c5",
        "0533e193-68db-4583-b12f-e291143f8279"
    )
    private val uuidsAsString =
        "11029f09-257b-40f2-aeb5-7fc2ca2a549a" + "#" +
            "096727d5-66df-4d7c-9a15-7d7219873341" + "#" +
            "4e59b4fe-b9ef-4741-94d7-3631ff3d393f" + "#" +
            "93ba31f2-a3a1-4414-9355-65a0ca8a1463" + "#" +
            "f62bc93f-8972-4d27-a389-aa8b55fd8d1a" + "#" +
            "c2854eb1-2cb4-47bd-83b2-9c77272d9a33" + "#" +
            "fe7fe8ad-3266-42a7-b400-8d79fc329b04" + "#" +
            "deff291c-8654-49b1-accb-ea6741df2213" + "#" +
            "3966684d-4b7d-4d25-97ee-55aae2dea7c5" + "#" +
            "0533e193-68db-4583-b12f-e291143f8279"

    @get:Rule
    val exception = ExpectedException.none()

    @Test
    fun uidListToString_nonNullListToNonNullString_pass() {
        val actual = converter.uidListToString(uuidsAsList)
        assertEquals(uuidsAsString, actual)
    }

//    @Test
//    fun uidListToString_nullListToNullString_pass() {
//        val actual = converter.uidListToString(null)
//        assertEquals(null, actual)
//    }

    @Test
    fun uidListToString_emptyListToNothing_throwException() {
        exception.expect(IllegalArgumentException::class.java)
        converter.uidListToString(emptyList())
    }

    @Test
    fun stringToUidList_nonNullStringToNonNullList_pass() {
        val actual = converter.stringToUidList(uuidsAsString)
        assertEquals(uuidsAsList, actual)
    }

//    @Test
//    fun stringToUidList_nullStringToNullList_pass() {
//        val actual = converter.stringToUidList(null)
//        assertEquals(null, actual)
//    }

    @Test
    fun stringToUidList_emptyStringToNothing_throwException() {
        exception.expect(IllegalArgumentException::class.java)
        converter.stringToUidList("")
    }

    private fun regexPatternFound(regex: String, matcher: String): Boolean {
        val pattern = Pattern.compile(regex)
        val patternMatcher = pattern.matcher(matcher)

        return patternMatcher.find()
    }
}
