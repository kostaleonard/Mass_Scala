package views

import java.awt.event.{KeyEvent, KeyListener}
import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage
import javax.swing.{JFrame, JPanel}

import scala.swing.{Frame, Graphics2D, Panel}


/**
  * Created by Leonard on 7/16/2017.
  */
class ImageRenderPanel(container: Frame, viewManager: ViewManager) extends Panel {
  protected var currentImage: Option[BufferedImage] = None
  protected var backgroundColor = Color.RED.darker //TODO change background color. It is dark red for testing.

  def getCurrentImage: Option[BufferedImage] = currentImage

  def setCurrentImage(opt: Option[BufferedImage]): Unit = currentImage = opt

  def renderImage(g: Graphics): Unit = {
    g.setColor(backgroundColor)
    g.fillRect(0, 0, container.size.width, container.size.height)
    //TODO this currently does not preserve the 16:9 aspect ratio. Consider changing.
    currentImage match{
      case Some(bufferedImage) => g.drawImage(bufferedImage,
        0, 0, container.size.width, container.size.height,
        0, 0, bufferedImage.getWidth, bufferedImage.getHeight,
        null)
      case None => ;
    }
  }

  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    renderImage(g)
  }

  //override def keyPressed(e: KeyEvent): Unit = viewManager.getCurrentView.keyPressed(e)

  //override def keyReleased(e: KeyEvent): Unit = viewManager.getCurrentView.keyReleased(e)

  //override def keyTyped(e: KeyEvent): Unit = viewManager.getCurrentView.keyTyped(e)
}
