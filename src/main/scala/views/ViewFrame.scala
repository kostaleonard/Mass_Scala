package views

import java.awt.event.{ActionEvent, ActionListener, WindowEvent, WindowListener}
import javax.swing.{JFrame, Timer}

/**
  * Created by Leonard on 7/17/2017.
  */
class ViewFrame(viewManager: ViewManager, keyPressManager: KeyPressManager) extends JFrame with WindowListener {
  val milisecondsPerSecond = 1000
  val repaintTimer = new Timer(milisecondsPerSecond/ViewManager.FRAMES_PER_SECOND, new RepaintListener)
  val keyHeldTimer = new Timer(milisecondsPerSecond/ViewManager.FRAMES_PER_SECOND, new KeyHeldListener)

  def setupRepaintTimer: Unit = {
    repaintTimer.start
  }

  def setupKeyHeldTimer: Unit = {
    keyHeldTimer.start
  }

  override def windowClosed(e: WindowEvent): Unit = {
    repaintTimer.stop
    keyHeldTimer.stop
  }

  override def windowActivated(e: WindowEvent): Unit = {}

  override def windowIconified(e: WindowEvent): Unit = {}

  override def windowClosing(e: WindowEvent): Unit = {}

  override def windowDeactivated(e: WindowEvent): Unit = {}

  override def windowDeiconified(e: WindowEvent): Unit = {}

  override def windowOpened(e: WindowEvent): Unit = {}

  class RepaintListener extends ActionListener{
    override def actionPerformed(e: ActionEvent): Unit = viewManager.repaint
  }

  class KeyHeldListener extends ActionListener{
    override def actionPerformed(e: ActionEvent): Unit = keyPressManager.checkForHeldKeys
  }
}
