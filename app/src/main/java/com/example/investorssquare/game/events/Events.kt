package com.example.investorssquare.game.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object EventBus {
    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    suspend fun postEvent(event: Event) {
        _events.emit(event)
    }
}

sealed class Event {
    data class DiceThrown(val firstNumber: Int, val secondNumber: Int) : Event()
    data object PlayerCrossedStart : Event()
    data object PlayerLanded : Event()
    data object PlayerLandedOnBoughtEstate : Event()
    data object PlayerLandedOnFreeEstate : Event()
    data class OnFieldClicked(val fieldIndex: Int): Event()
    data class BuyingEstate(val fieldIndex: Int): Event()
    data class EstateBought(val fieldIndex: Int): Event()
}
