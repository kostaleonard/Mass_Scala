package powers
import board.Board
import interfaces.{AreaOfEffect, Burner, Damager, Ranged}
import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
class Inferno extends TargetedActivatedPower with Damager with Ranged with Burner with AreaOfEffect {
  //Power
  eezoCost = 1
  //Damager
  baseDamage = 40
  damageMultiplierToShields = 0.5f
  //Ranged
  minRange = 1
  maxRange = 5
  //Burner
  burnChance = 1.0f
  armorRatingPenalty = 50
  burnDamagePerTurn = 2
  burnDuration = 4
  frozenTargetDamageBonus = 1.0f
  //AreaOfEffect
  blastRadius = 0

  override def usePower(attacker: Fighter, targetOption: Option[Fighter], board: Board): Unit = {
    super.usePower(attacker, targetOption, board)
    doAttack(attacker, targetOption.get, board)
  }

  override def toString: String = "Inferno"

  override def getDescription: String = "Burn opponents and incinerate armor."

  override def choiceName(choice: Int): String = choice match {
    case Power.LVL_1 => "Inferno"
    case Power.LVL_2 => "Eezo Cost"
    case Power.LVL_3 => "Damage"
    case Power.LVL_4A => "Plasma Infusion"
    case Power.LVL_4B => "Blast Radius"
    case Power.LVL_5A => "Immolation"
    case Power.LVL_5B => "Eezo Cost"
    case Power.LVL_6A => "Freeze Combo"
    case Power.LVL_6B => "Anti-Armor"
    case _ => ???
  }

  override def choiceDescription(choice: Int): String = choice match {
    case Power.LVL_1 => "Burn opponents; weaken armor rating"
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
    case Power.LVL_2 => eezoCost = (eezoCost * 0.7f).toInt
    case Power.LVL_3 => baseDamage = (baseDamage * 1.3f).toInt
    case Power.LVL_4A => damageMultiplierToShields = 1.0f
    case Power.LVL_4B => blastRadius += 1
    case Power.LVL_5A => burnDamagePerTurn = (baseDamage * 0.1f).toInt
    case Power.LVL_5B => eezoCost = (eezoCost * 0.4f).toInt
    case Power.LVL_6A => frozenTargetDamageBonus *= 2
    case Power.LVL_6B => armorRatingPenalty = (armorRatingPenalty * 1.5f).toInt
    case _ => ???
  }
}
