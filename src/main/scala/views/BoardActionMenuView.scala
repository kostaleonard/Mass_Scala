package views

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import controller.{EndTurn, KeyMappings, SwitchViews}
import model.Model
import views.gui._

/**
  * Created by Leonard on 10/7/2018.
  */
object BoardActionMenuView{
  val LEFT_SIDE_MENU_START_X = 50
  val RIGHT_SIDE_MENU_START_X = 1200
  val MENU_START_Y = 50
  val DEFAULT_ENTRY_WIDTH = 200
  val DEFAULT_ENTRY_HEIGHT = 100
  val END_TURN_FILENAME = "end_turn.png"
}
class BoardActionMenuView(model: Model, boardView: BoardView) extends View(model){
  protected val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  //protected val actionMenu = new BasicMenu
  protected val actionMenu = new ImageMenu
  protected val entryWidth = BoardActionMenuView.DEFAULT_ENTRY_WIDTH
  protected val entryHeight = BoardActionMenuView.DEFAULT_ENTRY_HEIGHT

  setupActionMenu

  override def getImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.drawImage(boardView.getImage, 0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, null)
    val actionMenuImage = actionMenu.getImage
    //TODO change menu location.
    if(boardView.getCursorDrawLoc.get.row > View.FRAME_DESIGN_WIDTH / 2)
      g2d.drawImage(actionMenuImage, BoardActionMenuView.LEFT_SIDE_MENU_START_X, BoardActionMenuView.MENU_START_Y, actionMenu.getWidth, actionMenu.getHeight, null)
    else
      g2d.drawImage(actionMenuImage, BoardActionMenuView.RIGHT_SIDE_MENU_START_X, BoardActionMenuView.MENU_START_Y, actionMenu.getWidth, actionMenu.getHeight, null)
    g2d.dispose()
    bufferedImage
  }

  def setupActionMenu: Unit = {
    actionMenu.setTitleString("ACTIONS")
    actionMenu.setWrapContentWidth(true)
    val actionsOnTargetLocation = boardView.getActionLocationMap.get(boardView.getCursorLoc)
    actionsOnTargetLocation match{
      case Some(actions) => actions.foreach { act =>
        val image = ImageIO.read(new File(View.getActionSourcePath(act)))
        actionMenu.appendMenuItem(new ImageItem(image, entryWidth, entryHeight, GuiAction{() =>
          act.doAction
          exitActionMenu
        }))
      }
      case None =>
        val image = ImageIO.read(new File(View.getSourcePath(BoardActionMenuView.END_TURN_FILENAME)))
        actionMenu.appendMenuItem(new ImageItem(image, entryWidth, entryHeight, GuiAction{() =>
          sendControllerMessage(EndTurn)
          exitActionMenu
        }))
    }

  }

  def exitActionMenu: Unit = {
    boardView.cursorDeselect
    sendControllerMessage(SwitchViews(boardView))
  }

  override def keyPressed(keyCode: Int): Unit = keyCode match{
    case KeyMappings.A_KEY => actionMenu.makeSelection
    case KeyMappings.B_KEY => exitActionMenu
    case KeyMappings.UP_KEY => actionMenu.scrollUp
    case KeyMappings.DOWN_KEY => actionMenu.scrollDown
    case _ => ;
  }

  override def keyReleased(keyCode: Int): Unit = {}

  override def keyHeld(keyCode: Int): Unit = {}
}
