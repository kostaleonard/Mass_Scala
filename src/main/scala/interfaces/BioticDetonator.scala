package interfaces

import fighter.Fighter
import powers.TargetedActivatedPower

/**
  * Created by Leonard on 6/22/2017.
  */
trait BioticDetonator {
  protected var bioticDetonatorPowerMultiplier = 2.0f
  protected var bioticDetonatorPower: TargetedActivatedPower

  protected def damageByCombinedPowerLevel(level: Int): Int = (level * bioticDetonatorPowerMultiplier).toInt

  def getBioticDetonationDamage(target: Fighter): Int = {
    //Returns the damage that will result from the bioticDetonatorPower and the highest level initiator.
    val maxInitiatorLevel = target.getActiveBioticInitiators.maxBy(_.initiatorPower.getLevel).initiatorPower.getLevel
    val comboLevel = maxInitiatorLevel + bioticDetonatorPower.getLevel
    damageByCombinedPowerLevel(comboLevel)
  }

  def comboCheck(target: Fighter): Boolean = {
    //Returns true if this target will detonate if the bioticDetonatorPower is used on it.
    target.getActiveBioticInitiators.exists(!_.initiatorPower.isSamePower(bioticDetonatorPower))
  }
}
