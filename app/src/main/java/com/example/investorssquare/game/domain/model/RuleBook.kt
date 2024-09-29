package com.example.investorssquare.game.domain.model

class RuleBook(
    var playAgainIfRolledDouble: Boolean = true,
    var auctionsEnabled: Boolean = true,
    var gatheringTaxesEnabled: Boolean = true,
    var collectTaxesOnFreeParkingEnabled: Boolean = true,
    var playAgainOnFreeParkingEnabled: Boolean = false,
    var rollADoubleToEscapeJailEnabled: Boolean = true,
    var payToEscapeJailEnabled: Boolean = true,
    var mortgagesEnabled: Boolean = true,
    var sellingEstateEnabled: Boolean = false,
    var doubleRentOnCollectedSetsEnabled: Boolean = true,
    var speedDieEnabled: Boolean = false,
    var payingTaxesViaPercentagesEnabled: Boolean = false,
    var isSetNecessaryToBuild: Boolean = true,
    var isVisitNecessaryToBuild: Boolean = true,
    var evenlyBuilding: Boolean = false,
    var buildingOnMultiplePropertiesInOneMoveEnabled: Boolean = false,

    var salary: Int = 200,
    var startingCapital: Int = 1500,
    var jailEscapePrice: Int = 100,
    var jailSentenceInMoves: Int = 3,
    var timePerMoveInSeconds: Int = 60,
    var buildingsPerMovePerProperty: Int = 1,
    var numberOfPlayers: Int = 2,
    var doublesRolledLimit: Int = 3
) {
}