package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.service.BankruptcyService
import com.example.investorssquare.game.service.EstateService.estates
import com.example.investorssquare.util.Constants.TOTAL_FIELDS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PlayerViewModel @Inject constructor() : ViewModel() {

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> get() = _isActive

    private val _jailSentence = MutableStateFlow(0)
    val jailSentence: StateFlow<Int> get() = _jailSentence

    private val _numberOfGetOutOfJailFreeCards = MutableStateFlow(0)
    val numberOfGetOutOfJailFreeCards: StateFlow<Int> get() = _numberOfGetOutOfJailFreeCards

    private val _isInJail = MutableStateFlow(false)
    val isInJail: StateFlow<Boolean> get() = _isInJail

    private val _isInDebt = MutableStateFlow(false)
    val isInDebt: StateFlow<Boolean> get() = _isInDebt

    private val _isInBankruptcy = MutableStateFlow(false)
    val isInBankruptcy: StateFlow<Boolean> get() = _isInBankruptcy

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

    var doublesRolledCounter: Int = 0

    fun acquireGetOutOfJailFreeCard(){
        _numberOfGetOutOfJailFreeCards.value++
    }

    fun useGetOutOfJailFreeCard(){
        _numberOfGetOutOfJailFreeCards.value--
    }

    fun moveBySteps(steps: Int) {
        _position.value = (_position.value + steps) % TOTAL_FIELDS
    }

    fun goToJail(sentence: Int){
        _isInJail.value = true
        _jailSentence.value = sentence + 1
    }

    fun escapeJail(){
        _isInJail.value = false
        _jailSentence.value = 0
    }

    fun moveByStepsBackwards(steps: Int) {
        _position.value = (_position.value - steps + TOTAL_FIELDS) % TOTAL_FIELDS
    }

    fun pay(price: Int) {
        _money.value -= price
        _isInDebt.value = _money.value<0
    }

    fun receive(amount: Int) {
        _money.value += amount
        _isInDebt.value = _money.value<0
    }

    fun finishMove() {
        doublesRolledCounter = 0
        _isActive.value = false
        if(isInJail.value){
            _jailSentence.value--
            _isInJail.value = _jailSentence.value>0
        }
    }

    fun startMove() {
        _isActive.value = true
    }

    fun setMoney(money: Int) {
        _money.value = money
        _isInDebt.value = _money.value<0
    }

    fun setColor(color: Color) {
        _color.value = color
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun canBuyEstate(currentField: Field): Boolean {
        if (currentField !is Estate) {
            return false
        }

        val isOnCorrectField = position.value == currentField.index
        val alreadyOwnsEstate = estates.value.any { it.estate.index == currentField.index && it.isOwnedByPlayer(this)}

        return isOnCorrectField && !alreadyOwnsEstate
    }

    fun bankrupt(){
        _isInBankruptcy.value = true
        for(estate in estates.value.filter { e->e.isOwnedByPlayer(this) }){
            estate.reset()
        }
    }
}