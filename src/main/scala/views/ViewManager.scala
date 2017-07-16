package views

import javax.swing.JFrame

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

  def setupFrame: Unit = currentView match{
    case printView: PrintView => frame.setVisible(false)
    case _ =>
      frame.setVisible(true)
      frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
  }

  def showMainMenu: Unit = currentView match{
    case mainMenuView: MainMenuView => ???
    case printView: PrintView => ???
    case _ => throw new UnsupportedViewOperationException
  }
}

case class UnsupportedViewOperationException(private val message: String = ViewManager.UNSUPPORTED_VIEW_OPERATION_EXCEPTION_MESSAGE, private val cause: Throwable = None.orNull)
  extends Exception(message, cause)