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
import com.example.investorssquare.game.service.highlighting_services.BuildingService
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService
import com.example.investorssquare.game.service.highlighting_services.MortgageService
import com.example.investorssquare.game.service.highlighting_services.RedeemService
import com.example.investorssquare.game.service.highlighting_services.SellingBuildingsService
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
                    is Event.ON_SWITCH_TO_BUILDING_MODE -> HighlightFieldsService.switchToBuildingMode()
                    is Event.ON_SWITCH_TO_SELLING_BUILDING_MODE -> HighlightFieldsService.switchToSellingBuildingMode()
                    is Event.ON_SWITCH_TO_MORTGAGE_MODE -> HighlightFieldsService.switchToMortgageMode()
                    is Event.ON_SWITCH_TO_REDEEM_MODE -> HighlightFieldsService.switchToRedeemMode()
                    is Event.ON_SWITCH_TO_SELLING_PROPERTY_MODE -> HighlightFieldsService.switchToSellingPropertyMode()
                    is Event.ON_GO_TO_JAIL -> {
                        disableDice()
                        goToJail()
                    }
                    is Event.ON_PLAYER_LANDED_ON_FREE_ESTATE -> showPopupForEstate()
                    is Event.ON_FIELD_CLICKED -> {
                        val estate = Game.getEstateByFieldIndex(event.fieldIndex)
                        if(!HighlightFieldsService.handleHighlightedFieldClicked(estate))
                            handleCardInformationClick(event.fieldIndex)
                    }
                    is Event.ON_ESTATE_BOUGHT -> {
                        if(payPriceForEstate(event.fieldIndex))
                            handleEstateBought(event.fieldIndex)
                    }
                    is Event.ON_MOVE_FINISHED -> {
                        HighlightFieldsService.turnOff()
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
                    is Event.ON_BAIL_OUT -> JailEscapeService.bailOut()
                    is Event.ON_ROLL_A_DOUBLE_TO_ESCAPE_JAIL -> JailEscapeService.rollADouble()
                    is Event.ON_USE_GET_OUT_OF_JAIL_FREE_CARD -> JailEscapeService.useGetOutOfJailFreeCard()
                    else -> { }
                }
            }
        }
    }
}