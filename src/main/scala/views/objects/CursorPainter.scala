package views.objects

import java.awt.{Color, Graphics2D, Image}
import java.awt.image.BufferedImage
import java.awt.AlphaComposite
import java.io.File
import javax.imageio.ImageIO

import views.View

/**
  * Created by Leonard on 11/16/2017.
  */
object CursorPainter {
  val DEFAULT_HEIGHT = 150
  val DEFAULT_WIDTH = 150
  val DEFAULT_COLOR = Color.ORANGE
  val DEFAULT_OPACITY = 0.2
  val DEFAULT_CURSOR_FILENAME = View.getSourcePath("cursor.png")
}
class CursorPainter {
  new File(CursorPainter.DEFAULT_CURSOR_FILENAME).delete()
  protected var height = CursorPainter.DEFAULT_HEIGHT
  protected var width = CursorPainter.DEFAULT_WIDTH
  protected var color = CursorPainter.DEFAULT_COLOR
  protected var opacity = CursorPainter.DEFAULT_OPACITY
  protected var bufferedImage = new BufferedImage(
    getWidth,
    getHeight,
    BufferedImage.TYPE_INT_ARGB
  )

  def getHeight: Int = height

  def getWidth: Int = width

  def getColor: Color = color

  def getOpacity: Double = opacity

  def setHeight(h: Int): Unit = height = h

  def setWidth(w: Int): Unit = width = w

  def setColor(c: Color): Unit = color = c

  def setOpacity(o: Double): Unit = opacity = o

  /** Writes the BufferedImage as a png to the resources/images directory so that it can be used later. This gives us
    * the ability to make transparent backgrounds. */
  def createImage: Unit = {
    ImageIO.write(getPaintedImage, "png", new File(CursorPainter.DEFAULT_CURSOR_FILENAME))
  }

  def getPaintedImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.setComposite(AlphaComposite.Clear)
    g2d.fillRect(0, 0, width, height)
    g2d.setComposite(AlphaComposite.SrcOver)
    g2d.setColor(color)
    //Top Rectangle:
    g2d.fillRect(0, 0, width, (height * opacity).toInt)
    //Bottom Rectangle:
    g2d.fillRect(0, (height * (1 - opacity)).toInt, width, (height * opacity).round.toInt)
    //Left Rectangle:
    g2d.fillRect(0, 0, (width * opacity).toInt, height)
    //Right Rectangle:
    g2d.fillRect((width * (1 - opacity)).toInt, 0, (width * opacity).round.toInt, height)
    g2d.dispose()
    bufferedImage
  }

  def getImage: Image = {
    val cursorFile = new File(CursorPainter.DEFAULT_CURSOR_FILENAME)
    if(cursorFile.exists()) ImageIO.read(cursorFile)//.getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
    else{
      createImage
      getImage
    }
  }
}