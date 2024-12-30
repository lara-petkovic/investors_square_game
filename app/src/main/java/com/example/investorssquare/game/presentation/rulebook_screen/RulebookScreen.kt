package com.example.investorssquare.game.presentation.rulebook_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            mapOf<String, Any>(
                "salary" to RuleBook.salary,
                "startingCapital" to RuleBook.startingCapital,
                "timePerMoveInSeconds" to RuleBook.timePerMoveInSeconds,
                "numberOfPlayers" to RuleBook.numberOfPlayers,
                "rollADoubleToEscapeJailEnabled" to RuleBook.rollADoubleToEscapeJailEnabled,
                "collectRentsWhileInJail" to RuleBook.collectRentsWhileInJail,
                "payToEscapeJailEnabled" to RuleBook.payToEscapeJailEnabled,
                "gatheringTaxesEnabled" to RuleBook.gatheringTaxesEnabled,
                "collectTaxesOnFreeParkingEnabled" to RuleBook.collectTaxesOnFreeParkingEnabled,
                "payingTaxesViaPercentagesEnabled" to RuleBook.payingTaxesViaPercentagesEnabled,
                "auctionsEnabled" to RuleBook.auctionsEnabled,
                "mortgagesEnabled" to RuleBook.mortgagesEnabled,
                "sellingEstateEnabled" to RuleBook.sellingEstateEnabled,
                "doubleRentOnCollectedSetsEnabled" to RuleBook.doubleRentOnCollectedSetsEnabled,
                "playAgainIfRolledDouble" to RuleBook.playAgainIfRolledDouble,
                "speedDieEnabled" to RuleBook.speedDieEnabled,
                "doublesRolledLimit" to RuleBook.doublesRolledLimit,
                "playAgainOnFreeParkingEnabled" to RuleBook.playAgainOnFreeParkingEnabled,
                "isSetNecessaryToBuild" to RuleBook.isSetNecessaryToBuild,
                "isVisitNecessaryToBuild" to RuleBook.isVisitNecessaryToBuild,
                "evenlyBuilding" to RuleBook.evenlyBuilding,
                "buildingOnMultiplePropertiesInOneMoveEnabled" to RuleBook.buildingOnMultiplePropertiesInOneMoveEnabled,
                "buildingsPerMovePerProperty" to RuleBook.buildingsPerMovePerProperty
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Game Rules", style = androidx.compose.ui.text.TextStyle(fontSize = 24.sp))

        Spacer(modifier = Modifier.height(16.dp))

        // Iterate over rulesState to create UI elements
        rulesState.forEach { (ruleName, ruleValue) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                when (ruleValue) {
                    is Boolean -> {
                        Checkbox(
                            checked = ruleValue,
                            onCheckedChange = { checked ->
                                rulesState = rulesState.toMutableMap().apply {
                                    this[ruleName] = checked
                                }
                            }
                        )
                        Text(
                            ruleName.replace("([A-Z])".toRegex(), " $1")
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                        )
                    }

                    is Int -> {
                        Text(
                            ruleName.replace("([A-Z])".toRegex(), " $1")
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = ruleValue.toString(),
                            onValueChange = { newValue ->
                                val newIntValue = newValue.toIntOrNull() ?: ruleValue
                                rulesState = rulesState.toMutableMap().apply {
                                    this[ruleName] = newIntValue
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            try {
                RuleBook.salary = rulesState["salary"] as Int
                RuleBook.startingCapital = rulesState["startingCapital"] as Int
                RuleBook.timePerMoveInSeconds = rulesState["timePerMoveInSeconds"] as Int
                RuleBook.numberOfPlayers = rulesState["numberOfPlayers"] as Int
                RuleBook.rollADoubleToEscapeJailEnabled = rulesState["rollADoubleToEscapeJailEnabled"] as Boolean
                RuleBook.collectRentsWhileInJail = rulesState["collectRentsWhileInJail"] as Boolean
                RuleBook.payToEscapeJailEnabled = rulesState["payToEscapeJailEnabled"] as Boolean
                RuleBook.gatheringTaxesEnabled = rulesState["gatheringTaxesEnabled"] as Boolean
                RuleBook.collectTaxesOnFreeParkingEnabled = rulesState["collectTaxesOnFreeParkingEnabled"] as Boolean
                RuleBook.payingTaxesViaPercentagesEnabled = rulesState["payingTaxesViaPercentagesEnabled"] as Boolean
                RuleBook.auctionsEnabled = rulesState["auctionsEnabled"] as Boolean
                RuleBook.mortgagesEnabled = rulesState["mortgagesEnabled"] as Boolean
                RuleBook.sellingEstateEnabled = rulesState["sellingEstateEnabled"] as Boolean
                RuleBook.doubleRentOnCollectedSetsEnabled = rulesState["doubleRentOnCollectedSetsEnabled"] as Boolean
                RuleBook.playAgainIfRolledDouble = rulesState["playAgainIfRolledDouble"] as Boolean
                RuleBook.speedDieEnabled = rulesState["speedDieEnabled"] as Boolean
                RuleBook.doublesRolledLimit = rulesState["doublesRolledLimit"] as Int
                RuleBook.playAgainOnFreeParkingEnabled = rulesState["playAgainOnFreeParkingEnabled"] as Boolean
                RuleBook.isSetNecessaryToBuild = rulesState["isSetNecessaryToBuild"] as Boolean
                RuleBook.isVisitNecessaryToBuild = rulesState["isVisitNecessaryToBuild"] as Boolean
                RuleBook.evenlyBuilding = rulesState["evenlyBuilding"] as Boolean
                RuleBook.buildingOnMultiplePropertiesInOneMoveEnabled = rulesState["buildingOnMultiplePropertiesInOneMoveEnabled"] as Boolean
                RuleBook.buildingsPerMovePerProperty = rulesState["buildingsPerMovePerProperty"] as Int

                Toast.makeText(navController.context, "Rules updated!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.RulebookScreen.route) { inclusive = true }
                }

            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error updating rules!", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Save Rules")
        }
    }
}
