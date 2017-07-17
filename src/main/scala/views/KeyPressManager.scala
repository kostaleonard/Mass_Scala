package views

import java.awt.event.{KeyEvent, KeyListener}

/**
  * Created by Leonard on 7/16/2017.
  *
  * This is a really lazy class. Just hands all the work over to the View...
  */
class KeyPressManager(viewManager: ViewManager) extends KeyListener {
  override def keyPressed(e: KeyEvent): Unit = viewManager.getCurrentView.keyPressed(e)

  override def keyReleased(e: KeyEvent): Unit = viewManager.getCurrentView.keyReleased(e)

  override def keyTyped(e: KeyEvent): Unit = viewManager.getCurrentView.keyTyped(e)
}
