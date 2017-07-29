package views

import java.awt.event.KeyEvent
import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import model.Model
import model.Model.RESOURCE_ROOT_DIRECTORY

/**
  * Created by Leonard on 6/3/2017.
  *
  * Any View and View subclasses should implement methods that return BufferedImage objects.
  * The ViewManager will be responsible for displaying these in a JFrame.
  *
  * NOTE: Use a 1600x900 (16:9) ratio for graphics.
  * Then use the following to adjust for screen size:
  *
  * https://gamedev.stackexchange.com/questions/83583/how-to-resize-my-2d-game-to-fit-the-screen
  * This resource states that you can render any graphics to a BufferedImage object,
  * which can be resized by calling the 10 argument drawImage method (which allows for scaling).
  *
  * https://gamedev.stackexchange.com/questions/45498/making-a-2d-game-with-responsive-resolution?rq=1
  * This resource gives a nice mathematical description of how to resize while preserving the aspect ratio.
  * So, rather than just resizing to fit the computer screen both vertically and horizontally,
  * you can preserve the 16:9 ratio. This will, however, create unused space in the background.
  */
object View{
  //Preserve a 16:9 aspect ratio in graphics.
  val FRAME_DESIGN_WIDTH = 1600
  val FRAME_DESIGN_HEIGHT = 900
  val IMAGES_DIRECTORY = "images"

  def getSourcePath(imageFilename: String): String = RESOURCE_ROOT_DIRECTORY + "/" + IMAGES_DIRECTORY + "/" + imageFilename
}
abstract class View(model: Model) {
  protected var keyPressManager: Option[KeyPressManager] = None
  protected var nextView: Option[View] = None

  def setKeyPressManager(opt: Option[KeyPressManager]): Unit = keyPressManager = opt

  def getNextView = nextView

  protected def getTestImage1: BufferedImage = {
    ImageIO.read(new File(View.getSourcePath("testImage.jpg")))
  }

  protected def getTestImage2: BufferedImage = {
    val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(Color.GRAY)
    g2d.fillRect(0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT)
    g2d.setColor(Color.GREEN)
    g2d.fillRect(100, 100, 200, 200)
    g2d.setColor(Color.CYAN)
    g2d.fillOval(1400, 600, 200, 300)
    g2d.setColor(Color.RED.darker)
    g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 60))
    g2d.drawString("TEST IMAGE", 600, 300)
    g2d.dispose()
    bufferedImage
  }

  //Key methods:
  def keyPressed(keyCode: Int): Unit = {}
  def keyReleased(keyCode: Int): Unit = {}
  def keyTyped(keyCode: Int): Unit = {}
  def keyHeld(keyCode: Int): Unit = {}

  //Abstract methods:
  def getImage: BufferedImage
}
