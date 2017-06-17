package weapons

import board.Board
import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
object Weapon {
  //Theoretical value for the maximum base damage achievable
  val THEORETICAL_MAX_BASE_DAMAGE = 100
}
abstract class Weapon {
  protected var baseDamage: Int = 0
  protected var minRange: Int = 0
  protected var maxRange: Int = 0
  protected var accuracy: Float = 1.0f

  def getMinRange: Int = minRange

  def getMaxRange: Int = maxRange

  protected def accuracyCheck(attacker: Fighter, target: Fighter): Boolean = {
    //Returns true if the attack hit, based on accuracy.
    //Bonuses/Effects also accounted for here.
    //Subclasses may override.
    val check = math.random
    //TODO extra accuracy check logic goes here
    check < accuracy
  }

  def doAttack(attacker: Fighter, target: Fighter, board: Board): Unit = {
    //Default implementation may be overriden by subclasses for further functionality.
    //For example, subclasses may add effects to the attack, either to self or enemy.
    val distance = attacker.crowFliesDistance(target)
    if(distance < minRange || distance > maxRange) throw new UnsupportedOperationException("The target is out of range.")
    if(accuracyCheck(attacker, target)) {
      target.takeDamage(this.getAttackDamage(attacker, target, board))
    }
  }

  def getAttackDamage(attacker: Fighter, target: Fighter, board: Board): Int = {
    //Default implementation may be overriden by subclasses for further functionality.
    //Should take into account the bonuses/powers/skillclass/race of self and enemy.
    //Should also do appropriate damage based on whether or not shields are active.
    //Should also take into account armor rating.
    //If the target is not wearing any armor, this attack should do the base damage (plus Bonuses).
    //TODO update weapon attack damage as necessary
    var rollingDamageCalculation = 0

    //Armor
    target.getArmor match {
      case Some(armor) => rollingDamageCalculation = (baseDamage * (1.0f - armor.armorProtectionModifier)).toInt
      case None => rollingDamageCalculation = baseDamage
    }

    //Tile modifiers
    rollingDamageCalculation =
      (rollingDamageCalculation * board.getTiles(attacker.getLocation.row)(attacker.getLocation.col).getAttackModifier).toInt
    rollingDamageCalculation =
      (rollingDamageCalculation / board.getTiles(target.getLocation.row)(target.getLocation.col).getDefenseModifier).toInt

    //Other bonuses
    //TODO update weapon attack damage with bonuses

    rollingDamageCalculation
  }

  //Abstract methods
  override def toString: String
  def isMelee: Boolean
  def isGrenade: Boolean
  def isGun: Boolean
}
