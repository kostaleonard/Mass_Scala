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

  def doEnemyPhase: Unit = {
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

  def doEnemyTurn: Unit = {
    //TODO give this to an AI handler class
    //TODO show this in the view.
    def doRandomMove(fighter: Fighter): Unit = {
      val moveChoices = scala.util.Random.shuffle(model.getAvailableMoves(fighter).toList)
      if(moveChoices.nonEmpty) model.moveFighter(fighter.getLocation, moveChoices.head)
    }
    def doRandomAction(fighter: Fighter): Unit = {
      val actionChoices = scala.util.Random.shuffle(model.getAvailableActions(fighter).toList)
      //view.showAvailableActions(fighter, actionChoices.toSet)
      if(actionChoices.nonEmpty){
        //view.showChosenAction(fighter, actionChoices.head)
        model.doFighterAction(actionChoices.head)
      }

    }
    model.getCurrentBoard.get.getEnemyParty.getFighters.foreach{ f =>
      while(f.canMove) doRandomMove(f)
      while(f.canAttack) doRandomAction(f)
    }
  }

  def clearControllerMessages: Unit = viewManager.getCurrentView.clearControllerMessages

  def checkControllerMessages: Unit = {
    viewManager.getCurrentView.getControllerMessages.foreach(m => m match{
      case SwitchViews(nextView) => changeViews(nextView)
      case EndTurn => doEnemyPhase
      case _ => throw new UnsupportedOperationException("Unrecognized Controller Message: " + m)
    })
    clearControllerMessages
  }

  def changeViews(nextView: View): Unit = viewManager.setCurrentView(nextView)

  //Key methods:
  def keyPressed(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyPressed(keyCode)
    checkControllerMessages
  }
  def keyReleased(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyReleased(keyCode)
    checkControllerMessages
  }
  def keyTyped(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyTyped(keyCode)
    checkControllerMessages
  }
  def keyHeld(keyCode: Int): Unit = {
    viewManager.getCurrentView.keyHeld(keyCode)
    checkControllerMessages
  }

  def exitGame: Unit = {
    System.exit(0)
  }
}
