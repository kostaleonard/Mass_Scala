package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Gun extends Weapon {
  //Shots before reloading
  protected var usesUntilReloadMax: Int = 0
  protected var usesUntilReloadCurrent: Int = usesUntilReloadMax

  def getUsesUntilReloadCurrent: Int = usesUntilReloadCurrent

  def getUsesUntilReloadMax: Int = usesUntilReloadMax

  def setUsesUntilReloadCurrent(turns: Int): Unit = usesUntilReloadCurrent = turns

  def setUsesUntilReloadMax(turns: Int): Unit = usesUntilReloadMax = turns

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
