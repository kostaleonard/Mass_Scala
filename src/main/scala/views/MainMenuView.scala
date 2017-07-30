package views

import java.awt.event.KeyEvent
import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import controller.Controller
import model.Model
import views.gui.{BasicMenu, GuiAction, MenuItem}

/**
  * Created by Leonard on 7/16/2017.
  */
class MainMenuView(model: Model) extends View(model) {
  protected val mainMenu = new BasicMenu
  protected val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  protected val backgroundImage = ImageIO.read(new File(View.getSourcePath("titleScreen.jpg")))
    .getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  setupMainMenu

  override def getImage: BufferedImage = getMainMenuImage

  def getMainMenuImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.drawImage(backgroundImage, 0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, null)
    val mainMenuImage = mainMenu.getImage
    g2d.drawImage(mainMenuImage, 1250, 500, mainMenu.getWidth, mainMenu.getHeight, null)
    g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 80))
    g2d.setColor(Color.WHITE.darker())
    g2d.drawString(Controller.GAME_TITLE, 50, 120)
    g2d.dispose()
    bufferedImage
  }

  def setupMainMenu: Unit = {
    mainMenu.setTitleString("MAIN MENU")
    mainMenu.appendMenuItem(MenuItem("CAMPAIGN", GuiAction(() => nextView = Some(new BoardView(model)))))
    mainMenu.appendMenuItem(MenuItem("MULTIPLAYER", GuiAction()))
    mainMenu.appendMenuItem(MenuItem("INVASION", GuiAction(), false))
    mainMenu.appendMenuItem(MenuItem("SETTINGS", GuiAction()))
    mainMenu.appendMenuItem(MenuItem("CHANGE PROFILE", GuiAction()))
    mainMenu.appendMenuItem(MenuItem("EXIT GAME", GuiAction(), false))
  }

  override def keyPressed(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " pressed")
    if(text.equals("A")) mainMenu.makeSelection
    else if(text.equals("Up")) mainMenu.scrollUp
    else if(text.equals("Down")) mainMenu.scrollDown
  }

  override def keyReleased(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " released")
  }

  override def keyHeld(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " held")
    if(text.equals("A")) mainMenu.makeSelection
    else if(text.equals("Up")) mainMenu.scrollUp
    else if(text.equals("Down")) mainMenu.scrollDown
  }
}
