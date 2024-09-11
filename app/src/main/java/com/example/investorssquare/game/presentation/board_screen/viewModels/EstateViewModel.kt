package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.lifecycle.ViewModel
import com.example.investorssquare.game.domain.model.Estate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class EstateViewModel @Inject constructor(estate: Estate) : ViewModel() {
    private val _estate = MutableStateFlow(estate)
    val estate: StateFlow<Estate> get() = _estate

    private val _ownerIndex = MutableStateFlow(-1)
    val ownerIndex: StateFlow<Int> get() = _ownerIndex

    fun setOwnerIndex(ownerIndex: Int) {
        _ownerIndex.value = ownerIndex
    }
}