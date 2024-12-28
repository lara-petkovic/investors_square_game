package com.example.investorssquare.game.service.highlighting_services

import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel

object HighlightFieldsService {
    fun handleHighlightedFieldClicked(estate: EstateViewModel?): Boolean {
        if(BuildingService.buildingModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                BuildingService.build(estate)
            }
            return true
        }
        else if(SellingBuildingsService.sellingModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                SellingBuildingsService.sell(estate)
            }
            return true
        }
        else if(MortgageService.mortgageModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                MortgageService.mortgage(estate)
            }
            return true
        }
        else if(RedeemService.redeemModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                RedeemService.redeem(estate)
            }
            return true
        }
        else if(SellingPropertiesService.sellingModeOn.value){
            if(estate !=null && estate.isHighlighted.value){
                SellingPropertiesService.sell(estate)
            }
            return true
        }
        return false
    }
    fun turnOff(){
        RedeemService.turnOffRedeemMode()
        MortgageService.turnOffMortgageMode()
        SellingBuildingsService.turnOffSellingMode()
        SellingPropertiesService.turnOffSellingMode()
        BuildingService.turnOffBuildingMode()
        BuildingService.resetBuildingsInCurrentMove()
    }
    fun switchToBuildingMode(){
        RedeemService.turnOffRedeemMode()
        MortgageService.turnOffMortgageMode()
        SellingBuildingsService.turnOffSellingMode()
        SellingPropertiesService.turnOffSellingMode()
        BuildingService.switchBuildingMode()
    }
    fun switchToSellingPropertyMode(){
        RedeemService.turnOffRedeemMode()
        MortgageService.turnOffMortgageMode()
        SellingBuildingsService.turnOffSellingMode()
        BuildingService.turnOffBuildingMode()
        SellingPropertiesService.switchSellingMode()
    }
    fun switchToSellingBuildingMode(){
        RedeemService.turnOffRedeemMode()
        MortgageService.turnOffMortgageMode()
        SellingPropertiesService.turnOffSellingMode()
        BuildingService.turnOffBuildingMode()
        SellingBuildingsService.switchSellingMode()
    }
    fun switchToRedeemMode(){
        MortgageService.turnOffMortgageMode()
        SellingBuildingsService.turnOffSellingMode()
        SellingPropertiesService.turnOffSellingMode()
        BuildingService.turnOffBuildingMode()
        RedeemService.switchRedeemMode()
    }
    fun switchToMortgageMode(){
        RedeemService.turnOffRedeemMode()
        SellingBuildingsService.turnOffSellingMode()
        SellingPropertiesService.turnOffSellingMode()
        BuildingService.turnOffBuildingMode()
        MortgageService.switchMortgageMode()
    }
}