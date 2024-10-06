package com.example.investorssquare.game.presentation.board_screen.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
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

    var doublesRolledCounter: Int = 0

    fun buyNewEstate(estate: EstateViewModel) {
        estate.setOwnerIndex(index.value)
        _estates.value += estate
    }

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

    fun moveToField(field: Int) {
        if (field < TOTAL_FIELDS) _position.value = field
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

    fun canBuyEstate(currentField: Field): Boolean {
        if (currentField !is Estate) {
            return false
        }

        val isOnCorrectField = position.value == currentField.index
        val alreadyOwnsEstate = _estates.value.any { it.estate.index == currentField.index }

        return isOnCorrectField && !alreadyOwnsEstate
    }
}