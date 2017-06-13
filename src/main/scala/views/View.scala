package views

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
  def render: Unit
}
