package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.lifecycle.ViewModel
import com.example.investorssquare.game.domain.model.CommunityCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor() : ViewModel() {

    private val _currentChanceCard = MutableStateFlow<CommunityCard?>(null)
    val currentChanceCard: StateFlow<CommunityCard?> = _currentChanceCard

    private val _currentCommunityChestCard = MutableStateFlow<CommunityCard?>(null)
    val currentCommunityChestCard: StateFlow<CommunityCard?> = _currentCommunityChestCard

}