package com.example.investorssquare.game.service

import com.example.investorssquare.game.presentation.board_screen.viewModels.RuleBook
import com.example.investorssquare.game.events.Event
import com.example.investorssquare.game.events.EventBus
import com.example.investorssquare.game.service.BankruptcyService.declareBankruptcy
import com.example.investorssquare.game.service.BankruptcyService.dismissDebtPopup
import com.example.investorssquare.game.service.BankruptcyService.openDebtPopup
import com.example.investorssquare.game.service.community_cards_services.CommunityCardService.executeCardAction
import com.example.investorssquare.game.service.community_cards_services.CommunityCardService.openCardPopup
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
import com.example.investorssquare.game.service.TradeService.acceptTradeOffer
import com.example.investorssquare.game.service.TradeService.cancelCounterOffer
import com.example.investorssquare.game.service.TradeService.changeTargetedPlayer
import com.example.investorssquare.game.service.TradeService.counterOffer
import com.example.investorssquare.game.service.TradeService.counterTradeOffer
import com.example.investorssquare.game.service.TradeService.createTradeOffer
import com.example.investorssquare.game.service.TradeService.creatingTradeOffer
import com.example.investorssquare.game.service.TradeService.declineTradeOffer
import com.example.investorssquare.game.service.TradeService.estateClicked
import com.example.investorssquare.game.service.TradeService.openTradeWindow
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
//community cards
                    is Event.ON_COMMUNITY_CARD_CLOSED -> executeCardAction()
                    is Event.ON_COMMUNITY_CARD_OPENED -> openCardPopup()
//player movement
                    is Event.ON_DICE_THROWN -> handleDiceThrown(event.firstNumber, event.secondNumber)
                    is Event.ON_MOVE_PLAYER -> moveActivePlayer()
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
                    is Event.ON_PLAYER_CROSSED_START -> collectSalary()
//player landing
                    is Event.ON_PLAYER_LANDED_ON_FREE_ESTATE -> showPopupForEstate()
                    is Event.ON_PLAYER_LANDED_ON_BOUGHT_ESTATE -> payRent()

                    is Event.ON_PLAYER_LANDED_ON_TAX -> payTax()
                    is Event.ON_PLAYER_LANDED_ON_FREE_PARKING -> {
                        if(RuleBook.collectTaxesOnFreeParkingEnabled)
                            collectGatheredTaxes()
                        else if(RuleBook.playAgainOnFreeParkingEnabled)
                            enableDice()
                    }
//highlight modes
                    is Event.ON_SWITCH_TO_BUILDING_MODE -> switchToBuildingMode()
                    is Event.ON_SWITCH_TO_SELLING_BUILDING_MODE -> switchToSellingBuildingMode()
                    is Event.ON_SWITCH_TO_MORTGAGE_MODE -> switchToMortgageMode()
                    is Event.ON_SWITCH_TO_REDEEM_MODE -> switchToRedeemMode()
                    is Event.ON_SWITCH_TO_SELLING_PROPERTY_MODE -> switchToSellingPropertyMode()
//buying estates
                    is Event.ON_FIELD_CLICKED -> {
                        val estate = getEstateByFieldIndex(event.fieldIndex)
                        if(!handleHighlightedFieldClicked(estate))
                            handleCardInformationClick(event.fieldIndex)
                    }
                    is Event.ON_ESTATE_BOUGHT -> {
                        if(payPriceForEstate(event.fieldIndex))
                            handleEstateBought(event.fieldIndex)
                    }
//jail
                    is Event.ON_GO_TO_JAIL -> {
                        disableDice()
                        goToJail()
                    }
                    is Event.ON_BAIL_OUT -> bailOut()
                    is Event.ON_ROLL_A_DOUBLE_TO_ESCAPE_JAIL -> rollADoubleToEscape()
                    is Event.ON_USE_GET_OUT_OF_JAIL_FREE_CARD -> useGetOutOfJailFreeCard()
//bankruptcy
                    is Event.ON_REPAY_DEBT -> dismissDebtPopup()
                    is Event.ON_DECLARE_BANKRUPTCY -> {
                        declareBankruptcy(getActivePlayer()!!)
                        turnOffHighlighting()
                        handleDiceToTheNextPlayer()
                    }
                    is Event.ON_PLAYER_WON -> TODO()
//trades
                    is Event.ON_ACCEPTING_A_TRADE -> acceptTradeOffer()
                    is Event.ON_CANCEL_COUNTER_TRADE_OFFER -> cancelCounterOffer()
                    is Event.ON_CARD_CLICKED -> estateClicked(event.fieldIndex)
                    is Event.ON_CHANGE_TARGET_PLAYER -> changeTargetedPlayer(event.next)
                    is Event.ON_COUNTER_TRADE_OFFER -> counterOffer()
                    is Event.ON_CREATING_A_TRADE -> createTradeOffer()
                    is Event.ON_DECLINING_A_TRADE -> declineTradeOffer()
                    is Event.ON_OFFER_A_TRADE -> openTradeWindow()

                    else->{}
                }
            }
        }
    }
}