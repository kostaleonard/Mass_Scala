package powers
import fighter.Fighter
import interfaces.Freezer

/**
  * Created by Leonard on 6/4/2017.
  */
class CryoAmmo extends AmmoPower with Freezer {
  //Power
  eezoRechargePenalty = 2
  //Damager
  baseDamage = 2
  accuracy = 1.0f //This should never change, because the ammo power will always hit
  damageMultiplierToShields = 1.0f
  damageMultiplierToHp = 1.0f
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
    case Power.LVL_4A => "Chill Chance"
    case Power.LVL_4B => "Chill Duration"
    case Power.LVL_5A => "Chill Chance"
    case Power.LVL_5B => "Gun Damage Bonus"
    case Power.LVL_6A => "Warhead"
    case Power.LVL_6B => "Frostbite"
    case _ => ???
  }

  override def choiceDescription(choice: Int): String = choice match {
    case Power.LVL_1 => "Increases gun damage; chilled enemies take armor, movement, and eezo recharge penalty"
    case Power.LVL_2 => "Movement penalty: +1"
    case Power.LVL_3 => "Armor rating penalty: +25%"
    case Power.LVL_4A => "Chill chance: +25%"
    case Power.LVL_4B => "Chill duration: +2"
    case Power.LVL_5A => "Chill chance: +50%"
    case Power.LVL_5B => "Gun damage bonus: +100%"
    case Power.LVL_6A => "Armor rating penalty: +60%"
    case Power.LVL_6B => "Damage vs. unshielded enemies: +100%"
    case _ => ???
  }

  override def addChoiceEffect(choice: Int): Unit = choice match {
    case Power.LVL_1 => ; //unlocks power
    case Power.LVL_2 => movementPenalty += 1
    case Power.LVL_3 => armorRatingPenalty = (armorRatingPenalty * 1.25f).toInt
    case Power.LVL_4A => chillChance *= 1.25f
    case Power.LVL_4B => freezeDuration += 2
    case Power.LVL_5A => chillChance *= 1.5f
    case Power.LVL_5B => baseDamage *= 2
    case Power.LVL_6A => armorRatingPenalty = (armorRatingPenalty * 1.6f).toInt
    case Power.LVL_6B => damageMultiplierToHp *= 2
    case _ => ???
  }
}
