package com.example.investorssquare.game.service

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BuildingService {
    var buildingsInCurrentMove : MutableMap<EstateViewModel, Int> = mutableMapOf()
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _buildingModeOn = MutableStateFlow(false)
    val buildingModeOn: StateFlow<Boolean> get() = _buildingModeOn

    fun resetBuildingsInCurrentMove(){
        buildingsInCurrentMove.clear()
    }
    fun switchBuildingMode(){
        if(buildingModeOn.value)
            turnOffBuildMode()
        else
            turnOnBuildingMode()
    }
    fun turnOnBuildingMode(){
        Game.turnOnHighlightMode()
        _buildingModeOn.value = true
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWherePlayerCanBuild(Game.getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffBuildMode(){
        if(_buildingModeOn.value){
            _buildingModeOn.value = false
            Game.turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    fun build(estate: EstateViewModel){
        if(!estate.isProperty)
            return
        val property = estate.estate as Property
        if(estate.numberOfBuildings.value==property.rent.size-1)
            return
        val player = Game.getActivePlayer()!!
        if(TransactionService.payIfAffordable(player, property.housePrice)){
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
    private fun getPropertiesWherePlayerCanBuild(player: PlayerViewModel): List<EstateViewModel>{
        var propertiesWherePlayerCanBuild = Game.estates.value.filter { e->
            e.isOwnedByPlayer(player) && e.isProperty && !e.isFullyBuilt()
        }
        if(Game.ruleBook.isSetNecessaryToBuild){
            propertiesWherePlayerCanBuild = propertiesWherePlayerCanBuild.filter{ p->
                Game.getPropertiesBySetColor((p.estate as Property).setColor).all { e -> e.isOwnedByPlayer(player) }
            }
            if(Game.ruleBook.evenlyBuilding){
                var res: MutableList<EstateViewModel> = mutableListOf()
                var lastColor: Color? = null
                for(e in propertiesWherePlayerCanBuild){
                    val p = e.estate as Property
                    if(lastColor==null || p.setColor!=lastColor){
                        val allPropertiesByColor = propertiesWherePlayerCanBuild
                            .filter{pro->(pro.estate as Property).setColor==p.setColor}
                            .sortedBy{ est-> est.numberOfBuildings.value }
                        val numberOfBuildings = allPropertiesByColor.first().numberOfBuildings.value;
                        for(eVM in allPropertiesByColor){
                            if(eVM.numberOfBuildings.value==numberOfBuildings){
                                res.add(eVM)
                            }
                        }
                        lastColor = p.setColor
                    }
                }
                propertiesWherePlayerCanBuild = res
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
        if(Game.ruleBook.buildingsPerMovePerProperty>0){
            propertiesWherePlayerCanBuild = propertiesWherePlayerCanBuild.filter{p->
                buildingsInCurrentMove[p]==null || buildingsInCurrentMove[p]!!<Game.ruleBook.buildingsPerMovePerProperty
            }
        }
        return propertiesWherePlayerCanBuild.filter{p->!p.isMortgaged.value}
    }

}