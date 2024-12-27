package com.example.investorssquare.game.domain.model

class RuleBook(
    //common rules
    var salary: Int = 200,
    var startingCapital: Int = 1500,
    var timePerMoveInSeconds: Int = 60,
    var numberOfPlayers: Int = 2,

    //jail rules
    var rollADoubleToEscapeJailEnabled: Boolean = true,
    var collectRentsWhileInJail: Boolean = false,
    var payToEscapeJailEnabled: Boolean = true,
    var jailEscapePrice: Int = 100,
    var jailSentenceInMoves: Int = 3,

    //taxes rules
    var gatheringTaxesEnabled: Boolean = true,
    var collectTaxesOnFreeParkingEnabled: Boolean = true,
    var payingTaxesViaPercentagesEnabled: Boolean = true,

    //property rules
    var auctionsEnabled: Boolean = true,
    var mortgagesEnabled: Boolean = true,
    var sellingEstateEnabled: Boolean = false,
    var doubleRentOnCollectedSetsEnabled: Boolean = true,

    //dice rules
    var playAgainIfRolledDouble: Boolean = true,
    var speedDieEnabled: Boolean = false,
    var doublesRolledLimit: Int = 3,
    var playAgainOnFreeParkingEnabled: Boolean = false,

    //building rules
    var isSetNecessaryToBuild: Boolean = true,
    var isVisitNecessaryToBuild: Boolean = false,
    var evenlyBuilding: Boolean = true,
    var buildingOnMultiplePropertiesInOneMoveEnabled: Boolean = true,
    var buildingsPerMovePerProperty: Int = 0,
)