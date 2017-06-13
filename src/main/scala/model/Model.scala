package model

import board.{Board, Location}
import fighter.Party

/**
  * Created by Leonard on 6/3/2017.
  */
object Model {
  def loadOrCreate(profileName: String): Model = {
    if(profileExists(profileName)) load(profileName) else create(profileName)
  }

  def profileExists(profileName: String): Boolean = {
    false //TODO check if profile exists in the saved profiles
  }

  def load(profileName: String): Model = {
    ??? //TODO load profile
  }

  def create(profileName: String): Model = {
    val model = new Model(profileName)
    model.playerParty = Party.random(4)
    model
  }
}
class Model(profileName: String) {
  private val this.profileName = profileName
  private var playerParty = Party.empty
  private var currentBoard = None: Option[Board]

  def isBoardActive: Boolean = {
    //Returns true if there are enemy fighters on the board
    currentBoard match{
      case Some(board) => board.isActive
      case None => false
    }
  }

  def setCurrentBoard(board: Option[Board]): Unit = {
    currentBoard = board
  }

  def getCurrentBoard: Option[Board] = currentBoard

  def getPlayerParty: Party = playerParty

  def canFighterMove(loc: Location): Boolean = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.fighterAt(loc) match{
      case Some(fighter) => fighter.canMove
      case None => throw new UnsupportedOperationException("There is no fighter at the requested location")
    }
  }

  def moveFighter(from: Location, to: Location): Unit = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.fighterAt(from) match{
      case Some(fighter) => fighter.moveTo(to)
      case None => throw new UnsupportedOperationException("There is no fighter at the requested location")
    }
  }

  def copyBoard(boardName: String): Unit = {
    //TODO Load a default board and store it in this object.
  }

  def save: Unit = {
    //TODO Save the model.Model to ???
  }
}
