package com.example.investorssquare.game.data.local

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.IOException

class JsonParser(private val context: Context) {

    fun loadBoard(fileName: String): Board {
        val jsonString = loadJsonFromFile(fileName)
        val jsonObject = Json.parseToJsonElement(jsonString).jsonObject

        val boardName = jsonObject["name"]?.jsonPrimitive?.content ?: throw Exception("Board name is null")
        val imageUrl = jsonObject["image-url"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")

        val fields = jsonObject["fields"]?.jsonArray ?: throw Exception("Fields array is null")
        val fieldList = fields.mapIndexed { index, element -> parseField(element.jsonObject, index) }

        val playerColors = parsePlayerColors(jsonObject)

        return Board(boardName, imageUrl, fieldList, playerColors)
    }

    private fun loadJsonFromFile(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw RuntimeException("Error reading JSON file. Make sure the file exists in the assets directory.", e)
        }
    }

    private fun parseField(fieldObject: JsonObject, index: Int): Field {
        val name = fieldObject["name"]?.jsonPrimitive?.content ?: throw Exception("Field name is null")
        val type = fieldObject["type"]?.jsonPrimitive?.content ?: throw Exception("Field type is null")

        return when (FieldType.valueOf(type)) {
            FieldType.PROPERTY -> {
                Property(
                    name = name,
                    index = index,
                    rent = parseRent(fieldObject),
                    price = fieldObject["price"]?.jsonPrimitive?.int ?: throw Exception("Price is null"),
                    mortgagePrice = fieldObject["mortgage-price"]?.jsonPrimitive?.int ?: throw Exception("Mortgage price is null"),
                    sellPrice = fieldObject["sell-price"]?.jsonPrimitive?.int ?: throw Exception("Sell price is null"),
                    setColor = Color(android.graphics.Color.parseColor(fieldObject["set"]?.jsonPrimitive?.content ?: throw Exception("Set color is null"))),
                    housePrice = fieldObject["house-price"]?.jsonPrimitive?.int ?: throw Exception("House price is null")
                )
            }
            FieldType.STATION -> {
                Station(
                    name = name,
                    index = index,
                    rent = parseRent(fieldObject),
                    price = fieldObject["price"]?.jsonPrimitive?.int ?: throw Exception("Price is null"),
                    mortgagePrice = fieldObject["mortgage-price"]?.jsonPrimitive?.int ?: throw Exception("Mortgage price is null"),
                    sellPrice = fieldObject["sell-price"]?.jsonPrimitive?.int ?: throw Exception("Sell price is null")
                )
            }
            FieldType.UTILITY -> {
                Utility(
                    name = name,
                    index = index,
                    rent = parseRent(fieldObject),
                    price = fieldObject["price"]?.jsonPrimitive?.int ?: throw Exception("Price is null"),
                    mortgagePrice = fieldObject["mortgage-price"]?.jsonPrimitive?.int ?: throw Exception("Mortgage price is null"),
                    sellPrice = fieldObject["sell-price"]?.jsonPrimitive?.int ?: throw Exception("Sell price is null")
                )
            }
            FieldType.TAX -> {
                Tax(
                    name = name,
                    index = index,
                    tax = fieldObject["tax"]?.jsonPrimitive?.int ?: throw Exception("Tax is null"),
                    taxPercentage = fieldObject["tax-percentage"]?.jsonPrimitive?.int ?: throw Exception("Tax percentage is null")
                )
            }
            FieldType.CHANCE -> Chance(name, index)
            FieldType.COMMUNITY_CHEST -> CommunityChest(name, index)
            else -> CornerField(name, index, FieldType.valueOf(type))
        }
    }

    private fun parseRent(fieldObject: JsonObject): List<Int> {
        return fieldObject["rent"]?.jsonArray?.map { it.jsonPrimitive.int }
            ?: throw Exception("Rent is null")
    }

    private fun parsePlayerColors(jsonObject: JsonObject): List<Color> {
        val colorsArray = jsonObject["player-colors"]?.jsonArray ?: throw Exception("Player colors array is null")
        return colorsArray.map { Color(android.graphics.Color.parseColor(it.jsonPrimitive.content)) }
    }
}
