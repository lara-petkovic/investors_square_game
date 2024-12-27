package com.example.investorssquare.game.service

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SellingService {
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _sellingModeOn = MutableStateFlow<Boolean>(false)
    val sellingModeOn: StateFlow<Boolean> get() = _sellingModeOn

    fun switchSellingMode(){
        if(sellingModeOn.value)
            turnOffSellingMode()
        else
            turnOnSellingMode()
    }
    fun turnOnSellingMode(){
        _sellingModeOn.value = true
        Game.turnOnHighlightMode()
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWherePlayerCanSell(Game.getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffSellingMode(){
        if(_sellingModeOn.value){
            _sellingModeOn.value = false
            Game.turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    private fun getPropertiesWherePlayerCanSell(player: PlayerViewModel): List<EstateViewModel>{
        var propertiesWherePlayerCanSell = Game.estates.value.filter{ p->
            p.isProperty && p.isOwnedByPlayer(player) && p.numberOfBuildings.value>0
        }
        if(Game.ruleBook.evenlyBuilding){
            var res: MutableList<EstateViewModel> = mutableListOf()
            var lastColor: Color? = null
            for(e in propertiesWherePlayerCanSell){
                val p = e.estate as Property
                if(lastColor==null || p.setColor!=lastColor){
                    val allPropertiesByColor = propertiesWherePlayerCanSell
                        .filter{pro->(pro.estate as Property).setColor==p.setColor}
                        .sortedByDescending{ est-> est.numberOfBuildings.value }
                    val numberOfBuildings = allPropertiesByColor.first().numberOfBuildings.value;
                    for(eVM in allPropertiesByColor){
                        if(eVM.numberOfBuildings.value==numberOfBuildings){
                            res.add(eVM)
                        }
                    }
                    lastColor = p.setColor
                }
            }
            propertiesWherePlayerCanSell = res
        }
        return propertiesWherePlayerCanSell;
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
        highlightProperties()
        if(BuildingService.buildingsInCurrentMove[estate]!=null){
            BuildingService.buildingsInCurrentMove[estate] = BuildingService.buildingsInCurrentMove[estate]!! - 1;
        }
    }
}