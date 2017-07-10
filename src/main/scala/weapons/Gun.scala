package weapons
import board.Board
import fighter.Fighter
import powers.AmmoPower

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Gun extends Weapon {
  //Shots before reloading
  protected var usesUntilReloadMax: Int = 0
  protected var usesUntilReloadCurrent: Int = usesUntilReloadMax
  //Active AmmoPower
  protected var activeAmmoPower: Option[AmmoPower] = None

  override def doAttack(attacker: Fighter, target: Fighter, board: Board): Unit = {
    if(!isLoaded) throw new UnsupportedOperationException("Cannot fire an unloaded gun.")
    activeAmmoPower match {
      case None => super.doAttack(attacker, target, board)
      case Some(ammoPower) => doAttackWithAmmoPower(attacker, target, board)
    }
    usesUntilReloadCurrent -= 1
  }

  protected def doAttackWithAmmoPower(attacker: Fighter, target: Fighter, board: Board): Unit = {
    //Both this Gun and the activeAmmoPower are instances of Damager.
    //So, both must do their damage and effects.
    //We must do this carefully.
    if(accuracyCheck(attacker, target)) {
      val weaponDamage = this.getAttackDamage(attacker, target, board)
      val ammoDamage = activeAmmoPower.map(_.getAttackDamage(attacker, target, board))
      target.takeDamage(this.getAttackDamage(attacker, target, board))
      tryAddEffects(attacker, target, board)
      activeAmmoPower.map(_.tryAddEffects(attacker, target, board))
      tryAreaOfEffect(attacker, target, board)
      activeAmmoPower.map(_.tryAreaOfEffect(attacker, target, board))
      if(!target.isAlive) attacker.gainEXP(target.expGivenToVictoriousFighter)
    }
  }

  def getUsesUntilReloadCurrent: Int = usesUntilReloadCurrent

  def getUsesUntilReloadMax: Int = usesUntilReloadMax

  def setUsesUntilReloadCurrent(turns: Int): Unit = usesUntilReloadCurrent = turns

  def setUsesUntilReloadMax(turns: Int): Unit = usesUntilReloadMax = turns

  def getActiveAmmoPower: Option[AmmoPower] = activeAmmoPower

  def setActiveAmmoPower(opt: Option[AmmoPower]): Unit = activeAmmoPower = opt

  def isLoaded: Boolean = {
    //Returns true if this weapon has ammunition that can be used right now.
    usesUntilReloadCurrent > 0
  }

  def canReload: Boolean = {
    //Returns true if this weapon is not full on ammunition.
    usesUntilReloadCurrent < usesUntilReloadMax
  }

  def reload: Unit = setUsesUntilReloadCurrent(getUsesUntilReloadMax)

  override def isMelee: Boolean = false
  override def isGrenade: Boolean = false
  override def isGun: Boolean = true

  //Abstract methods
  def isPistol: Boolean
  def isAssaultRifle: Boolean
  def isSniperRifle: Boolean
  def isShotgun: Boolean
}
