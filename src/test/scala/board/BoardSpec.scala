package board

import org.scalatest._
import fighter.Fighter

/**
  * Created by Leonard on 6/13/2017.
  */
class BoardSpec extends FlatSpec with Matchers {

  def blankBoardFixture = new{
    val board = new Board
    val defaultRows = 10
    val defaultCols = 15
    val tiles = Array.fill[Array[Tile]](defaultRows)(Array.fill[Tile](defaultCols)(new GrassPlains))
    board.setTiles(tiles)
  }

  def twoFightersFixture = new{
    val board = new Board
    val defaultRows = 10
    val defaultCols = 15
    val tiles = Array.fill[Array[Tile]](defaultRows)(Array.fill[Tile](defaultCols)(new GrassPlains))
    board.setTiles(tiles)
    val f1 = Fighter.random(1)
    val loc1 = Location(0, 1)
    val f2 = Fighter.random(2)
    val loc2 = Location(1, 0)
    f1.setLocation(loc1)
    f2.setLocation(loc2)
    board.placeFighterOnBoard(f1)
    board.placeFighterOnBoard(f2)
  }

  "A Board" should "be able to update its Tiles." in {
    blankBoardFixture.board.getTiles.length should be (blankBoardFixture.defaultRows)
    blankBoardFixture.board.getTiles(0).length should be (blankBoardFixture.defaultCols)
  }

  it should "be able to load a default Board from the default save files." in {
    //TODO test default board loading
  }

  it should "be able to add Fighters to the Tiles." in {
    val board = blankBoardFixture.board
    val f1 = Fighter.random(1)
    val loc1 = Location(0, 1)
    val f2 = Fighter.random(2)
    val loc2 = Location(1, 0)
    f1.setLocation(loc1)
    f2.setLocation(loc2)

    board.fighterAt(loc1) should be (None)
    board.fighterAt(loc2) should be (None)

    board.placeFighterOnBoard(f1)
    board.placeFighterOnBoard(f2)

    board.fighterAt(loc1) should be (Some(f1))
    board.fighterAt(loc2) should be (Some(f2))
  }

  it should "be able to keep track of moving Fighters." in {
    val fix = twoFightersFixture
    val board = fix.board
    val dest1 = Location(2, 0)
    val dest2 = Location(0, 2)
    val loc1 = fix.loc1
    val loc2 = fix.loc2
    val f1 = fix.f1
    val f2 = fix.f2
    board.fighterAt(loc1) should be (Some(f1))
    board.fighterAt(loc2) should be (Some(f2))
    board.fighterAt(dest1) should be (None)
    board.fighterAt(dest2) should be (None)

    board.moveFighterTo(f1, dest1)
    board.moveFighterTo(f2, dest2)
    board.fighterAt(loc1) should be (None)
    board.fighterAt(loc2) should be (None)
    board.fighterAt(dest1) should be (Some(f1))
    board.fighterAt(dest2) should be (Some(f2))
  }

  it should "throw an exception if a Fighter is placed on an occupied Tile." in {
    val board = twoFightersFixture.board
    val alreadyUsedLoc = twoFightersFixture.loc1
    val f3 = Fighter.random(3)
    f3.setLocation(alreadyUsedLoc)
    a [UnsupportedOperationException] should be thrownBy {
      board.placeFighterOnBoard(f3)
    }
  }

  it should "throw an exception if a Fighter is placed outside Board bounds." in {
    val board = blankBoardFixture.board
    var outsideBounds = Location(-1, 0)
    val f1 = Fighter.random(1)
    f1.setLocation(outsideBounds)
    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      board.placeFighterOnBoard(f1)
    }
    outsideBounds = Location(board.getTiles.length, 0)
    a [ArrayIndexOutOfBoundsException] should be thrownBy {
      board.placeFighterOnBoard(f1)
    }
  }
}