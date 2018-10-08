package views

import java.awt.image.BufferedImage

import controller.Controller

/**
  * Created by Leonard on 7/16/2017.
  */
//TODO investigate Java Swing performace improvement strategies. Animations are flickering.
//TODO maybe just use default keyboard input behavior?
object ViewManager{
  val UNSUPPORTED_VIEW_OPERATION_EXCEPTION_MESSAGE = "The current View is not designed to render this screen; you must switch the current View."
  val DEFAULT_FRAME_WIDTH = 800
  val DEFAULT_FRAME_HEIGHT = 450
  val FRAMES_PER_SECOND = 60
  val KEY_EVENTS_PER_SECOND = 100
  val HELD_KEY_EVENTS_PER_KEY_ACTION = 10
  val MILLISECONDS_PER_SECOND = 1000
}
class ViewManager(initialView: View, controller: Controller){
  protected var currentView: View = initialView
  //protected var lastDrawFunction: ()=>Unit = {()=>;}
  protected val frame = new ViewFrame(this, controller)
  setCurrentView(initialView)
  setupFrame

  def getCurrentView: View = currentView

  def setCurrentView(newView: View): Unit = {
    currentView.setKeyPressManager(None)
    currentView = newView
    currentView.setKeyPressManager(Some(frame.getKeyPressManager))
  }

  //Gets a NEW image from the currentView.
  //Frame's panel will then repaint (only if it has to due to resizing) the LAST GIVEN image at the correct scale.
  def repaint: Unit = currentView match{
    case printView: PrintView => ;
    case _ =>
      //lastDrawFunction()
      showView
  }

  protected def renderImage(bufferedImage: BufferedImage): Unit = frame.renderImage(bufferedImage)

  protected def setupFrame: Unit = currentView match{
    case printView: PrintView => ;
    case _ => frame.setup
  }

  def showView: Unit = currentView match{
    case printView: PrintView => printView.showMainMenu
    case _ => renderImage(currentView.getImage)
  }

  /*
  def showMainMenu: Unit = currentView match{
    case mainMenuView: MainMenuView =>
      lastDrawFunction = ()=>showMainMenu
      renderImage(mainMenuView.getMainMenuImage)
    case printView: PrintView => printView.showMainMenu
    case _ => throw new UnsupportedViewOperationException
  }
  def showStartScreen: Unit = ???
  def showBoard: Unit = ???
  def showPlayerParty: Unit = ???
  def showBoardCommands: Unit = ???
  def showAvailableMoves(moves: Set[Location]): Unit = ???
  def showAvailableActions(fighter: Fighter, actions: Iterable[Action]): Unit = ???
  def showChosenAction(fighter: Fighter, action: Action): Unit = ???
  */
}

case class UnsupportedViewOperationException(private val message: String = ViewManager.UNSUPPORTED_VIEW_OPERATION_EXCEPTION_MESSAGE, private val cause: Throwable = None.orNull)
  extends Exception(message, cause)