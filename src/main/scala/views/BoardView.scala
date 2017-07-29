package views

import java.awt.image.BufferedImage

import model.Model

/**
  * Created by Leonard on 7/29/2017.
  */
class BoardView(model: Model) extends View(model) {
  override def getImage: BufferedImage = getTestImage2
}
