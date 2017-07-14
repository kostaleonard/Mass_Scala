package weapons

import interfaces.{Damager, Ranged}

/**
  * Created by Leonard on 6/4/2017.
  */
object Weapon {
  //Theoretical value for the maximum base damage achievable
  val THEORETICAL_MAX_BASE_DAMAGE = 100
}
abstract class Weapon extends Damager with Ranged {
  //Abstract methods
  override def toString: String
  def isMelee: Boolean
  def isGrenade: Boolean
  def isGun: Boolean
  def getDefaultWeaponDamage: Int
}
