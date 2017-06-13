package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Gun extends Weapon {
  //Shots before reloading
  protected var magazineSize: Int = 1

  override def isMelee: Boolean = false
  override def isGrenade: Boolean = false
  override def isGun: Boolean = true

  //Abstract methods
  def isPistol: Boolean
  def isAssaultRifle: Boolean
  def isSniperRifle: Boolean
  def isShotgun: Boolean
}
