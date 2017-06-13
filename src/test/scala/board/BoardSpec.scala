package board

import org.scalatest._
import fighter.Fighter

/**
  * Created by Leonard on 6/13/2017.
  */
class BoardSpec extends FlatSpec with Matchers {

  "A Board" should "be able to update its Tiles." in {
    val board = new Board
    val defaultRows = 10
    val defaultCols = 15
    val tiles = Array.fill[Array[Tile]](defaultRows)(Array.fill[Tile](defaultCols)(new GrassPlains))
    board.setTiles(tiles)
    board.getTiles.length should be (defaultRows)
    board.getTiles(0).length should be (defaultCols)
  }

  "A Board" should "be able to load a default Board from the default save files." in {
    val board = new Board

  }

  "A Board" should "be able to add Fighters to the Tiles." in {
    val board = new Board

  }
}