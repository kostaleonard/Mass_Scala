package powers

import board.Board
import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class ActivatedPower extends Power {
  protected var damage = 0
  protected var minRange = 0
  protected var maxRange = 0

  def getMinRange: Int = minRange
  def getMaxRange: Int = maxRange

  def usePower(attacker: Fighter, targetOption: Option[Fighter], board: Board): Unit = {
    //Subclasses MUST override
    val e0 = getEezoCost
    if(!attacker.canUseEezo(e0)) throw new UnsupportedOperationException("Fighter cannot use " + e0 + " Eezo.")
    attacker.loseEezo(e0)
  }

  //Does this power need a target?
  def isTargeted: Boolean

  override def isPassive: Boolean = false
  override def isActivated: Boolean = true
  override def isSustained: Boolean = false
}
