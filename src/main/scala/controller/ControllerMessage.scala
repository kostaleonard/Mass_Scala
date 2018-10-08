package controller

import views.View

/**
  * Created by Leonard on 10/8/2018.
  */
sealed trait ControllerMessage

case class SwitchViews(nextView: View) extends ControllerMessage

case object EndTurn extends ControllerMessage
