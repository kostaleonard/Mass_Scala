package powers

import board.Board
import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class ActivatedPower extends Power {
  def usePower(attacker: Fighter, targetOption: Option[Fighter], board: Board): Unit = {
    //Subclasses MUST override
    val e0 = getEezoCost
    if(!attacker.canUseEezo(e0)) throw new UnsupportedOperationException("Fighter cannot use " + e0 + " Eezo.")
    attacker.loseEezo(e0)
  }

  override def isPassive: Boolean = false
  override def isActivated: Boolean = true
  override def isSustained: Boolean = false

  //Abstract methods:
  def isTargeted: Boolean
}
