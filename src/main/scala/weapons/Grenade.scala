package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Grenade extends Weapon {
  protected var blastRadius = 1

  override def isMelee: Boolean = false
  override def isGrenade: Boolean = true
  override def isGun: Boolean = false
}
