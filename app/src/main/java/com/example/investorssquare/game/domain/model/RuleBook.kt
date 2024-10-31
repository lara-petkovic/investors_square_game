package com.example.investorssquare.game.domain.model

class RuleBook(
    var playAgainIfRolledDouble: Boolean = true,
    var auctionsEnabled: Boolean = true,
    var gatheringTaxesEnabled: Boolean = true,
    var collectTaxesOnFreeParkingEnabled: Boolean = true,
    var playAgainOnFreeParkingEnabled: Boolean = false,
    var rollADoubleToEscapeJailEnabled: Boolean = true,
    var collectRentsWhileInJail: Boolean = false,
    var payToEscapeJailEnabled: Boolean = true,
    var mortgagesEnabled: Boolean = true,
    var sellingEstateEnabled: Boolean = false,
    var doubleRentOnCollectedSetsEnabled: Boolean = true,
    var speedDieEnabled: Boolean = false,
    var payingTaxesViaPercentagesEnabled: Boolean = true,
    var isSetNecessaryToBuild: Boolean = false,
    var isVisitNecessaryToBuild: Boolean = false,
    var evenlyBuilding: Boolean = false,
    var buildingOnMultiplePropertiesInOneMoveEnabled: Boolean = true,

    var salary: Int = 200,
    var startingCapital: Int = 1500,
    var jailEscapePrice: Int = 100,
    var jailSentenceInMoves: Int = 3,
    var timePerMoveInSeconds: Int = 60,
    var buildingsPerMovePerProperty: Int = 10,
    var numberOfPlayers: Int = 2,
    var doublesRolledLimit: Int = 3
)