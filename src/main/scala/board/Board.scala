package board

import fighter.{Party, Fighter}

/**
  * Created by Leonard on 6/3/2017.
  */
object Board{
  def getTestBoard: Board = {
    //TODO get rid of getTestBoard function
    //Fill Tiles
    val defaultRows = 10
    val defaultCols = 10
    val defaultEnemyPartySize = 3
    val b = new Board
    b.tiles = Array.fill[Array[Tile]](defaultRows)(Array.fill[Tile](defaultCols)(if(Math.random() > 0.5) new GrassPlains else new Mountains))

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

  def loadDefaultBoardFromFile(filename: String): Board = {
    //TODO implement loading Boards.
    new Board
  }
}
class Board {
  private var playerParty = Party.empty
  private var enemyParty = Party.empty
  private var tiles = Array.empty[Array[Tile]]

  def setTiles(t: Array[Array[Tile]]): Unit = tiles = t

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
        throw new UnsupportedOperationException("The fighter on the tile is not the specified Fighter")
      tiles(oldLoc.row)(oldLoc.col).setFighter(None)
      tiles(newLoc.row)(newLoc.col).setFighter(Some(fighter))
      fighter.setLocation(newLoc)
    }
    else throw new ArrayIndexOutOfBoundsException("Cannot move Fighter to/from outside board boundaries")
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

  def isActive: Boolean = enemyParty.getFighters.nonEmpty

  def availableMoveLocations(fighter: Fighter): scala.collection.immutable.Set[Location] = {
    //Returns the Set of all available move locations for this fighter on this board.
    if(!fighter.canMove) return scala.collection.immutable.Set.empty[Location]
    var result = scala.collection.immutable.Set.empty[Location]
    def addAvailableMoveLocations(loc: Location, distance: Int): Unit = {
      if(distance >= 0 && loc.inBounds(tiles) && fighter.canCross(tiles(loc.row)(loc.col).getClass)){
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
}
