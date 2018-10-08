package controller

import views.{MainMenuView, PrintView, View, ViewManager}
import model.Model
import board.Board
import board.Location
import fighter.Fighter

/**
  * Created by Leonard on 6/3/2017.
  */
object Controller {
  val ANY_INPUT = 0
  val GAME_TITLE = "Saving Planet Ryan"

  def main(args: Array[String]): Unit = {
    val controller = new Controller
    controller.playGame
  }
}

class Controller {
  private val model = Model.loadOrCreate(getProfileName)
  private val viewManager = new ViewManager(new MainMenuView(model), this)

  def getProfileName: String = {
    //TODO get a profile name from the user (through the View--either saved or created).
    "Leo"
  }

  def playGame: Unit = runMainMenu

  def runMainMenu: Unit = viewManager.setCurrentView(new MainMenuView(model))

  def playRandomBoard: Unit = {
    //TODO remove playRandomBoard function
    model.setCurrentBoard(Some(Board.getTestBoard))
    val fighterArray = model.getPlayerParty.getFighters.toArray
    fighterArray.indices.foreach { i =>
      fighterArray(i).setLocation(Location(i, i))
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
    /*
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
    */
  }

  def doPlayerAction: Unit = {
  /*
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
    */
  }

  def movePlayerFighter: Unit = {
    /*
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
    */
  }

  def doEnemyTurn: Unit = {
    /*
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
    */
  }

  def changeViews(nextView: View): Unit = {
    viewManager.getCurrentView.setNextView(None)
    viewManager.setCurrentView(nextView)
  }

  //Key methods:
  def keyPressed(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyPressed(keyCode)
    viewManager.getCurrentView.getNextView.map(nextView => changeViews(nextView))
  }
  def keyReleased(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyReleased(keyCode)
    viewManager.getCurrentView.getNextView.map(nextView => changeViews(nextView))
  }
  def keyTyped(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyTyped(keyCode)
    viewManager.getCurrentView.getNextView.map(nextView => changeViews(nextView))
  }
  def keyHeld(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyHeld(keyCode)
    viewManager.getCurrentView.getNextView.map(nextView => changeViews(nextView))
  }

  def exitGame: Unit = {
    System.exit(0)
  }
}
