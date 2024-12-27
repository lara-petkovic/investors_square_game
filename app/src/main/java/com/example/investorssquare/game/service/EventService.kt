package com.example.investorssquare.game.service

import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.presentation.board_screen.viewModels.Game
import com.example.investorssquare.game.service.CommunityCardService.executeCardAction
import com.example.investorssquare.game.service.CommunityCardService.openCardPopup
import com.example.investorssquare.game.service.DiceService.disableDice
import com.example.investorssquare.game.service.DiceService.enableDice
import com.example.investorssquare.game.service.DiceService.handleDiceThrown
import com.example.investorssquare.game.service.EstateService.handleCardInformationClick
import com.example.investorssquare.game.service.EstateService.handleEstateBought
import com.example.investorssquare.game.service.EstateService.showPopupForEstate
import com.example.investorssquare.game.service.MoveService.handleDiceToTheNextPlayer
import com.example.investorssquare.game.service.PlayerMovementService.goToJail
import com.example.investorssquare.game.service.PlayerMovementService.moveActivePlayer
import com.example.investorssquare.game.service.TransactionService.collectGatheredTaxes
import com.example.investorssquare.game.service.TransactionService.collectSalary
import com.example.investorssquare.game.service.TransactionService.payPriceForEstate
import com.example.investorssquare.game.service.TransactionService.payRent
import com.example.investorssquare.game.service.TransactionService.payTax
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object EventService {
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    init {
        observeEvents()
    }
    private fun observeEvents() {
        serviceScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is Event.ON_COMMUNITY_CARD_CLOSED -> executeCardAction()
                    is Event.ON_COMMUNITY_CARD_OPENED -> openCardPopup()
                    is Event.ON_DICE_THROWN -> handleDiceThrown(event.firstNumber, event.secondNumber)
                    is Event.ON_MOVE_PLAYER -> moveActivePlayer()
                    is Event.ON_SWITCH_TO_BUILDING_MODE -> {
                        SellingService.turnOffSellingMode()
                        MortgageService.turnOffMortgageMode()
                        RedeemService.turnOffRedeemMode()
                        BuildingService.switchBuildingMode()
                    }
                    is Event.ON_SWITCH_TO_SELLING_MODE -> {
                        BuildingService.turnOffBuildMode()
                        MortgageService.turnOffMortgageMode()
                        RedeemService.turnOffRedeemMode()
                        SellingService.switchSellingMode()
                    }
                    is Event.ON_SWITCH_TO_MORTGAGE_MODE -> {
                        BuildingService.turnOffBuildMode()
                        RedeemService.turnOffRedeemMode()
                        SellingService.turnOffSellingMode()
                        MortgageService.switchMortgageMode()
                    }
                    is Event.ON_SWITCH_TO_REDEEM_MODE -> {
                        BuildingService.turnOffBuildMode()
                        MortgageService.turnOffMortgageMode()
                        SellingService.turnOffSellingMode()
                        RedeemService.switchRedeemMode()
                    }
                    is Event.ON_GO_TO_JAIL -> {
                        disableDice()
                        goToJail()
                    }
                    is Event.ON_PLAYER_LANDED_ON_FREE_ESTATE -> showPopupForEstate()
                    is Event.ON_FIELD_CLICKED -> {
                        val estate = Game.getEstateByFieldIndex(event.fieldIndex)
                        if(BuildingService.buildingModeOn.value){
                            if(estate !=null && estate.isHighlighted.value)
                                BuildingService.build(estate)
                        }
                        else if(SellingService.sellingModeOn.value){
                            if(estate !=null && estate.isHighlighted.value)
                                SellingService.sell(estate)
                        }
                        else if(MortgageService.mortgageModeOn.value){
                            if(estate !=null && estate.isHighlighted.value)
                                MortgageService.mortgage(estate)
                        }
                        else if(RedeemService.redeemModeOn.value){
                            if(estate !=null && estate.isHighlighted.value)
                                RedeemService.redeem(estate)
                        }
                        else
                            handleCardInformationClick(event.fieldIndex)
                    }
                    is Event.ON_ESTATE_BOUGHT -> {
                        if(payPriceForEstate(event.fieldIndex))
                            handleEstateBought(event.fieldIndex)
                    }
                    is Event.ON_MOVE_FINISHED -> {
                        BuildingService.turnOffBuildMode()
                        SellingService.turnOffSellingMode()
                        handleDiceToTheNextPlayer()
                    }
                    is Event.ON_PLAYER_LANDED_ON_BOUGHT_ESTATE -> payRent()
                    is Event.ON_PLAYER_CROSSED_START -> collectSalary()
                    is Event.ON_PLAYER_LANDED_ON_TAX -> payTax()
                    is Event.ON_PLAYER_LANDED_ON_FREE_PARKING -> {
                        if(Game.ruleBook.collectTaxesOnFreeParkingEnabled)
                            collectGatheredTaxes()
                        else if(Game.ruleBook.playAgainOnFreeParkingEnabled)
                            enableDice()
                    }
                    else -> { }
                }
            }
        }
    }
}