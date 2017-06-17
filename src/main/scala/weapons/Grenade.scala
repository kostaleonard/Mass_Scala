package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Grenade extends Weapon {
  protected var blastRadius = 1
  protected var ammunitionMax = 0
  protected var ammunitionCurrent = ammunitionMax

  def getAmmunitionMax: Int = ammunitionMax

  def getAmmunitionCurrent: Int = ammunitionCurrent

  def setAmmunitionMax(ammo: Int): Unit = ammunitionMax = ammo

  def setAmmunitionCurrent(ammo: Int): Unit = ammunitionCurrent = ammo

  override def isMelee: Boolean = false
  override def isGrenade: Boolean = true
  override def isGun: Boolean = false
}
