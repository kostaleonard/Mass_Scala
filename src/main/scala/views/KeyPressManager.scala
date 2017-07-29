package views

import java.awt.event.{ActionEvent, ActionListener, KeyEvent, KeyListener}
import javax.swing.Timer

import controller.Controller

/**
  * Created by Leonard on 7/16/2017.
  */
class KeyPressManager(controller: Controller) extends KeyListener{
  protected val keyCodeMap: scala.collection.mutable.Map[Int, Boolean] = scala.collection.mutable.Map.empty[Int, Boolean]
  protected var lastHeldKeys: scala.collection.immutable.Set[Int] = scala.collection.immutable.Set.empty[Int]

  override def keyPressed(e: KeyEvent): Unit = {
    keyCodeMap += e.getKeyCode -> true
    controller.keyPressed(e.getKeyCode)
    //viewManager.getCurrentView.keyPressed(e.getKeyCode)
  }

  override def keyReleased(e: KeyEvent): Unit = {
    keyCodeMap += e.getKeyCode -> false
    controller.keyReleased(e.getKeyCode)
    //viewManager.getCurrentView.keyReleased(e.getKeyCode)
  }

  override def keyTyped(e: KeyEvent): Unit = {
    controller.keyTyped(e.getKeyCode)
    //viewManager.getCurrentView.keyTyped(e.getKeyCode)
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
