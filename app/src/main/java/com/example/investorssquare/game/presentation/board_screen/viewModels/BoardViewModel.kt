package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.CommunityCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor() : ViewModel() {

    private val _currentChanceCard = MutableStateFlow<CommunityCard?>(null)
    val currentChanceCard: StateFlow<CommunityCard?> = _currentChanceCard

    private val _currentCommunityChestCard = MutableStateFlow<CommunityCard?>(null)
    val currentCommunityChestCard: StateFlow<CommunityCard?> = _currentCommunityChestCard

    init {
        shuffleCards()
    }

    fun shuffleCards() {
        //board.shuffleCommunityCards()
    }

    fun drawChanceCard() {
//        viewModelScope.launch {
//            _currentChanceCard.value = board.drawChanceCard()
//        }
    }

    fun drawCommunityChestCard() {
//        viewModelScope.launch {
//            _currentCommunityChestCard.value = board.drawCommunityChestCard()
//        }
    }
}