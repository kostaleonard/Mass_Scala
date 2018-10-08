package controller

import actions.Action
import board.{Board, Location}
import fighter.Fighter
import views.View

/**
  * Created by Leonard on 10/8/2018.
  */
sealed trait ControllerMessage

case class SwitchViews(nextView: View) extends ControllerMessage

case class DoAction(action: Action) extends ControllerMessage

case class MoveFighter(board: Board, fighter: Fighter, location: Location) extends ControllerMessage

case object EndTurn extends ControllerMessage

case class SendKeyPress(keyCode: Int) extends ControllerMessage
