package com.example.investorssquare.game.service

import com.example.investorssquare.game.domain.model.Board
import com.example.investorssquare.game.domain.model.Estate
import com.example.investorssquare.game.domain.model.Field
import com.example.investorssquare.game.presentation.board_screen.viewModels.EstateViewModel
import com.example.investorssquare.game.service.EstateService.setEstates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BoardService {
    private val _board = MutableStateFlow<Board?>(null)
    val board: StateFlow<Board?> get() = _board
    fun setBoard(board: Board) {
        _board.value = board
        setEstates(board.fields.filterIsInstance<Estate>().map { EstateViewModel(it) })
    }

    private val _currentField = MutableStateFlow<Field?>(null)
    val currentField: StateFlow<Field?> get() = _currentField

    //highlighting board
    private val _highlightMode = MutableStateFlow<Boolean>(false)
    val highlightMode: StateFlow<Boolean> = _highlightMode
    fun turnOnHighlightMode(){
        _highlightMode.value = true
    }
    fun turnOffHighlightMode(){
        _highlightMode.value = false
    }

    //popup on the board
    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> get() = _showPopup
    fun dismissPopupForField() {
        _showPopup.value = false
    }
    fun showPopupForField(fieldIndex: Int) {
        _currentField.value = _board.value?.fields?.get(fieldIndex)
        _showPopup.value = true
    }
}