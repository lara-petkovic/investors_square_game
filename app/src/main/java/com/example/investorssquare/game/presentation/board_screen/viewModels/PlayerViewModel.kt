package com.example.investorssquare.game.presentation.board_screen.viewModels

import android.os.CountDownTimer
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.investorssquare.game.service.MoveService
import com.example.investorssquare.util.Constants.NUMBER_OF_FIELDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> get() = _isActive

    private val _index = MutableStateFlow(0)
    val index: StateFlow<Int> get() = _index

    private val _color = MutableStateFlow(Color.Black)
    val color: StateFlow<Color> get() = _color

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _money = MutableStateFlow(0)
    val money: StateFlow<Int> get() = _money

    private val _position = MutableStateFlow(0)
    val position: StateFlow<Int> get() = _position

    private val _estates = MutableStateFlow<List<EstateViewModel>>(emptyList())
    val estates: StateFlow<List<EstateViewModel>> get() = _estates

    val timeSeconds = 20 // IZBACITI

    private val _remainingTime = MutableStateFlow(timeSeconds)
    val remainingTime: StateFlow<Int> get() = _remainingTime

    private var turnTimer: CountDownTimer? = null
    
    var doublesRolledCounter: Int = 0

    fun buyNewEstate(estate: EstateViewModel) {
        estate.setOwnerIndex(index.value)
        _estates.value += estate
    }

    fun moveBySteps(steps: Int) {
        _position.value = (_position.value + steps) % NUMBER_OF_FIELDS
    }

    fun moveByStepsBackwards(steps: Int) {
        _position.value = (_position.value - steps + NUMBER_OF_FIELDS) % NUMBER_OF_FIELDS
    }

    fun moveToField(field: Int) {
        if (field < NUMBER_OF_FIELDS) _position.value = field
    }

    fun pay(price: Int) {
        _money.value -= price
    }

    fun receive(amount: Int) {
        _money.value += amount
    }

    fun finishMove() {
        doublesRolledCounter = 0
        _isActive.value = false
        stopTurnTimer()
    }

    fun startMove() {
        _isActive.value = true
        startTurnTimerOnMainThread()
    }

    fun setMoney(money: Int) {
        _money.value = money
    }

    fun setColor(color: Color) {
        _color.value = color
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun setPosition(position: Int) {
        _position.value = position
    }

    fun setName(name: String) {
        _name.value = name
    }

    private fun startTurnTimerOnMainThread() {
        CoroutineScope(Dispatchers.Main).launch {
            startTurnTimer()
        }
    }

    private fun startTurnTimer() {
        turnTimer = object : CountDownTimer((timeSeconds * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {

                finishMove()
                MoveService.handleDiceToTheNextPlayer()
                _remainingTime.value = 0
            }
        }.start()
    }


    private fun stopTurnTimer() {
        turnTimer?.cancel()
        _remainingTime.value = timeSeconds
    }
}