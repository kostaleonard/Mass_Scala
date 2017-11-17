package views.objects

import java.awt.{Color, Graphics2D, Image}
import java.awt.image.BufferedImage

/**
  * Created by Leonard on 11/16/2017.
  */
object CursorPainter {
  val DEFAULT_HEIGHT = 150
  val DEFAULT_WIDTH = 150
  val DEFAULT_COLOR = Color.ORANGE
}
class CursorPainter {
  protected var height = CursorPainter.DEFAULT_HEIGHT
  protected var width = CursorPainter.DEFAULT_WIDTH
  protected var color = CursorPainter.DEFAULT_COLOR
  protected var bufferedImage = new BufferedImage(
    getWidth,
    getHeight,
    BufferedImage.TYPE_INT_RGB)

  def getHeight: Int = height

  def getWidth: Int = width

  def getColor: Color = color

  def setHeight(h: Int): Unit = height = h

  def setWidth(w: Int): Unit = width = w

  def setColor(c: Color): Unit = color = c

  def getImage: Image = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(color)
    g2d.fillRect(0, 0, width, height) //TODO make this less obstructive.
    g2d.dispose()
    bufferedImage
  }
}