package views

import java.awt.event.KeyEvent
import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import board.{Board, Location}
import controller.{Controller, KeyMappings}
import model.Model
import views.gui.{BasicMenu, BasicMenuItem, GuiAction, MenuItem}

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
    mainMenu.appendMenuItem(BasicMenuItem("CAMPAIGN", GuiAction{() =>
      //TODO do not use test board
      model.setCurrentBoard(Some(Board.getTestBoard))
      val fighterArray = model.getPlayerParty.getFighters.toArray
      fighterArray.indices.foreach { i =>
        fighterArray(i).setLocation(Location(i, i))
      }
      model.getCurrentBoard.get.placePlayerPartyOnBoard(model.getPlayerParty)
      setNextView(Some(new BoardView(model)))}))
    mainMenu.appendMenuItem(BasicMenuItem("MULTIPLAYER", GuiAction()))
    mainMenu.appendMenuItem(BasicMenuItem("INVASION", GuiAction(), false))
    mainMenu.appendMenuItem(BasicMenuItem("SETTINGS", GuiAction()))
    mainMenu.appendMenuItem(BasicMenuItem("CHANGE PROFILE", GuiAction()))
    mainMenu.appendMenuItem(BasicMenuItem("EXIT GAME", GuiAction(), false))
  }

  override def keyPressed(keyCode: Int): Unit = keyCode match{
    case KeyMappings.A_KEY => mainMenu.makeSelection
    case KeyMappings.UP_KEY => mainMenu.scrollUp
    case KeyMappings.DOWN_KEY => mainMenu.scrollDown
    case _ => ;
  }

  override def keyReleased(keyCode: Int): Unit = {}

  override def keyHeld(keyCode: Int): Unit = keyCode match {
    case KeyMappings.UP_KEY => mainMenu.scrollUp
    case KeyMappings.DOWN_KEY => mainMenu.scrollDown
    case _ => ;
  }
}
