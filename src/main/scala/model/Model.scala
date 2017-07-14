package model

import java.io._

import actions.Action
import board.{Board, Location}
import fighter.{Fighter, Party}

/**
  * Created by Leonard on 6/3/2017.
  */
object Model {
  val RESOURCE_ROOT_DIRECTORY = "C:/Users/Leonard/IdeaProjects/Mass_Scala/resources"
  val PROFILE_DIRECTORY = "profiles"
  val MODEL_SERIAL_VERSION_UID = 100L

  def getSourcePath(profileName: String): String = RESOURCE_ROOT_DIRECTORY + "/" + PROFILE_DIRECTORY + "/" + profileName + ".pro"

  def loadOrCreate(profileName: String): Model = {
    if(profileExists(profileName)) load(profileName) else create(profileName)
  }

  def profileExists(profileName: String): Boolean = {
    val sourcePath = getSourcePath(profileName)
    new File(sourcePath).exists
  }

  def load(profileName: String): Model = {
    val sourcePath = getSourcePath(profileName)
    val ois = new ObjectInputStream(new FileInputStream(sourcePath))
    val model = ois.readObject.asInstanceOf[Model]
    ois.close
    model
  }

  def create(profileName: String): Model = {
    val model = new Model(profileName)
    model.playerParty = Party.random(4)
    model
  }
}

@SerialVersionUID(Model.MODEL_SERIAL_VERSION_UID)
class Model(profileName: String) extends Serializable {
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

  def setCurrentBoard(board: Option[Board]): Unit = currentBoard = board

  def getCurrentBoard: Option[Board] = currentBoard

  def getPlayerParty: Party = playerParty

  def moveFighter(from: Location, to: Location): Unit = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.fighterAt(from) match{
      case Some(fighter) => currentBoard.get.moveFighterTo(fighter, to)
      case None => throw new UnsupportedOperationException("There is no fighter at the requested location: " + from.toString)
    }
  }

  def doFighterAction(action: Action): Unit = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.doFighterAction(action)
  }

  def fighterAt(loc: Location): Option[Fighter] = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.fighterAt(loc)
  }

  def getAvailableMoves(fighter: Fighter): scala.collection.immutable.Set[Location] = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.availableMoveLocations(fighter)
  }

  def getAvailableActions(fighter: Fighter): scala.collection.immutable.Set[Action] = {
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot get Fighter on empty board")
    currentBoard.get.availableActions(fighter)
  }

  def recoverMovesAndActions: Unit = {
    //Allow Fighters to recover moves and actions so that they can use them next turn.
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot recover moves/actions on an empty board")
    playerParty.recoverMovesAndActions
    currentBoard.get.getEnemyParty.recoverMovesAndActions
  }

  def doTurnlyActions: Unit = {
    //Allow Fighters to recover HP/shields, EEZO, and do any turnly effects.
    if(currentBoard.isEmpty) throw new UnsupportedOperationException("Cannot do turnly actions on an empty board")
    playerParty.doTurnlyActions
    currentBoard.get.getEnemyParty.doTurnlyActions
  }

  def loadBoard(boardName: String): Board = Board.load(boardName)

  def save: Unit = {
    val destinationPath = Model.getSourcePath(profileName)
    val oos = new ObjectOutputStream(new FileOutputStream(destinationPath))
    oos.writeObject(this)
    oos.close
  }
}
