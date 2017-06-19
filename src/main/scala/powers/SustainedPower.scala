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

  def usePower(attacker: Fighter): Unit = {
    //Subclasses MUST override
    val e0 = getEezoCost
    if(attacker.canUseEezo(e0)) throw new UnsupportedOperationException("Fighter cannot use that much Eezo.")
    attacker.loseEezo(e0)
    attacker.takeEezoRechargePenalty(getEezoRechargePenalty)
  }

  def discontinuePower(attacker: Fighter): Unit = {
    //Subclasses MUST override
    attacker.removeEezoRechargePenalty(getEezoRechargePenalty)
  }
}

