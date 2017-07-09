package powers
import fighter.Fighter
import interfaces.Freezer

/**
  * Created by Leonard on 6/4/2017.
  */
class CryoAmmo extends AmmoPower with Freezer {
  //Power
  eezoRechargePenalty = 2
  //AmmoPower
  damageBonus = 1
  //Freezer
  chillChance = 0.1f
  armorRatingPenalty = 20
  movementPenalty = 1
  enemyEezoRecoveryPenalty = 3
  freezeDuration = 3

  override def toString: String = "Cryo Ammo"

  override def getDescription: String = "Slow down and freeze enemies while crippling armor"

  override def choiceName(choice: Int): String = choice match {
    case Power.LVL_1 => "Cryo Ammo"
    case Power.LVL_2 => "Slow"
    case Power.LVL_3 => "Anti-Armor"
    case Power.LVL_4A => "Max Eezo Penalty"
    case Power.LVL_4B => "Eezo Recharge Penalty"
    case Power.LVL_5A => ???
    case Power.LVL_5B => ???
    case Power.LVL_6A => ???
    case Power.LVL_6B => ???
    case _ => ???
  }

  override def choiceDescription(choice: Int): String = choice match {
    case Power.LVL_1 => "Burn unshielded opponents; weaken armor rating"
    case Power.LVL_2 => "Eezo cost: -30%"
    case Power.LVL_3 => "Inferno damage: +30%"
    case Power.LVL_4A => "Deal full damage to shielded opponents"
    case Power.LVL_4B => "Blast radius: +1"
    case Power.LVL_5A => "Burn damage: 10% power damage per turn for 4 turns"
    case Power.LVL_5B => "Eezo cost: -40%"
    case Power.LVL_6A => "Damage to frozen/chilled targets: +100%"
    case Power.LVL_6B => "Armor rating penalty: +50%"
    case _ => ???
  }

  override def addChoiceEffect(choice: Int): Unit = choice match {
    case Power.LVL_1 => ; //unlocks power
    case Power.LVL_2 => ???
    case Power.LVL_3 => ???
    case Power.LVL_4A => ???
    case Power.LVL_4B => ???
    case Power.LVL_5A => ???
    case Power.LVL_5B => ???
    case Power.LVL_6A => ???
    case Power.LVL_6B => ???
    case _ => ???
  }

  override def usePower(attacker: Fighter): Unit = {
    //Other effects (actual damage changes/chill) will be handled by Weapon.
    super.usePower(attacker)
    //TODO add more logic here for using the CryoAmmo Power
  }

  override def discontinuePower(attacker: Fighter): Unit = {
    super.discontinuePower(attacker)
    //TODO add more logic here for using the CryoAmmo Power
  }
}
