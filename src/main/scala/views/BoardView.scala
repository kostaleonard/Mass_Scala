package views

import java.awt.event.KeyEvent
import java.awt.image.BufferedImage

import model.Model

/**
  * Created by Leonard on 7/29/2017.
  */
class BoardView(model: Model) extends View(model) {
  override def getImage: BufferedImage = getTestImage2

  override def keyPressed(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " pressed")
  }

  override def keyReleased(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " released")
  }

  override def keyHeld(keyCode: Int): Unit = {
    val text = KeyEvent.getKeyText(keyCode)
    println(text + " held")
  }
}
