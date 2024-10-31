package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BuildingService {

    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _buildingModeOn = MutableStateFlow<Boolean>(false)
    val buildingModeOn: StateFlow<Boolean> get() = _buildingModeOn

    fun switchBuildingMode(){
        if(buildingModeOn.value)
            turnOffBuildMode()
        else
            turnOnBuildingMode()
    }
    fun turnOnBuildingMode(){
        _buildingModeOn.value = true
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.setClosedToBuild() }
        highlightedProperties = getPropertiesWherePlayerCanBuild(Game.getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.setOpenToBuild() }
    }
    fun turnOffBuildMode(){
        if(_buildingModeOn.value){
            _buildingModeOn.value = false
            highlightedProperties.forEach { p-> p.setClosedToBuild() }
            highlightedProperties = emptyList()
        }
    }
    fun getPropertiesWherePlayerCanBuild(player: PlayerViewModel): List<EstateViewModel>{
        var propertiesWherePlayerCanBuild = Game.estates.value.filter { e->
            e.isOwnedByPlayer(player) && e.isProperty && !e.isFullyBuilt()
        }
        if(Game.ruleBook.isSetNecessaryToBuild){
            propertiesWherePlayerCanBuild = propertiesWherePlayerCanBuild.filter{ p->
                Game.getPropertiesBySetColor((p.estate as Property).setColor).all { e -> e.isOwnedByPlayer(player) }
            }
        }
        if(Game.ruleBook.isVisitNecessaryToBuild){
            val currentProperty = propertiesWherePlayerCanBuild.find { p -> p.estate.index == player.position.value }
            if(currentProperty == null){
                return emptyList()
            }
            else{
                propertiesWherePlayerCanBuild =
                    if(!Game.ruleBook.isSetNecessaryToBuild){
                        propertiesWherePlayerCanBuild.filter { p-> p==currentProperty }
                    } else{
                        propertiesWherePlayerCanBuild.filter { p->
                            (p.estate as Property).setColor == (currentProperty.estate as Property).setColor
                        }
                    }
            }
            if(!Game.ruleBook.buildingOnMultiplePropertiesInOneMoveEnabled){
                propertiesWherePlayerCanBuild = propertiesWherePlayerCanBuild.filter{p->p==currentProperty}
            }
        }
        return propertiesWherePlayerCanBuild
    }
    fun build(estate: EstateViewModel){
        if(!estate.isProperty)
            return
        val property = estate.estate as Property
        if(estate.numberOfBuildings.value==property.rent.size-1)
            return
        val player = Game.getActivePlayer()!!
        if(TransactionService.buyBuilding(player, property.housePrice)){
            estate.addBuilding()
            highlightProperties()
        }
    }
    fun sell(estate: EstateViewModel){
        if(!estate.isProperty)
            return
        val property = estate.estate as Property
        if(estate.numberOfBuildings.value==0)
            return
        val player = Game.getActivePlayer()!!
        TransactionService.receive(player, property.housePrice/2)
        estate.removeBuilding()
    }
}