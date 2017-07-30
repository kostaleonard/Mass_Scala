package views

import java.awt.event._
import java.awt.image.BufferedImage
import javax.swing.{JFrame, Timer, WindowConstants}

import controller.Controller

/**
  * Created by Leonard on 7/17/2017.
  */
class ViewFrame(viewManager: ViewManager, controller: Controller) extends JFrame with WindowListener with ComponentListener {
  protected val keyPressManager = new KeyPressManager(controller)
  protected val mainPanel = new ImageRenderPanel
  protected val repaintTimer = new Timer(ViewManager.MILLISECONDS_PER_SECOND/ViewManager.FRAMES_PER_SECOND, new RepaintListener)
  protected val keyHeldTimer = new Timer(ViewManager.MILLISECONDS_PER_SECOND/ViewManager.HELD_KEY_EVENTS_PER_SECOND, new KeyHeldListener)
  setVisible(false)

  def getKeyPressManager: KeyPressManager = keyPressManager

  def setup: Unit = {
    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
    addWindowListener(this)
    addComponentListener(this)
    setSize(ViewManager.DEFAULT_FRAME_WIDTH, ViewManager.DEFAULT_FRAME_HEIGHT) //In case the panel is restored.
    //TODO maximize the frame by default once testing is completed.
    //setExtendedState(java.awt.Frame.MAXIMIZED_BOTH)
    mainPanel.setSize(getWidth, getHeight)
    //TODO frame decoration is throwing off graphics calculations slightly. Give user these buttons organically, then uncomment below.
    //setUndecorated(true)
    setVisible(true)
    setFocusable(true)
    add(mainPanel)
    addKeyListener(keyPressManager)
    setupRepaintTimer
    setupKeyHeldTimer
  }

  def setupRepaintTimer: Unit = repaintTimer.start

  def setupKeyHeldTimer: Unit = keyHeldTimer.start

  def renderImage(bufferedImage: BufferedImage): Unit = {
    mainPanel.setCurrentImage(Some(bufferedImage))
    mainPanel.repaint()
  }

  override def windowClosed(e: WindowEvent): Unit = {
    repaintTimer.stop
    keyHeldTimer.stop
    System.exit(0)
  }

  override def windowActivated(e: WindowEvent): Unit = {}

  override def windowIconified(e: WindowEvent): Unit = {}

  override def windowClosing(e: WindowEvent): Unit = {
    repaintTimer.stop
    keyHeldTimer.stop
    System.exit(0)
  }

  override def windowDeactivated(e: WindowEvent): Unit = {}

  override def windowDeiconified(e: WindowEvent): Unit = {}

  override def windowOpened(e: WindowEvent): Unit = {}

  override def componentHidden(e: ComponentEvent): Unit = {}

  override def componentMoved(e: ComponentEvent): Unit = {}

  override def componentShown(e: ComponentEvent): Unit = {}

  override def componentResized(e: ComponentEvent): Unit = {
    mainPanel.setSize(getWidth, getHeight)
    mainPanel.repaint()
  }

  class RepaintListener extends ActionListener{
    override def actionPerformed(e: ActionEvent): Unit = {
      viewManager.repaint
    }
  }

  class KeyHeldListener extends ActionListener{
    override def actionPerformed(e: ActionEvent): Unit = {
      keyPressManager.checkForHeldKeys
    }
  }
}
