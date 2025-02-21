package com.example.investorssquare.game.presentation.rulebook_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.investorssquare.game.navigation.Screen
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import java.util.Locale

@Composable
fun RulebookScreen(navController: NavController) {
    var rulesState by remember {
        mutableStateOf(
            mapOf(
                "General" to mapOf(
                    "salary" to RuleBook.salary,
                    "startingCapital" to RuleBook.startingCapital,
                    "numberOfPlayers" to RuleBook.numberOfPlayers,
                    "timePerMoveInSeconds" to RuleBook.timePerMoveInSeconds
                ),
                "Jail" to mapOf(
                    "rollADoubleToEscapeJailEnabled" to RuleBook.rollADoubleToEscapeJailEnabled,
                    "collectRentsWhileInJail" to RuleBook.collectRentsWhileInJail,
                    "payToEscapeJailEnabled" to RuleBook.payToEscapeJailEnabled,
                    "jailEscapePrice" to RuleBook.jailEscapePrice,
                    "jailSentenceInMoves" to RuleBook.jailSentenceInMoves
                ),
                "Taxes" to mapOf(
                    "gatheringTaxesEnabled" to RuleBook.gatheringTaxesEnabled,
                    "collectTaxesOnFreeParkingEnabled" to RuleBook.collectTaxesOnFreeParkingEnabled,
                    "payingTaxesViaPercentagesEnabled" to RuleBook.payingTaxesViaPercentagesEnabled
                ),
                "Properties" to mapOf(
                    "auctionsEnabled" to RuleBook.auctionsEnabled,
                    "mortgagesEnabled" to RuleBook.mortgagesEnabled,
                    "sellingEstateEnabled" to RuleBook.sellingEstateEnabled,
                    "doubleRentOnCollectedSetsEnabled" to RuleBook.doubleRentOnCollectedSetsEnabled
                ),
                "Dice" to mapOf(
                    "playAgainIfRolledDouble" to RuleBook.playAgainIfRolledDouble,
                    "speedDieEnabled" to RuleBook.speedDieEnabled,
                    "doublesRolledLimit" to RuleBook.doublesRolledLimit,
                    "playAgainOnFreeParkingEnabled" to RuleBook.playAgainOnFreeParkingEnabled
                ),
                "Buildings" to mapOf(
                    "isVisitNecessaryToBuild" to RuleBook.isVisitNecessaryToBuild,
                    "isSetNecessaryToBuild" to RuleBook.isSetNecessaryToBuild,
                    "evenlyBuilding" to RuleBook.evenlyBuilding,
                    "buildingOnMultiplePropertiesInOneMoveEnabled" to RuleBook.buildingOnMultiplePropertiesInOneMoveEnabled,
                    "buildingsPerMovePerProperty" to RuleBook.buildingsPerMovePerProperty
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.onPrimary)
    ) {
        Text("Game Rules",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            fontSize = 34.sp,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 38.dp))
        Spacer(modifier = Modifier.height(16.dp))

        rulesState.forEach { (category, rules) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 8.dp)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.onTertiary,
                        shape = RoundedCornerShape(13.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Section Header
                    Text(
                        category,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )

                    // Section content
                    rules.forEach { (ruleName, ruleValue) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                ruleName.replace("([A-Z])".toRegex(), " $1")
                                    .replaceFirstChar { it.titlecase(Locale.ROOT) },
                                modifier = Modifier.weight(1f)
                            )
                            when (ruleValue) {
                                is Boolean -> {
                                    Switch(
                                        checked = ruleValue,
                                        onCheckedChange = { checked ->
                                            rulesState = rulesState.toMutableMap().apply {
                                                this[category] = this[category]?.toMutableMap()?.apply { this[ruleName] = checked }
                                                    ?: rules
                                            }
                                        }
                                    )
                                }
                                is Int -> {
                                    TextField(
                                        value = ruleValue.toString(),
                                        onValueChange = { newValue ->
                                            val newIntValue = newValue.toIntOrNull() ?: ruleValue
                                            rulesState = rulesState.toMutableMap().apply {
                                                this[category] = this[category]?.toMutableMap()?.apply { this[ruleName] = newIntValue }
                                                    ?: rules
                                            }
                                        },
                                        label = { Text("Enter value") },
                                        modifier = Modifier.width(100.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    // General
                    RuleBook.salary = rulesState["General"]?.get("salary") as Int
                    RuleBook.startingCapital = rulesState["General"]?.get("startingCapital") as Int
                    RuleBook.numberOfPlayers = rulesState["General"]?.get("numberOfPlayers") as Int
                    RuleBook.timePerMoveInSeconds = rulesState["General"]?.get("timePerMoveInSeconds") as Int

                    // Jail
                    RuleBook.rollADoubleToEscapeJailEnabled = rulesState["Jail"]?.get("rollADoubleToEscapeJailEnabled") as Boolean
                    RuleBook.collectRentsWhileInJail = rulesState["Jail"]?.get("collectRentsWhileInJail") as Boolean
                    RuleBook.payToEscapeJailEnabled = rulesState["Jail"]?.get("payToEscapeJailEnabled") as Boolean
                    RuleBook.jailEscapePrice = rulesState["Jail"]?.get("jailEscapePrice") as Int
                    RuleBook.jailSentenceInMoves = rulesState["Jail"]?.get("jailSentenceInMoves") as Int

                    // Taxes
                    RuleBook.gatheringTaxesEnabled = rulesState["Taxes"]?.get("gatheringTaxesEnabled") as Boolean
                    RuleBook.collectTaxesOnFreeParkingEnabled = rulesState["Taxes"]?.get("collectTaxesOnFreeParkingEnabled") as Boolean
                    RuleBook.payingTaxesViaPercentagesEnabled = rulesState["Taxes"]?.get("payingTaxesViaPercentagesEnabled") as Boolean

                    // Properties
                    RuleBook.auctionsEnabled = rulesState["Properties"]?.get("auctionsEnabled") as Boolean
                    RuleBook.mortgagesEnabled = rulesState["Properties"]?.get("mortgagesEnabled") as Boolean
                    RuleBook.sellingEstateEnabled = rulesState["Properties"]?.get("sellingEstateEnabled") as Boolean
                    RuleBook.doubleRentOnCollectedSetsEnabled = rulesState["Properties"]?.get("doubleRentOnCollectedSetsEnabled") as Boolean

                    // Dice
                    RuleBook.playAgainIfRolledDouble = rulesState["Dice"]?.get("playAgainIfRolledDouble") as Boolean
                    RuleBook.speedDieEnabled = rulesState["Dice"]?.get("speedDieEnabled") as Boolean
                    RuleBook.doublesRolledLimit = rulesState["Dice"]?.get("doublesRolledLimit") as Int
                    RuleBook.playAgainOnFreeParkingEnabled = rulesState["Dice"]?.get("playAgainOnFreeParkingEnabled") as Boolean

                    // Buildings
                    RuleBook.isVisitNecessaryToBuild = rulesState["Buildings"]?.get("isVisitNecessaryToBuild") as Boolean
                    RuleBook.isSetNecessaryToBuild = rulesState["Buildings"]?.get("isSetNecessaryToBuild") as Boolean
                    RuleBook.evenlyBuilding = rulesState["Buildings"]?.get("evenlyBuilding") as Boolean
                    RuleBook.buildingOnMultiplePropertiesInOneMoveEnabled = rulesState["Buildings"]?.get("buildingOnMultiplePropertiesInOneMoveEnabled") as Boolean
                    RuleBook.buildingsPerMovePerProperty = rulesState["Buildings"]?.get("buildingsPerMovePerProperty") as Int

                    Toast.makeText(navController.context, "Rules updated!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.RulebookScreen.route) { inclusive = true }
                    }
                } catch (e: Exception) {
                    Toast.makeText(navController.context, "Error updating rules!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 15.dp)
        ) {
            Text("Save Rules")
        }
    }
}