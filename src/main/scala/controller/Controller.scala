package controller

import views.PrintView
import model.Model
import board.Board
import board.Location

/**
  * Created by Leonard on 6/3/2017.
  */
object Controller {
  val ANY_INPUT = 0

  def main(args: Array[String]): Unit = {
    //TODO make the project do something useful.
    val controller = new Controller
    controller.startGame
    controller.exitGame
  }
}

class Controller {
  private val model = Model.loadOrCreate(getProfileName)
  private val view = new PrintView(model)

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
    while(model.isBoardActive){
      doTurn
    }
  }

  def doTurn: Unit = {
    doPlayerTurn
    doEnemyTurn
  }

  def doPlayerTurn: Unit = {
    view.showBoard
    view.showPlayerParty
    var endTurn = false
    while(!endTurn) {
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
        case Some("a") => ???
        case _ => System.out.println("Unrecognized command")
      }
    }
  }

  def movePlayerFighter: Unit = {
    def getNumFromInput(in: Option[String]): Option[Int] = in match {
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
    System.out.println("Enter from row: ")
    val fromRow = getNumFromInput(this.waitForStringInput)
    System.out.println("Enter from col: ")
    val fromCol = getNumFromInput(this.waitForStringInput)
    System.out.println("Enter to row: ")
    val toRow = getNumFromInput(this.waitForStringInput)
    System.out.println("Enter to col: ")
    val toCol = getNumFromInput(this.waitForStringInput)
    if(fromRow.isEmpty || fromCol.isEmpty || toRow.isEmpty || toCol.isEmpty) return
    else if(model.canFighterMove(Location(fromRow.get, fromCol.get)))
      model.moveFighter(Location(fromRow.get, fromCol.get), Location(toRow.get, toCol.get))
  }

  def doEnemyTurn: Unit = {
    ???
  }

  def exitGame: Unit = {
    System.exit(0)
  }
}
