package board

import java.io.{FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

import actions._
import fighter.{Fighter, Party}
import interfaces.Ranged
import model.Model
import powers._
import weapons.{Grenade, Gun, MeleeWeapon}

/**
  * Created by Leonard on 6/3/2017.
  */
object Board{
  val BOARD_DIRECTORY = "boards"
  val BOARD_SERIAL_UID = 101L

  def getSourcePath(boardName: String): String = Model.RESOURCE_ROOT_DIRECTORY + "/" + BOARD_DIRECTORY + "/" + boardName + ".brd"

  def getTestBoard: Board = {
    //TODO get rid of getTestBoard function
    //Fill Tiles
    val defaultRows = 10
    val defaultCols = 10
    val defaultEnemyPartySize = 3
    val b = new Board("testBoard")
    b.tiles = Array.fill[Array[Tile]](defaultRows)(Array.fill[Tile](defaultCols)(if(Math.random > 0.5) new GrassPlains else new Mountains))

    //Add enemies, making sure that they have unique locations
    var enemyParty = Party.random(defaultEnemyPartySize)
    var locsSet = scala.collection.immutable.Set.empty[Location]
    do {
      enemyParty.getFighters.foreach { f =>
        val randomRow = (math.random * defaultRows).toInt
        val randomCol = (math.random * defaultCols).toInt
        f.setLocation(Location(randomRow, randomCol))
      }
      locsSet = scala.collection.immutable.Set.empty[Location]
      enemyParty.getFighters.foreach(f => locsSet += f.getLocation)
    } while(locsSet.size != enemyParty.getFighters.size)

    b.placeEnemyPartyOnBoard(enemyParty)

    b
  }

  def load(boardName: String): Board = {
    val sourcePath = getSourcePath(boardName)
    val ois = new ObjectInputStream(new FileInputStream(sourcePath))
    val board = ois.readObject.asInstanceOf[Board]
    ois.close
    board
  }
}

@SerialVersionUID(Board.BOARD_SERIAL_UID)
class Board(name: String) extends Serializable {
  private var boardName = name
  private var playerParty = Party.empty
  private var enemyParty = Party.empty
  private var tiles = Array.empty[Array[Tile]]

  def getBoardName: String = boardName

  def setBoardName(newName: String): Unit = boardName = newName

  def setTiles(t: Array[Array[Tile]]): Unit = tiles = t

  def getPlayerParty: Party = playerParty

  def getEnemyParty: Party = enemyParty

  private def setPlayerParty(party: Party): Unit = playerParty = party

  private def setEnemyParty(party: Party): Unit = enemyParty = party

  def placeFighterOnBoard(fighter: Fighter): Unit = {
    if(fighter.getLocation.inBounds(tiles)){
      tiles(fighter.getLocation.row)(fighter.getLocation.col).getFighter match {
        case Some(otherFighter) => throw new UnsupportedOperationException("There is already a Fighter on that tile")
        case None => tiles(fighter.getLocation.row)(fighter.getLocation.col).setFighter(Some(fighter))
      }
    }
    else throw new ArrayIndexOutOfBoundsException("Fighter not placed within board boundaries")
  }

  def moveFighterTo(fighter: Fighter, newLoc: Location): Unit = {
    if(newLoc.inBounds(tiles) && fighter.getLocation.inBounds(tiles)){
      val oldLoc = fighter.getLocation
      //Check to make sure that the fighter on the tile is the one requested.
      if(!tiles(oldLoc.row)(oldLoc.col).getFighter.contains(fighter))
        throw new UnsupportedOperationException("The fighter on the tile is not the specified Fighter. Expected " + fighter.toString + " but found " + tiles(oldLoc.row)(oldLoc.col).getFighter)
      tiles(oldLoc.row)(oldLoc.col).setFighter(None)
      tiles(newLoc.row)(newLoc.col).setFighter(Some(fighter))
      fighter.moveTo(newLoc)
    }
    else throw new ArrayIndexOutOfBoundsException("Cannot move Fighter to/from outside board boundaries")
  }

  def doFighterAction(action: Action): Unit = {
    //Do the action and check for any changes to the board (i.e. dead people)
    action.doAction
    //Check for dead people
    (playerParty.getFighters ++ enemyParty.getFighters).filter(!_.isAlive).foreach{ deadFighter =>
      removeFighterFromBoard(deadFighter)
      removeFighterFromParty(deadFighter)
    }
  }

  def removeFighterFromBoard(fighter: Fighter): Unit = {
    if(fighter.getLocation.inBounds(tiles)) {
      val oldLoc = fighter.getLocation
      //Check to make sure that the fighter on the tile is the one requested.
      if (!tiles(oldLoc.row)(oldLoc.col).getFighter.contains(fighter))
        throw new UnsupportedOperationException("The fighter on the tile is not the specified Fighter")
      tiles(oldLoc.row)(oldLoc.col).setFighter(None)
    }
  }

  def removeFighterFromParty(fighter: Fighter): Unit = {
    //Party membership is mandatory!
    if(playerParty.getFighters(fighter)) playerParty.removeFighter(fighter)
    else if(enemyParty.getFighters(fighter)) enemyParty.removeFighter(fighter)
    else throw new UnsupportedOperationException("Cannot remove Fighter from the Party because the fighter does not belong to a Party.")
  }

  def placePlayerPartyOnBoard(party: Party): Unit = {
    setPlayerParty(party)
    party.getFighters.foreach(placeFighterOnBoard)
  }

  def placeEnemyPartyOnBoard(party: Party): Unit = {
    setEnemyParty(party)
    party.getFighters.foreach(placeFighterOnBoard)
  }

  private def getPartyLocationsMap(party: Party): scala.collection.immutable.Map[Location, Fighter] = {
    var locsMap = scala.collection.immutable.Map.empty[Location, Fighter]
    party.getFighters.foreach(f => locsMap += (f.getLocation -> f))
    locsMap
  }

  def getPlayerPartyLocationsMap: scala.collection.immutable.Map[Location, Fighter] = getPartyLocationsMap(playerParty)

  def getEnemyPartyLocationsMap: scala.collection.immutable.Map[Location, Fighter] = getPartyLocationsMap(enemyParty)

  def getAllFighterLocationsMap: scala.collection.immutable.Map[Location, Fighter] =
    getPlayerPartyLocationsMap ++ getEnemyPartyLocationsMap

  def fighterAt(loc: Location): Option[Fighter] = {
    tiles(loc.row)(loc.col).getFighter
  }

  def getTiles: Array[Array[Tile]] = tiles

  def isClearOfEnemies: Boolean = enemyParty.getFighters.isEmpty

  //TODO add extra logic to isActive so that the board can have various objectives (seize, clear, survive)
  def isActive: Boolean = playerParty.getFighters.nonEmpty && enemyParty.getFighters.nonEmpty

  def getWinner: Option[Party] = {
    //TODO add extra logic to the getWinner function once objectives are incorporated
    if(isActive) None
    else if(playerParty.getFighters.nonEmpty) Some(playerParty)
    else Some(enemyParty)
  }

  def availableMoveLocations(fighter: Fighter): scala.collection.immutable.Set[Location] = {
    //Returns the Set of all available move locations for this fighter on this board.
    if(!fighter.canMove) return scala.collection.immutable.Set.empty[Location]
    var result = scala.collection.immutable.Set.empty[Location]
    def addAvailableMoveLocations(loc: Location, distance: Int): Unit = {
      if(distance >= 0 &&
        loc.inBounds(tiles) &&
        fighter.canCross(tiles(loc.row)(loc.col).getClass) &&
        (tiles(loc.row)(loc.col).getFighter.isEmpty || tiles(loc.row)(loc.col).getFighter.get == fighter)){
        //If you can cross this tile, add it to the available move locations
        result += loc
        val movementCost = tiles(loc.row)(loc.col).getMovementCost
        //Also try surrounding locations
        addAvailableMoveLocations(Location(loc.row + 1, loc.col), distance - movementCost)
        addAvailableMoveLocations(Location(loc.row - 1, loc.col), distance - movementCost)
        addAvailableMoveLocations(Location(loc.row, loc.col + 1), distance - movementCost)
        addAvailableMoveLocations(Location(loc.row, loc.col - 1), distance - movementCost)
      }
    }
    addAvailableMoveLocations(fighter.getLocation, fighter.getMovementCurrent)
    //Remove the fighter's own location
    result - fighter.getLocation
  }

  def onOppositeTeams(f1: Fighter, f2: Fighter): Boolean = {
    //Returns true if f1 and f2 are in different parties.
    (playerParty.getFighters(f1) && enemyParty.getFighters(f2)) ||
      (playerParty.getFighters(f2) && enemyParty.getFighters(f1))
  }

  def availableActions(fighter: Fighter): scala.collection.immutable.Set[Action] = {
    if(!fighter.canAttack) return scala.collection.immutable.Set.empty[Action]
    var result = scala.collection.immutable.Set.empty[Action]
    val usableWeapons = fighter.getWeapons.filter(fighter.canUseWeapon)
    val usablePowers = fighter.getPowers.filter(fighter.canUsePower)
    //You can always wait!
    result += Wait(fighter)
    def addLocationIndependentPowerActions: Unit = {
      usablePowers.foreach(_ match {
        case pas: PassivePower => ;
        case sus: SustainedPower =>
          if(sus.isInUse) result += DiscontinueSustainedPower(sus, fighter)
          else result += UseSustainedPower(sus, fighter)
        case unt: UntargetedActivatedPower =>
          result += UseActivatedPower(unt, fighter, None, this)
        case tar: TargetedActivatedPower => ;
        case _ => throw new UnsupportedOperationException("Unrecognized Power.")
      })
    }
    def addWeaponReloadActions: Unit = {
      fighter.getWeapons.filter(_ match {
        case gun: Gun => gun.canReload
        case _ => false
      }).foreach(result += ReloadWeapon(_, fighter))
    }
    def addActionsOnTiles(loc: Location, distance: Int): Unit = {
      if(distance >= 0 && loc.inBounds(tiles)){
        //Add possible actions for this Tile
        val crowFliesDistance = fighter.getLocation.crowFliesDistance(loc)
        val targetOption = tiles(loc.row)(loc.col).getFighter
        if(targetOption.nonEmpty && onOppositeTeams(fighter, targetOption.get)) {
          //Weapon Actions:
          usableWeapons
            .filter(_.inRange(crowFliesDistance))
            .foreach(result += UseWeapon(_, fighter, targetOption.get, this))
          //Power Actions:
          usablePowers.foreach( p => p match{
            case act: TargetedActivatedPower => act match{
              case r: Ranged =>
                if(r.inRange(crowFliesDistance))
                  result += UseActivatedPower(act, fighter, targetOption, this)
              case _ => result += UseActivatedPower(act, fighter, targetOption, this)
            }
            case _ => ;
          })
        }
        //Check surrounding Tiles
        addActionsOnTiles(Location(loc.row + 1, loc.col), distance - 1)
        addActionsOnTiles(Location(loc.row - 1, loc.col), distance - 1)
        addActionsOnTiles(Location(loc.row, loc.col + 1), distance - 1)
        addActionsOnTiles(Location(loc.row, loc.col - 1), distance - 1)
      }
    }
    addLocationIndependentPowerActions
    addWeaponReloadActions
    val maxWeaponRange = (scala.collection.immutable.Set(0) ++ fighter.getWeapons.map(_.getMaxRange)).max
    val maxPowerRange = (scala.collection.immutable.Set(0) ++ fighter.getPowers.map(_ match {
      case r: Ranged => r.getMaxRange
      case _ => 0
    })).max
    val maxRange = maxWeaponRange max maxPowerRange
    addActionsOnTiles(fighter.getLocation, maxRange)
    result
  }

  def save: Unit = {
    val destinationPath = Board.getSourcePath(boardName)
    val oos = new ObjectOutputStream(new FileOutputStream(destinationPath))
    oos.writeObject(this)
    oos.close
  }
}
