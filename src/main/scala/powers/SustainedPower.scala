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
    if(isInUse) throw new UnsupportedOperationException("Cannot reuse a SustainedPower when it is already in use.")
    val e0 = getEezoCost
    if(!attacker.canUseEezo(e0)) throw new UnsupportedOperationException("Fighter cannot use " + e0 + " Eezo.")
    inUse = true
    attacker.loseEezo(e0)
    attacker.takeEezoRechargePenalty(getEezoRechargePenalty)
  }

  def discontinuePower(attacker: Fighter): Unit = {
    //Subclasses MUST override
    if(!isInUse) throw new UnsupportedOperationException("Cannot discontinue a SustainedPower when it is not in use.")
    inUse = false
    attacker.removeEezoRechargePenalty(getEezoRechargePenalty)
  }
}

