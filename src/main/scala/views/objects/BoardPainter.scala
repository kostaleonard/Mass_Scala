package views.objects

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

import board.{Board, GrassPlains, Mountains, Tile}
import views.View

/**
  * Created by Leonard on 7/30/2017.
  */
object BoardPainter {
  val MAX_TILE_SIZE = 100
  val DEFAULT_TILE_SIZE = 75
  val MIN_TILE_SIZE = 10
}
class BoardPainter(board: Board) {
  protected var tileSize = BoardPainter.DEFAULT_TILE_SIZE
  protected var bufferedImage = new BufferedImage(
    tileSize * board.getTiles(0).length,
    tileSize * board.getTiles.length,
    BufferedImage.TYPE_INT_RGB)

  def getImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    board.getTiles.indices.foreach{ r =>
      board.getTiles(r).indices.foreach{ c =>
        val tile = board.getTiles(r)(c)
        tile match{
          case grass: GrassPlains => g2d.setColor(Color.GREEN)
          case mountinas: Mountains => g2d.setColor(Color.ORANGE.darker())
          case _ => g2d.setColor(Color.BLACK)
        }
        g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize)
      }
    }
    g2d.dispose()
    bufferedImage
  }

  def setTileSize(newSize: Int): Unit = {
    tileSize = (newSize min BoardPainter.MAX_TILE_SIZE) max BoardPainter.MIN_TILE_SIZE
    bufferedImage = new BufferedImage(
      tileSize * board.getTiles(0).length,
      tileSize * board.getTiles.length,
      BufferedImage.TYPE_INT_RGB)
  }

  def getTileImage(tile: Tile): BufferedImage = {
    val tileImage = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB)
    ???
  }
}
