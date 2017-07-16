package views

import java.awt.Image
import java.awt.image.BufferedImage
import javax.swing.{ImageIcon, JFrame, JLabel}

import actions.Action
import board.Location
import fighter.Fighter

/**
  * Created by Leonard on 7/16/2017.
  *
  * ViewManager should be the sole owner of any JFrames.
  * This will keep transitions between Views smooth.
  */
object ViewManager{
  val UNSUPPORTED_VIEW_OPERATION_EXCEPTION_MESSAGE = "The current View is not designed to render this screen; you must switch the current View."
}
class ViewManager(initialView: View) {
  var currentView: View = initialView
  val frame = new JFrame
  setupFrame

  def getCurrentView: View = currentView

  def setCurrentView(newView: View): Unit = currentView = newView

  private def renderImage(bufferedImage: BufferedImage): Unit = {
    //frame.add(new JLabel(new ImageIcon(t.getImage())));
    frame.removeAll
    frame.add(new JLabel(new ImageIcon(bufferedImage.getScaledInstance(100, 100, Image.SCALE_DEFAULT))))
  }

  def setupFrame: Unit = currentView match{
    case printView: PrintView => frame.setVisible(false)
    case _ =>
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
      frame.setVisible(true)
  }

  def showMainMenu: Unit = currentView match{
    case mainMenuView: MainMenuView => renderImage(mainMenuView.getMainMenuImage)
    case printView: PrintView => ???
    case _ => throw new UnsupportedViewOperationException
  }

  def showStartScreen: Unit = ???
  def showBoard: Unit = ???
  def showPlayerParty: Unit = ???
  def showBoardCommands: Unit = ???
  def showAvailableMoves(moves: Set[Location]): Unit = ???
  def showAvailableActions(fighter: Fighter, actions: Iterable[Action]): Unit = ???
  def showChosenAction(fighter: Fighter, action: Action): Unit = ???
}

case class UnsupportedViewOperationException(private val message: String = ViewManager.UNSUPPORTED_VIEW_OPERATION_EXCEPTION_MESSAGE, private val cause: Throwable = None.orNull)
  extends Exception(message, cause)