package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class CrusaderShotgun extends Shotgun {
  baseDamage = getDefaultWeaponDamage
  minRange = 1
  maxRange = 2
  usesUntilReloadMax = 2
  usesUntilReloadCurrent = usesUntilReloadMax

  override def toString: String = "M5 Crusader"

  override def getDefaultWeaponDamage: Int = 40
}
