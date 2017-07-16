package views

import java.awt.image.BufferedImage
import javax.swing.JFrame

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
  val DEFAULT_FRAME_WIDTH = 160 * 5
  val DEFAULT_FRAME_HEIGHT = 90 * 5
}
class ViewManager(initialView: View) {
  protected var currentView: View = initialView
  protected val frame = new JFrame
  protected val mainPanel = new ImageRenderPanel(frame)
  protected var lastDrawFunction: ()=>Unit = {()=>;}
  setupFrame

  def getCurrentView: View = currentView

  def setCurrentView(newView: View): Unit = currentView = newView

  def repaint: Unit = currentView match{
    case printView: PrintView => ;
    case _ => lastDrawFunction()
  }

  private def renderImage(bufferedImage: BufferedImage): Unit = {
    mainPanel.setCurrentImage(Some(bufferedImage))
    mainPanel.repaint()
  }

  def setupFrame: Unit = currentView match{
    case printView: PrintView => frame.setVisible(false)
    case _ =>
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      frame.setSize(ViewManager.DEFAULT_FRAME_WIDTH, ViewManager.DEFAULT_FRAME_HEIGHT) //In case the panel is restored.
      frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
      //TODO frame decoration is throwing off graphics calculations slightly. Give user these buttons organically, then uncomment below.
      //frame.setUndecorated(true)
      frame.setVisible(true)
      frame.add(mainPanel)
  }

  def showMainMenu: Unit = currentView match{
    case mainMenuView: MainMenuView =>
      lastDrawFunction = ()=>showMainMenu
      renderImage(mainMenuView.getMainMenuImage)
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