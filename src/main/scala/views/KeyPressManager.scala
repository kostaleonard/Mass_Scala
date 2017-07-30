package views

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import javax.swing.Timer

import controller.{Controller, KeyMappings}

/**
  * Created by Leonard on 7/16/2017.
  */
class KeyPressManager(controller: Controller) extends KeyListener{
  protected val keyCodeMap: scala.collection.mutable.Map[Int, Boolean] = scala.collection.mutable.Map.empty[Int, Boolean]
  protected var lastHeldKeys: scala.collection.immutable.Set[Int] = scala.collection.immutable.Set.empty[Int]
  protected val keyMappings = new KeyMappings

  def translateKeyCode(keyCode: Int): Int = keyMappings.getKeyMapping(keyCode)

  override def keyPressed(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    keyCodeMap += keyCode -> true
    controller.keyPressed(keyCode)
    //viewManager.getCurrentView.keyPressed(keyCode)
  }

  override def keyReleased(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    keyCodeMap += keyCode -> false
    controller.keyReleased(keyCode)
    //viewManager.getCurrentView.keyReleased(keyCode)
  }

  override def keyTyped(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    controller.keyTyped(keyCode)
    //viewManager.getCurrentView.keyTyped(keyCode)
  }

  def isPressed(keyCode: Int): Boolean = keyCodeMap.getOrElse(keyCode, false)

  def checkForHeldKeys: Unit = {
    val nextHeldKeys: scala.collection.immutable.Set[Int] =
      keyCodeMap.keySet.filter(k => keyCodeMap(k)).asInstanceOf[scala.collection.immutable.Set[Int]]
    (nextHeldKeys union lastHeldKeys).foreach(k => controller.keyHeld(k))
    //(nextHeldKeys union lastHeldKeys).foreach(k => viewManager.getCurrentView.keyHeld(k))
    lastHeldKeys = nextHeldKeys
  }
}
