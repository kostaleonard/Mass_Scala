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
  val DEFAULT_OPACTIY = 0.25
}
class CursorPainter {
  protected var height = CursorPainter.DEFAULT_HEIGHT
  protected var width = CursorPainter.DEFAULT_WIDTH
  protected var color = CursorPainter.DEFAULT_COLOR
  protected var opacity = CursorPainter.DEFAULT_OPACTIY
  protected var bufferedImage = new BufferedImage(
    getWidth,
    getHeight,
    BufferedImage.TYPE_INT_RGB)

  def getHeight: Int = height

  def getWidth: Int = width

  def getColor: Color = color

  def getOpacity: Double = opacity

  def setHeight(h: Int): Unit = height = h

  def setWidth(w: Int): Unit = width = w

  def setColor(c: Color): Unit = color = c

  def setOpacity(o: Double): Unit = opacity = o

  def getImage: Image = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(color)
    //Top Rectangle:
    g2d.fillRect(0, 0, width, (height * opacity).toInt)
    //Bottom Rectangle:
    g2d.fillRect((height * (1 - opacity)).toInt, 0, width, (height * opacity).toInt)
    //Left Rectangle:
    g2d.fillRect(0, 0, (width * opacity).toInt, height)
    //Right Rectangle:
    g2d.fillRect(0, (width * (1 - opacity)).toInt, (width * opacity).toInt, height)
    g2d.dispose()
    bufferedImage
  }
}