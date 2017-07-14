package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class PredatorPistol extends Pistol {
  baseDamage = getDefaultWeaponDamage
  minRange = 1
  maxRange = 3
  usesUntilReloadMax = 5
  usesUntilReloadCurrent = usesUntilReloadMax

  override def toString: String = "M9 Predator"

  override def getDefaultWeaponDamage: Int = 10
}
