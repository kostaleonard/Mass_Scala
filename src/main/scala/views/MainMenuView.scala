package views

import actions.Action
import board.Location
import fighter.Fighter
import model.Model

/**
  * Created by Leonard on 7/16/2017.
  */
class MainMenuView(model: Model) extends View(model) {
  override def showStartScreen: Unit = ???
  override def showMainMenu: Unit = ???
  override def showBoard: Unit = ???
  override def showPlayerParty: Unit = ???
  override def showBoardCommands: Unit = ???
  override def showAvailableMoves(moves: Set[Location]): Unit = ???
  override def showAvailableActions(fighter: Fighter, actions: Iterable[Action]): Unit = ???
  override def showChosenAction(fighter: Fighter, action: Action): Unit = ???
  override def render: Unit = ???
}
