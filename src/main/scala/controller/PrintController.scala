package controller

import views.{PrintView, ViewManager}
import model.Model
import board.Board
import board.Location
import fighter.Fighter

/**
  * Created by Leonard on 7/16/2017.
  */
object PrintController {
  val ANY_INPUT = 0

  def main(args: Array[String]): Unit = {
    val controller = new Controller

    //controller.viewManager.setCurrentView(new MainMenuView(controller.model))
    //controller.viewManager.setupFrame
    //controller.viewManager.showMainMenu

    controller.playGame
    controller.exitGame
  }
}

class PrintController {
  private val model = Model.loadOrCreate(getProfileName)
  //private val viewManager = new ViewManager(new MainMenuView(model))
  private val view = new PrintView(model)
  private val viewManager = new ViewManager(view)

  def getProfileName: String = {
    //TODO get a profile name from the user (through the View--either saved or created).
    "Leo"
  }

  def waitForCharInput: Option[Char] = {
    try {
      Some(scala.io.StdIn.readChar())
    } catch{ case e: java.lang.StringIndexOutOfBoundsException => None }
  }

  def waitForStringInput: Option[String] = {
    try {
      Some(scala.io.StdIn.readLine())
    } catch{ case e: java.lang.StringIndexOutOfBoundsException => None }
  }

  def startGame: Unit = {
    view.showStartScreen
    this.waitForCharInput
    this.runMainMenu
  }

  def runMainMenu: Unit = {
    view.showMainMenu
    val command = this.waitForCharInput
    command match{
      case Some('c') => customizeParty
      case Some('r') => playRandomBoard
      case _ => System.out.println("Unrecognized command")
    }
  }

  def customizeParty: Unit = {
    //TODO customizeParty function
    ???
  }

  def playRandomBoard: Unit = {
    //TODO remove playRandomBoard function
    model.setCurrentBoard(Some(Board.getTestBoard))
    val fighterArray = model.getPlayerParty.getFighters.toArray
    fighterArray.indices.foreach { i =>
      fighterArray(i).setLocation(Location(i, i))
      //model.getCurrentBoard.get.placeFighterOnBoard(fighterArray(i))
    }
    model.getCurrentBoard.get.placePlayerPartyOnBoard(model.getPlayerParty)
    while(model.isBoardActive){
      doTurn
    }
  }

  def doTurn: Unit = {
    doPlayerTurn
    doEnemyTurn
    recoverMovesAndActions
    doTurnlyEffects
  }

  def recoverMovesAndActions: Unit = {
    //Allow Fighters to recover moves and actions so that they can use them next turn.
    model.recoverMovesAndActions
  }

  def doTurnlyEffects: Unit = {
    //Allow Fighters to recover HP/shields, EEZO, and do any turnly effects.
    model.doTurnlyActions
  }

  def doPlayerTurn: Unit = {
    var endTurn = false
    while(!endTurn) {
      view.showBoard
      view.showPlayerParty
      view.showBoardCommands
      val command = this.waitForStringInput
      command match {
        case Some("q") =>
          System.out.println("Exiting")
          exitGame
        case Some("e") =>
          System.out.println("Ending turn")
          endTurn = true
        case Some("m") => movePlayerFighter
        case Some("a") => doPlayerAction
        case _ => System.out.println("Unrecognized command")
      }
    }
  }

  private def getNumFromInput(in: Option[String]): Option[Int] = in match {
    case Some(str) =>
      if(str.matches("[0-9]+")) Some(str.toInt)
      else {
        System.out.println("Unrecognized number.")
        None
      }
    case _ =>
      System.out.println("Unrecognized number.")
      None
  }

  private def getFighterFromInput: Option[Fighter] = {
    System.out.println("Enter from row: ")
    val fromRow = getNumFromInput(this.waitForStringInput)
    System.out.println("Enter from col: ")
    val fromCol = getNumFromInput(this.waitForStringInput)
    if (fromRow.isEmpty || fromCol.isEmpty) None
    else model.fighterAt(Location(fromRow.get, fromCol.get))
  }

  def doPlayerAction: Unit = {
    val fighter = getFighterFromInput
    if(fighter == None){
      System.out.println("No fighter at that location.")
      return
    }
    else if(!fighter.get.canAttack){
      System.out.println("That fighter cannot attack.")
      return
    }
    val actionsArray = model.getAvailableActions(fighter.get).toArray
    view.showAvailableActions(fighter.get, actionsArray)
    System.out.println("Which action (0..n)?")
    val choice = getNumFromInput(this.waitForStringInput)
    if(choice.isEmpty || choice.get < 0 || choice.get >= actionsArray.length) System.out.println("Unrecognized choice")
    else model.doFighterAction(actionsArray(choice.get))
  }

  def movePlayerFighter: Unit = {
    def getEndLocation: Option[Location] = {
      System.out.println("Enter to row: ")
      val toRow = getNumFromInput(this.waitForStringInput)
      System.out.println("Enter to col: ")
      val toCol = getNumFromInput(this.waitForStringInput)
      if(toRow.isEmpty || toCol.isEmpty) None
      else Some(Location(toRow.get, toCol.get))
    }
    val fighter = getFighterFromInput
    if(fighter == None){
      System.out.println("No fighter at that location.")
      return
    }
    else if(!fighter.get.canMove){
      System.out.println("That fighter cannot move.")
      return
    }
    val moves = model.getAvailableMoves(fighter.get)
    view.showAvailableMoves(moves)
    val endLocation = getEndLocation
    if(endLocation == None){
      System.out.println("Not a valid location.")
      return
    }
    if(!moves(endLocation.get)) System.out.println("The Fighter cannot move to that location.")
    else model.moveFighter(fighter.get.getLocation, endLocation.get)
  }

  def doEnemyTurn: Unit = {
    //TODO give this to an AI handler class
    def doRandomMove(fighter: Fighter): Unit = {
      val moveChoices = scala.util.Random.shuffle(model.getAvailableMoves(fighter).toList)
      if(moveChoices.nonEmpty) model.moveFighter(fighter.getLocation, moveChoices.head)
    }
    def doRandomAction(fighter: Fighter): Unit = {
      val actionChoices = scala.util.Random.shuffle(model.getAvailableActions(fighter).toList)
      view.showAvailableActions(fighter, actionChoices.toSet)
      if(actionChoices.nonEmpty){
        view.showChosenAction(fighter, actionChoices.head)
        model.doFighterAction(actionChoices.head)
      }

    }
    model.getCurrentBoard.get.getEnemyParty.getFighters.foreach{ f =>
      while(f.canMove) doRandomMove(f)
      while(f.canAttack) doRandomAction(f)
    }
  }

  def exitGame: Unit = {
    System.exit(0)
  }
}
