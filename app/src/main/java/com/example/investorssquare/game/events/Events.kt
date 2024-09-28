package com.example.investorssquare.game.events

import com.example.investorssquare.game.presentation.board_screen.viewModels.BoardVMEvent
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
    data class PlayerCrossedStart() : Event()
    data class OnFieldClicked(val fieldIndex: Int): Event()
}
