package com.example.investorssquare.game.service.highlighting_services

import androidx.compose.ui.graphics.Color
import com.example.investorssquare.game.domain.model.Property
import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.presentation.board_screen.viewModels.PlayerViewModel
import com.example.investorssquare.game.service.BoardService.turnOffHighlightMode
import com.example.investorssquare.game.service.BoardService.turnOnHighlightMode
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService
import com.example.investorssquare.game.service.TransactionService.receive
import com.example.investorssquare.game.service.highlighting_services.BuildingService.buildingsInCurrentMove
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SellingBuildingsService {
    private var highlightedProperties: List<EstateViewModel> = emptyList()

    private val _sellingModeOn = MutableStateFlow<Boolean>(false)
    val sellingBuildingsModeOn: StateFlow<Boolean> get() = _sellingModeOn

    fun switchSellingBuildingsMode(){
        if(sellingBuildingsModeOn.value)
            turnOffSellingBuildingsMode()
        else
            turnOnSellingBuildingsMode()
    }
    fun turnOnSellingBuildingsMode(){
        _sellingModeOn.value = true
        turnOnHighlightMode()
        highlightProperties()
    }
    private fun highlightProperties() {
        highlightedProperties.forEach { p -> p.unhighlight() }
        highlightedProperties = getPropertiesWherePlayerCanSell(getActivePlayer()!!)
        highlightedProperties.forEach { p -> p.highlight() }
    }
    fun turnOffSellingBuildingsMode(){
        if(_sellingModeOn.value){
            _sellingModeOn.value = false
            turnOffHighlightMode()
            highlightedProperties.forEach { p-> p.unhighlight() }
            highlightedProperties = emptyList()
        }
    }
    private fun getPropertiesWherePlayerCanSell(player: PlayerViewModel): List<EstateViewModel>{
        var propertiesWherePlayerCanSell = estates.value.filter{ p->
            p.isProperty && p.isOwnedByPlayer(player) && p.numberOfBuildings.value>0
        }
        if(RuleBook.evenlyBuilding){
            val res: MutableList<EstateViewModel> = mutableListOf()
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
    fun sellBuilding(estate: EstateViewModel){
        if(!estate.isProperty)
            return
        val property = estate.estate as Property
        if(estate.numberOfBuildings.value==0)
            return
        val player = getActivePlayer()!!
        receive(player, property.housePrice / 2)
        estate.removeBuilding()
        highlightProperties()
        if(buildingsInCurrentMove[estate]!=null){
           buildingsInCurrentMove[estate] = buildingsInCurrentMove[estate]!! - 1;
        }
    }
}