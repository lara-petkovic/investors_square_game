package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.lifecycle.ViewModel
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.FieldType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class EstateViewModel @Inject constructor(val estate: Estate) : ViewModel() {
    val isUtility: Boolean = estate.type == FieldType.UTILITY
    val isStation: Boolean = estate.type == FieldType.STATION
    val isProperty: Boolean = estate.type == FieldType.PROPERTY

    private val _numberOfBuildings = MutableStateFlow(0)
    val numberOfBuildings : StateFlow<Int> get() = _numberOfBuildings

    private val _ownerIndex = MutableStateFlow(-1)
    val ownerIndex: StateFlow<Int> get() = _ownerIndex

    fun setOwnerIndex(ownerIndex: Int) {
        _ownerIndex.value = ownerIndex
    }
    fun isOwnedByPlayer(player: PlayerViewModel): Boolean {
        return ownerIndex.value == player.index.value
    }
    fun isFullyBuilt(): Boolean {
        return numberOfBuildings.value == estate.rent.size-1
    }
}