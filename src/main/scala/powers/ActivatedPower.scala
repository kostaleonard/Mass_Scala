package powers

import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class ActivatedPower extends Power {
  protected var damage = 0

  def usePower(attacker: Fighter, target: Fighter): Unit = ??? //TODO default implementation of usePower

  override def isPassive: Boolean = false
  override def isActivated: Boolean = true
  override def isSustained: Boolean = false
}
