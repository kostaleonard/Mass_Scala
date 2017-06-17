package powers

import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class SustainedPower extends Power {
  protected var eezoRechargePenalty = 0
  protected var inUse: Boolean = false

  def isInUse: Boolean = inUse

  override def isPassive: Boolean = false
  override def isActivated: Boolean = false
  override def isSustained: Boolean = true

  def getEezoRechargePenalty: Int = eezoRechargePenalty

  //Abstract methods
  def usePower(attacker: Fighter): Unit
  def discontinuePower(attacker: Fighter): Unit
}

