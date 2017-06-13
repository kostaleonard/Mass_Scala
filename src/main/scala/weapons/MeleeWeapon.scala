package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class MeleeWeapon extends Weapon {
  //Sub-classes could still override if they wanted...
  minRange = 1
  maxRange = 1

  override def isMelee: Boolean = true
  override def isGrenade: Boolean = false
  override def isGun: Boolean = false
}
