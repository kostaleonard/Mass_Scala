package views

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import javax.swing.Timer

import controller.{Controller, KeyMappings}

import scala.collection.mutable.ListBuffer

/**
  * Created by Leonard on 7/16/2017.
  */
//TODO make key input smoother
class KeyPressManager(controller: Controller) extends KeyListener{
  protected val keyCodeMap: scala.collection.mutable.Map[Int, ListBuffer[Boolean]] = scala.collection.mutable.Map.empty[Int, ListBuffer[Boolean]]
  protected val keyMappings = new KeyMappings

  def translateKeyCode(keyCode: Int): Int = keyMappings.getKeyMapping(keyCode)

  override def keyPressed(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    controller.keyPressed(keyCode)
  }

  override def keyReleased(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    controller.keyReleased(keyCode)
  }

  override def keyTyped(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    controller.keyTyped(keyCode)
  }
}
