package powers

import fighter.Fighter
import interfaces.Damager

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class AmmoPower extends SustainedPower with Damager {
  //Subclasses may override
  eezoRechargePenalty = 5
  //Damager
  baseDamage = 0
  accuracy = 1.0f //This should never change, because the ammo power will always hit
  damageMultiplierToShields = 1.0f
  damageMultiplierToHp = 1.0f

  override def usePower(attacker: Fighter): Unit = {
    //Make sure to discontinue other ammo powers
    attacker.getSustainedPowersInUse.foreach{sus => sus match{
      case ammoPower: AmmoPower => ammoPower.discontinuePower(attacker)
    }}
    super.usePower(attacker)
    attacker.setActiveAmmoPower(Some(this))
  }

  override def discontinuePower(attacker: Fighter): Unit = {
    super.discontinuePower(attacker)
    attacker.setActiveAmmoPower(None)
  }
}
