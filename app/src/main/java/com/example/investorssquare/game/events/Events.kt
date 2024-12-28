package com.example.investorssquare.game.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun postEvent(event: Event) {
        _events.emit(event)
    }
}

sealed class Event {
    data class ON_DICE_THROWN(val firstNumber: Int, val secondNumber: Int) : Event()
    data object ON_PLAYER_CROSSED_START : Event()
    data object ON_MOVE_FINISHED : Event()
    data object ON_BAIL_OUT : Event()
    data object ON_USE_GET_OUT_OF_JAIL_FREE_CARD : Event()
    data object ON_ROLL_A_DOUBLE_TO_ESCAPE_JAIL : Event()
    data object ON_MOVE_PLAYER : Event()
    data object ON_GO_TO_JAIL : Event()
    data object ON_COMMUNITY_CARD_CLOSED : Event()
    data object ON_COMMUNITY_CARD_OPENED : Event()
    data object ON_PLAYER_LANDED_ON_FREE_PARKING : Event()
    data object ON_PLAYER_LANDED_ON_TAX : Event()
    data object ON_PLAYER_LANDED_ON_BOUGHT_ESTATE : Event()
    data object ON_PLAYER_LANDED_ON_FREE_ESTATE : Event()
    data object ON_SWITCH_TO_BUILDING_MODE : Event()
    data object ON_SWITCH_TO_MORTGAGE_MODE : Event()
    data object ON_SWITCH_TO_REDEEM_MODE : Event()
    data object ON_SWITCH_TO_SELLING_MODE : Event()
    data class ON_FIELD_CLICKED(val fieldIndex: Int): Event()
    data class ON_ESTATE_BOUGHT(val fieldIndex: Int): Event()
}
