package views

import java.awt.event.KeyEvent
import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import model.Model

/**
  * Created by Leonard on 7/16/2017.
  */
class MainMenuView(model: Model) extends View(model) {
  def getMainMenuImage: BufferedImage = {
    val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(Color.GRAY)
    g2d.fillRect(0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT)
    g2d.setColor(Color.GREEN)
    g2d.fillRect(100, 100, 200, 200)
    g2d.setColor(Color.CYAN)
    g2d.fillOval(1400, 600, 200, 300)
    g2d.setColor(Color.RED.darker)
    g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 60))
    g2d.drawString("MAIN MENU", 600, 300)
    bufferedImage
  }

  override def keyPressed(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " pressed")
  }

  override def keyReleased(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " released")
  }

  override def keyHeld(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " held")
  }
}
