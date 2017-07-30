package controller

import java.awt.event.KeyEvent

/**
  * Created by Leonard on 7/30/2017.
  */
object KeyMappings {
  val NO_MAPPING = -1
  val A_KEY = 0
  val B_KEY = 1
  val L_TRIGGER_KEY = 2
  val R_TRIGGER_KEY = 3
  val START_KEY = 4
  val SELECT_KEY = 5
  val UP_KEY = 6
  val DOWN_KEY = 7
  val LEFT_KEY = 8
  val RIGHT_KEY = 9

  val DEFAULT_KEYBOARD_MAPPINGS = scala.collection.mutable.Map(
    KeyEvent.VK_Z -> A_KEY,
    KeyEvent.VK_X -> B_KEY,
    KeyEvent.VK_A -> L_TRIGGER_KEY,
    KeyEvent.VK_S -> R_TRIGGER_KEY,
    KeyEvent.VK_ENTER -> START_KEY,
    KeyEvent.VK_BACK_SPACE -> SELECT_KEY,
    KeyEvent.VK_UP -> UP_KEY,
    KeyEvent.VK_DOWN -> DOWN_KEY,
    KeyEvent.VK_LEFT -> LEFT_KEY,
    KeyEvent.VK_RIGHT -> RIGHT_KEY
  )
}
class KeyMappings {
  protected val keyMappings: scala.collection.mutable.Map[Int, Int] = KeyMappings.DEFAULT_KEYBOARD_MAPPINGS

  def getKeyMapping(keyCode: Int): Int = keyMappings.getOrElse(keyCode, KeyMappings.NO_MAPPING)

  protected def addKeyMapping(keyCode: Int, mapping: Int): Unit = keyMappings += (keyCode -> mapping)

  protected def removeKeyMapping(keyCode: Int): Unit = keyMappings.remove(keyCode)

  def changeKeyMapping(keyCode: Int, newMapping: Int): Unit = {
    keyMappings.keySet
      .filter(currentKeyCode => newMapping == keyMappings(currentKeyCode))
      .foreach(currentKeyCode => removeKeyMapping(currentKeyCode))
    addKeyMapping(keyCode, newMapping)
  }
}
