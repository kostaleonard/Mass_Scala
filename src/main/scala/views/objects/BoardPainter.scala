package views.objects

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage

import board._
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
  protected var cursorLocOption: Option[Location] = None

  def getCursorLocOption: Option[Location] = cursorLocOption

  def setCursorLocOption(opt: Option[Location]): Unit = cursorLocOption = opt

  def getImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    //Draw board:
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
    //Draw fighters:
    board.getPlayerParty.getFighters.foreach{fighter =>
      val fighterAvatar = new FighterAvatar(fighter)
      val fighterImage = fighterAvatar.getFriendlyImage
      g2d.drawImage(fighterImage,
        fighter.getLocation.col * tileSize, fighter.getLocation.row * tileSize,
        tileSize, tileSize, null)
    }
    board.getEnemyParty.getFighters.foreach{fighter =>
      val fighterAvatar = new FighterAvatar(fighter)
      val fighterImage = fighterAvatar.getEnemyImage
      g2d.drawImage(fighterImage,
        fighter.getLocation.col * tileSize, fighter.getLocation.row * tileSize,
        tileSize, tileSize, null)
    }
    //Draw cursor:
    cursorLocOption.map{ loc =>
      val cursorPainter = new CursorPainter
      g2d.drawImage(cursorPainter.getImage, loc.col * tileSize, loc.row * tileSize, tileSize, tileSize, null)
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
