package com.example.investorssquare.game.service.highlighting_services

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.service.BoardService.turnOffHighlightMode
import com.example.investorssquare.game.service.BoardService.turnOnHighlightMode
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.EstateService.getAllPropertiesBySet
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService.payIfAffordable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BuildingService {
    var buildingsInCurrentMove : MutableMap<EstateViewModel, Int> = mutableMapOf()
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _buildingModeOn = MutableStateFlow(false)
    val buildingModeOn: StateFlow<Boolean> get() = _buildingModeOn

    fun resetBuildingsInCurrentMove() {
        buildingsInCurrentMove.clear()
    }

    fun switchBuildingMode() {
        if(buildingModeOn.value)
            turnOffBuildingMode()
        else
            turnOnBuildingMode()
    }

    fun turnOffBuildingMode() {
        if(_buildingModeOn.value) {
            _buildingModeOn.value = false
            turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }

    fun build(estate: EstateViewModel) {
        if(!estate.isProperty)
            return
        val property = estate.estate as Property
        if(estate.numberOfBuildings.value==property.rent.size-1)
            return
        val player = getActivePlayer()!!
        if(payIfAffordable(player, property.housePrice)){
            estate.addBuilding()
            if(buildingsInCurrentMove.containsKey(estate)){
                buildingsInCurrentMove[estate] = buildingsInCurrentMove[estate]?.plus(1)!!
            }
            else{
                buildingsInCurrentMove[estate] = 1
            }
            highlightProperties()
        }
    }

    private fun turnOnBuildingMode() {
        turnOnHighlightMode()
        _buildingModeOn.value = true
        highlightProperties()
    }

    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWherePlayerCanBuild(getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }

    private fun getPropertiesWherePlayerCanBuild(player: PlayerViewModel): List<EstateViewModel> {
        var properties = estates.value.filter { it.isOwnedByPlayer(player) && it.isProperty && !it.isFullyBuilt() }

        if (RuleBook.isSetNecessaryToBuild) {
            properties = filterByCompleteSets(player, properties)
            if (RuleBook.evenlyBuilding) {
                properties = filterByEvenBuilding(properties)
            }
        }

        if (RuleBook.isVisitNecessaryToBuild) {
            properties = filterByVisitedProperty(player, properties)
        }

        if (RuleBook.buildingsPerMovePerProperty > 0) {
            properties = filterByBuildingsPerMove(properties)
        }

        return properties.filter {
            !it.isMortgaged.value && (it.estate as Property).housePrice <= player.money.value
        }
    }

    private fun filterByCompleteSets(player: PlayerViewModel, properties: List<EstateViewModel>): List<EstateViewModel> {
        return properties.filter { property ->
            getAllPropertiesBySet((property.estate as Property).setColor).all { it.isOwnedByPlayer(player) }
        }
    }

    private fun filterByEvenBuilding(properties: List<EstateViewModel>): List<EstateViewModel> {
        val result = mutableListOf<EstateViewModel>()
        val propertiesByColor = properties.groupBy { (it.estate as Property).setColor }

        for ((_, propertiesInColor) in propertiesByColor) {
            val sortedProperties = propertiesInColor.sortedBy { it.numberOfBuildings.value }
            val minBuildings = sortedProperties.first().numberOfBuildings.value
            result.addAll(sortedProperties.filter { it.numberOfBuildings.value == minBuildings })
        }

        return result
    }

    private fun filterByVisitedProperty(player: PlayerViewModel, properties: List<EstateViewModel>): List<EstateViewModel> {
        val currentProperty = properties.find { it.estate.index == player.position.value } ?: return emptyList()

        return if (!RuleBook.isSetNecessaryToBuild) {
            properties.filter { it == currentProperty }
        } else {
            properties.filter {
                (it.estate as Property).setColor == (currentProperty.estate as Property).setColor
            }
        }.let {
            if (!RuleBook.buildingOnMultiplePropertiesInOneMoveEnabled) {
                it.filter { property -> property == currentProperty }
            } else {
                it
            }
        }
    }

    private fun filterByBuildingsPerMove(properties: List<EstateViewModel>): List<EstateViewModel> {
        return properties.filter {
            buildingsInCurrentMove[it] == null || buildingsInCurrentMove[it]!! < RuleBook.buildingsPerMovePerProperty
        }
    }
}