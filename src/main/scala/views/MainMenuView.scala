package views

import java.awt.event.KeyEvent
import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import model.Model

/**
  * Created by Leonard on 7/16/2017.
  */
class MainMenuView(model: Model) extends View(model) {
  override def getImage: BufferedImage = getMainMenuImage

  def getMainMenuImage: BufferedImage = {
    val backgroundImage = ImageIO.read(new File(View.getSourcePath("titleScreen.jpg")))
      .getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
    val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.drawImage(backgroundImage, 0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, null)
    g2d.setColor(new Color(100, 100, 100, 200))
    g2d.fillRect(1280, 570, 280, 280)
    g2d.setColor(Color.WHITE)
    g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40))
    g2d.drawString("MAIN MENU", 1300, 600)
    g2d.dispose()
    bufferedImage
  }

  override def keyPressed(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " pressed")
    if(text.equals("A")) nextView = Some(new BoardView(model))
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
