package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.service.BankruptcyService.declareBankruptcy
import com.example.investorssquare.game.service.BankruptcyService.dismissDebtPopup
import com.example.investorssquare.game.service.BankruptcyService.openDebtPopup
import com.example.investorssquare.game.service.CommunityCardService.executeCardAction
import com.example.investorssquare.game.service.CommunityCardService.openCardPopup
import com.example.investorssquare.game.service.DiceService.disableDice
import com.example.investorssquare.game.service.DiceService.enableDice
import com.example.investorssquare.game.service.DiceService.handleDiceThrown
import com.example.investorssquare.game.service.EstateService.getEstateByFieldIndex
import com.example.investorssquare.game.service.EstateService.handleCardInformationClick
import com.example.investorssquare.game.service.EstateService.handleEstateBought
import com.example.investorssquare.game.service.EstateService.showPopupForEstate
import com.example.investorssquare.game.service.JailEscapeService.bailOut
import com.example.investorssquare.game.service.JailEscapeService.rollADoubleToEscape
import com.example.investorssquare.game.service.JailEscapeService.useGetOutOfJailFreeCard
import com.example.investorssquare.game.service.MoveService.handleDiceToTheNextPlayer
import com.example.investorssquare.game.service.PlayerMovementService.goToJail
import com.example.investorssquare.game.service.PlayerMovementService.moveActivePlayer
import com.example.investorssquare.game.service.PlayersService.getActivePlayer
import com.example.investorssquare.game.service.TransactionService.collectGatheredTaxes
import com.example.investorssquare.game.service.TransactionService.collectSalary
import com.example.investorssquare.game.service.TransactionService.payPriceForEstate
import com.example.investorssquare.game.service.TransactionService.payRent
import com.example.investorssquare.game.service.TransactionService.payTax
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.handleHighlightedFieldClicked
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.switchToBuildingMode
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.switchToMortgageMode
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.switchToRedeemMode
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.switchToSellingBuildingMode
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.switchToSellingPropertyMode
import com.example.investorssquare.game.service.highlighting_services.HighlightFieldsService.turnOffHighlighting
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
                    is Event.ON_SWITCH_TO_BUILDING_MODE -> switchToBuildingMode()
                    is Event.ON_SWITCH_TO_SELLING_BUILDING_MODE -> switchToSellingBuildingMode()
                    is Event.ON_SWITCH_TO_MORTGAGE_MODE -> switchToMortgageMode()
                    is Event.ON_SWITCH_TO_REDEEM_MODE -> switchToRedeemMode()
                    is Event.ON_SWITCH_TO_SELLING_PROPERTY_MODE -> switchToSellingPropertyMode()
                    is Event.ON_GO_TO_JAIL -> {
                        disableDice()
                        goToJail()
                    }
                    is Event.ON_PLAYER_LANDED_ON_FREE_ESTATE -> showPopupForEstate()
                    is Event.ON_FIELD_CLICKED -> {
                        val estate = getEstateByFieldIndex(event.fieldIndex)
                        if(!handleHighlightedFieldClicked(estate))
                            handleCardInformationClick(event.fieldIndex)
                    }
                    is Event.ON_ESTATE_BOUGHT -> {
                        if(payPriceForEstate(event.fieldIndex))
                            handleEstateBought(event.fieldIndex)
                    }
                    is Event.ON_MOVE_FINISHED -> {
                        turnOffHighlighting()
                        val player = getActivePlayer()!!
                        if(player.isInDebt.value){
                            openDebtPopup()
                        }
                        else{
                            handleDiceToTheNextPlayer()
                        }
                    }
                    is Event.ON_PLAYER_LANDED_ON_BOUGHT_ESTATE -> payRent()
                    is Event.ON_PLAYER_CROSSED_START -> collectSalary()
                    is Event.ON_PLAYER_LANDED_ON_TAX -> payTax()
                    is Event.ON_PLAYER_LANDED_ON_FREE_PARKING -> {
                        if(RuleBook.collectTaxesOnFreeParkingEnabled)
                            collectGatheredTaxes()
                        else if(RuleBook.playAgainOnFreeParkingEnabled)
                            enableDice()
                    }
                    is Event.ON_BAIL_OUT -> bailOut()
                    is Event.ON_ROLL_A_DOUBLE_TO_ESCAPE_JAIL -> rollADoubleToEscape()
                    is Event.ON_USE_GET_OUT_OF_JAIL_FREE_CARD -> useGetOutOfJailFreeCard()
                    is Event.ON_REPAY_DEBT -> dismissDebtPopup()
                    is Event.ON_DECLARE_BANKRUPTCY -> {
                        declareBankruptcy(getActivePlayer()!!)
                        turnOffHighlighting()
                        handleDiceToTheNextPlayer()
                    }
                }
            }
        }
    }
}