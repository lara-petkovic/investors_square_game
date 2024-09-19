package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DiceViewModel @Inject constructor() : ViewModel() {
    private val _diceNumber1 = MutableStateFlow(1)
    val diceNumber1: StateFlow<Int> get() = _diceNumber1

    private val _diceNumber2 = MutableStateFlow(1)
    val diceNumber2: StateFlow<Int> get() = _diceNumber2

    private val _isDiceButtonEnabled = MutableStateFlow(true)
    val isDiceButtonEnabled: StateFlow<Boolean> get() = _isDiceButtonEnabled

    fun rollDice() {
        _diceNumber1.value = Random.nextInt(1, 7)
        _diceNumber2.value = Random.nextInt(1, 7)
    }
    fun getDiceSum(): Int{
        return _diceNumber1.value + _diceNumber2.value
    }
    fun isRolledDouble(): Boolean{
        return _diceNumber1.value==_diceNumber2.value
    }
    fun disableDiceButton(){
        _isDiceButtonEnabled.value = false
    }
    fun enableDiceButton(){
        _isDiceButtonEnabled.value = true
    }
}
