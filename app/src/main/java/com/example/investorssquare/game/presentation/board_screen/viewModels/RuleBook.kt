package com.example.investorssquare.game.presentation.board_screen.viewModels

object RuleBook {
    // General rules
    var salary: Int = 200
    var startingCapital: Int = 1500
    var numberOfPlayers: Int = 2
    var timePerMoveInSeconds: Int = 60

    // Jail rules
    var rollADoubleToEscapeJailEnabled: Boolean = true
    var collectRentsWhileInJail: Boolean = true
    var payToEscapeJailEnabled: Boolean = true
    var jailEscapePrice: Int = 100
    var jailSentenceInMoves: Int = 3

    // Tax rules
    var gatheringTaxesEnabled: Boolean = true
    var collectTaxesOnFreeParkingEnabled: Boolean = true
    var payingTaxesViaPercentagesEnabled: Boolean = true

    // Property rules
    var auctionsEnabled: Boolean = true
    var mortgagesEnabled: Boolean = true
    var sellingEstateEnabled: Boolean = false
    var doubleRentOnCollectedSetsEnabled: Boolean = true

    // Dice rules
    var playAgainIfRolledDouble: Boolean = true
    var speedDieEnabled: Boolean = false
    var doublesRolledLimit: Int = 3
    var playAgainOnFreeParkingEnabled: Boolean = false

    // Building rules
    var isSetNecessaryToBuild: Boolean = true
    var isVisitNecessaryToBuild: Boolean = false
    var evenlyBuilding: Boolean = true
    var buildingOnMultiplePropertiesInOneMoveEnabled: Boolean = true
    var buildingsPerMovePerProperty: Int = 0
}