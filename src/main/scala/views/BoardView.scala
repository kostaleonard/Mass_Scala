package views

import java.awt.{Color, Font, Graphics2D}
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import controller.KeyMappings
import model.Model
import views.objects.BoardPainter

/**
  * Created by Leonard on 7/29/2017.
  */
class BoardView(model: Model) extends View(model) {
  protected val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  protected val backgroundImage = ImageIO.read(new File(View.getSourcePath("boardBackground.jpg")))
    .getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  protected val boardPainter =
    new BoardPainter(model.getCurrentBoard.getOrElse(throw new UnsupportedOperationException("Cannot draw empty board.")))
  protected var boardOffset_X = 100
  protected var boardOffset_Y = 50

  override def getImage: BufferedImage = getBoardImage

  def getBoardImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.drawImage(backgroundImage, 0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, null)
    val boardImage = boardPainter.getImage
    g2d.drawImage(boardImage, boardOffset_X, boardOffset_Y, boardImage.getWidth, boardImage.getHeight, null)
    g2d.dispose()
    bufferedImage
  }

  override def keyPressed(keyCode: Int): Unit = {}

  override def keyReleased(keyCode: Int): Unit = {}

  override def keyHeld(keyCode: Int): Unit = {}
}
