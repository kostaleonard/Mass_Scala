package weapons

import fighter.Fighter

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Weapon {
  protected var baseDamage: Int = 0
  protected var minRange: Int = 0
  protected var maxRange: Int = 0

  def doAttack(attacker: Fighter, target: Fighter): Unit = {
    //Default implementation may be overriden by subclasses for further functionality.
    //For example, subclasses may add effects to the attack, either to self or enemy.
    target.takeDamage(this.getAttackDamage(attacker, target))
  }

  def getAttackDamage(attacker: Fighter, target: Fighter): Int = {
    //Default implementation may be overriden by subclasses for further functionality.
    //Should take into account the bonuses/powers/skillclass/race of self and enemy.
    //Should also do appropriate damage based on whether or not shields are active.
    //If shields are not active, should also take into account armor rating.
    //TODO getAttackDamage
    1
  }

  //Abstract methods
  override def toString: String
  def isMelee: Boolean
  def isGrenade: Boolean
  def isGun: Boolean
}
