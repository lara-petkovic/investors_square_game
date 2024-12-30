package com.example.investorssquare.game.service.highlighting_services

import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.service.highlighting_services.BuildingService.build
import com.example.investorssquare.game.service.highlighting_services.BuildingService.buildingModeOn
import com.example.investorssquare.game.service.highlighting_services.BuildingService.resetBuildingsInCurrentMove
import com.example.investorssquare.game.service.highlighting_services.BuildingService.switchBuildingMode
import com.example.investorssquare.game.service.highlighting_services.BuildingService.turnOffBuildingMode
import com.example.investorssquare.game.service.highlighting_services.MortgageService.mortgage
import com.example.investorssquare.game.service.highlighting_services.MortgageService.mortgageModeOn
import com.example.investorssquare.game.service.highlighting_services.MortgageService.switchMortgageMode
import com.example.investorssquare.game.service.highlighting_services.MortgageService.turnOffMortgageMode
import com.example.investorssquare.game.service.highlighting_services.RedeemService.redeem
import com.example.investorssquare.game.service.highlighting_services.RedeemService.redeemModeOn
import com.example.investorssquare.game.service.highlighting_services.RedeemService.switchRedeemMode
import com.example.investorssquare.game.service.highlighting_services.RedeemService.turnOffRedeemMode
import com.example.investorssquare.game.service.highlighting_services.SellingBuildingsService.sellBuilding
import com.example.investorssquare.game.service.highlighting_services.SellingBuildingsService.sellingBuildingsModeOn
import com.example.investorssquare.game.service.highlighting_services.SellingBuildingsService.switchSellingBuildingsMode
import com.example.investorssquare.game.service.highlighting_services.SellingBuildingsService.turnOffSellingBuildingsMode
import com.example.investorssquare.game.service.highlighting_services.SellingPropertiesService.sellProperty
import com.example.investorssquare.game.service.highlighting_services.SellingPropertiesService.sellingPropertiesModeOn
import com.example.investorssquare.game.service.highlighting_services.SellingPropertiesService.switchSellingPropertiesMode
import com.example.investorssquare.game.service.highlighting_services.SellingPropertiesService.turnOffSellingPropertiesMode

object HighlightFieldsService {
    fun handleHighlightedFieldClicked(estate: EstateViewModel?): Boolean {
        if(buildingModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                build(estate)
            }
            return true
        }
        else if(sellingBuildingsModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                sellBuilding(estate)
            }
            return true
        }
        else if(mortgageModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                mortgage(estate)
            }
            return true
        }
        else if(redeemModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                redeem(estate)
            }
            return true
        }
        else if(sellingPropertiesModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                sellProperty(estate)
            }
            return true
        }
        return false
    }
    fun turnOffHighlighting(){
        turnOffRedeemMode()
        turnOffMortgageMode()
        turnOffSellingBuildingsMode()
        turnOffSellingPropertiesMode()
        turnOffBuildingMode()
        resetBuildingsInCurrentMove()
    }
    fun switchToBuildingMode(){
        turnOffRedeemMode()
        turnOffMortgageMode()
        turnOffSellingBuildingsMode()
        turnOffSellingPropertiesMode()
        switchBuildingMode()
    }
    fun switchToSellingPropertyMode(){
        turnOffRedeemMode()
        turnOffMortgageMode()
        turnOffSellingBuildingsMode()
        turnOffBuildingMode()
        switchSellingPropertiesMode()
    }
    fun switchToSellingBuildingMode(){
        turnOffRedeemMode()
        turnOffMortgageMode()
        turnOffSellingPropertiesMode()
        turnOffBuildingMode()
        switchSellingBuildingsMode()
    }
    fun switchToRedeemMode(){
        turnOffMortgageMode()
        turnOffSellingBuildingsMode()
        turnOffSellingPropertiesMode()
        turnOffBuildingMode()
        switchRedeemMode()
    }
    fun switchToMortgageMode(){
        turnOffRedeemMode()
        turnOffSellingBuildingsMode()
        turnOffSellingPropertiesMode()
        turnOffBuildingMode()
        switchMortgageMode()
    }
}