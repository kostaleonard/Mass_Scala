package views

import java.awt.{Color, Graphics}
import java.awt.image.BufferedImage
import javax.swing.{JFrame, JPanel}

/**
  * Created by Leonard on 7/16/2017.
  */
class ImageRenderPanel(container: JFrame) extends JPanel {
  protected val this.container: JFrame = container
  protected var currentImage: Option[BufferedImage] = None
  protected var backgroundColor = Color.RED.darker //TODO change background color. It is dark red for testing.

  def getCurrentImage: Option[BufferedImage] = currentImage

  def setCurrentImage(opt: Option[BufferedImage]): Unit = currentImage = opt

  def renderImage(g: Graphics): Unit = {
    g.setColor(backgroundColor)
    g.fillRect(0, 0, container.getWidth, container.getHeight)
    //TODO this currently does not preserve the 16:9 aspect ratio. Consider changing.
    currentImage match{
      case Some(bufferedImage) => g.drawImage(bufferedImage,
        0, 0, container.getWidth, container.getHeight,
        0, 0, bufferedImage.getWidth, bufferedImage.getHeight,
        null)
      case None => ;
    }
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    renderImage(g)
  }
}
