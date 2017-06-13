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

  private def setPlayerParty(party: Party): Unit = playerParty = party

  private def setEnemyParty(party: Party): Unit = enemyParty = party

  private def placeFighterOnBoard(fighter: Fighter): Unit = {
    if(fighter.getLocation.inBounds(tiles)){
      tiles(fighter.getLocation.row)(fighter.getLocation.col).getFighter match {
        case Some(otherFighter) => throw new UnsupportedOperationException("There is already a Fighter on that tile")
        case None => tiles(fighter.getLocation.row)(fighter.getLocation.col).setFighter(Some(fighter))
      }
    }
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
}
