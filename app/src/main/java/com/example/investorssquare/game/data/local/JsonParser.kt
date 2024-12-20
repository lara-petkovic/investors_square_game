package com.example.investorssquare.game.data.local

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.*
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
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
        val propertyCommonName = jsonObject["property-common-name"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val propertyCommonNamePlural = jsonObject["property-common-name-plural"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val stationCommonName = jsonObject["station-common-name"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val stationCommonNamePlural = jsonObject["station-common-name-plural"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val utilityCommonName = jsonObject["utility-common-name"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val utilityCommonNamePlural = jsonObject["utility-common-name-plural"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val diceColor = Color(android.graphics.Color.parseColor(jsonObject["dice-color"]?.jsonPrimitive?.content ?: throw Exception("Set color is null")))
        val fields = jsonObject["fields"]?.jsonArray ?: throw Exception("Fields array is null")
        val fieldList = fields.mapIndexed { index, element -> parseField(element.jsonObject, index, propertyCommonName, propertyCommonNamePlural, stationCommonName, stationCommonNamePlural, utilityCommonName, utilityCommonNamePlural) }

        val communityChest = jsonObject["community-chest"]?.jsonObject?:throw Exception("Community chest cards are required.")
        val chance = jsonObject["chance"]?.jsonObject?:throw Exception("Chance cards are required.")
        val playerColors = parsePlayerColors(jsonObject)

        val board = Board(
            boardName,
            diceColor,
            fieldList,
            playerColors,
            loadCommunity(chance),
            loadCommunity(communityChest)
        )
        board.shuffleCommunityCards()
        Game.ruleBook = RuleBook()
        return board
    }

    private fun loadJsonFromFile(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw RuntimeException("Error reading JSON file. Make sure the file exists in the assets directory.", e)
        }
    }
    private fun loadCommunity(obj: JsonObject): Community {
        val commonName = obj["common-name"]?.jsonPrimitive?.content ?: throw Exception("Image URL is null")
        val primaryColor = Color(android.graphics.Color.parseColor(obj["primary-color"]?.jsonPrimitive?.content ?: throw Exception("Set color is null")))
        val imageUrl =obj["image-url"]?.jsonPrimitive?.content ?: throw Exception("Set color is null")
        val array = obj["cards"]?.jsonArray?: throw Exception("Fields array is null")
        val cards = array.mapIndexed { index, element -> parseCommunityCard(element.jsonObject, index, true) }.toMutableList()
        return Community(imageUrl, commonName, cards, primaryColor)
    }
    private fun parseCommunityCard(cardObject: JsonObject, index: Int, isChanceCard: Boolean): CommunityCard {
        val text = cardObject["text"]?.jsonPrimitive?.content ?: throw Exception("Text is required for community card")
        val actionCode = cardObject["action"]?.jsonPrimitive?.int ?: throw Exception("Action code is required for community card")
        return CommunityCard(text, actionCode, isChanceCard)
    }

    private fun parseField(fieldObject: JsonObject, index: Int, pcn:String, pcnp:String, scn:String, scnp:String, ucn:String, ucnp:String): Field {
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
                    housePrice = fieldObject["house-price"]?.jsonPrimitive?.int ?: throw Exception("House price is null"),
                    imageUrl = fieldObject["image-url"]?.jsonPrimitive?.content ?: "",
                    commonName = pcn,
                    commonNamePlural = pcnp
                )
            }
            FieldType.STATION -> {
                Station(
                    name = name,
                    index = index,
                    rent = parseRent(fieldObject),
                    price = fieldObject["price"]?.jsonPrimitive?.int ?: throw Exception("Price is null"),
                    mortgagePrice = fieldObject["mortgage-price"]?.jsonPrimitive?.int ?: throw Exception("Mortgage price is null"),
                    sellPrice = fieldObject["sell-price"]?.jsonPrimitive?.int ?: throw Exception("Sell price is null"),
                    imageUrl = fieldObject["image-url"]?.jsonPrimitive?.content ?: "",
                    commonName = scn,
                    commonNamePlural = scnp
                )
            }
            FieldType.UTILITY -> {
                Utility(
                    name = name,
                    index = index,
                    rent = parseRent(fieldObject),
                    price = fieldObject["price"]?.jsonPrimitive?.int ?: throw Exception("Price is null"),
                    mortgagePrice = fieldObject["mortgage-price"]?.jsonPrimitive?.int ?: throw Exception("Mortgage price is null"),
                    sellPrice = fieldObject["sell-price"]?.jsonPrimitive?.int ?: throw Exception("Sell price is null"),
                    imageUrl = fieldObject["image-url"]?.jsonPrimitive?.content ?: "",
                    commonName = ucn,
                    commonNamePlural = ucnp
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
            FieldType.CHANCE -> Field(name, FieldType.CHANCE, index)
            FieldType.COMMUNITY_CHEST -> Field(name, FieldType.COMMUNITY_CHEST, index)
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
