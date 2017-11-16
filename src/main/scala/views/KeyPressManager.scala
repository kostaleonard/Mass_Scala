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
  protected val keysPressedOverLastInterval = scala.collection.mutable.Set.empty[Int]
  protected val keyMappings = new KeyMappings

  def translateKeyCode(keyCode: Int): Int = keyMappings.getKeyMapping(keyCode)

  override def keyPressed(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    keysPressedOverLastInterval += keyCode
    //viewManager.getCurrentView.keyPressed(keyCode)
  }

  override def keyReleased(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    keysPressedOverLastInterval -= keyCode
    //viewManager.getCurrentView.keyReleased(keyCode)
  }

  override def keyTyped(e: KeyEvent): Unit = {
    val keyCode = translateKeyCode(e.getKeyCode)
    keysPressedOverLastInterval += keyCode
    //viewManager.getCurrentView.keyTyped(keyCode)
  }

  def startNextKeyInterval: Unit = {
    (keyCodeMap.keySet union keysPressedOverLastInterval).foreach{ keyCode =>
      val newMapping = keysPressedOverLastInterval(keyCode) +: keyCodeMap
        .getOrElse(keyCode, ListBuffer.fill(ViewManager.HELD_KEY_EVENTS_PER_KEY_ACTION)(false))
        .dropRight(1)
      keyCodeMap += keyCode -> newMapping
    }

    keyCodeMap.keys.foreach{ keyCode =>
      keyCodeMap(keyCode) match {
        case events if events.forall(pressed => pressed) =>
          controller.keyHeld(keyCode)
          keyCodeMap += keyCode -> ListBuffer.fill(ViewManager.HELD_KEY_EVENTS_PER_KEY_ACTION)(false)
        case events if !events.head && events.tail.forall(pressed => pressed) =>
          controller.keyReleased(keyCode)
          keyCodeMap += keyCode -> ListBuffer.fill(ViewManager.HELD_KEY_EVENTS_PER_KEY_ACTION)(false)
        case false +: middle :+ false if middle.exists(pressed => pressed)  =>
          controller.keyPressed(keyCode)
          keyCodeMap += keyCode -> ListBuffer.fill(ViewManager.HELD_KEY_EVENTS_PER_KEY_ACTION)(false)
        case _ => ;
      }
    }
  }
}
