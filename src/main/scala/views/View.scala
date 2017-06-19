package views

import actions.Action
import board.Location
import fighter.Fighter
import model.Model

/**
  * Created by Leonard on 6/3/2017.
  */
abstract class View(model: Model) {
  private var this.model: Model = model

  //Abstract methods
  def showStartScreen: Unit
  def showMainMenu: Unit
  def showBoard: Unit
  def showPlayerParty: Unit
  def showBoardCommands: Unit
  def showAvailableMoves(moves: scala.collection.immutable.Set[Location]): Unit
  def showAvailableActions(fighter: Fighter, actions: scala.collection.immutable.Set[Action]): Unit
  def showChosenAction(fighter: Fighter, action: Action): Unit
  def render: Unit
}
