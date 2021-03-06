package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class AvengerAssaultRifle extends AssaultRifle {
  baseDamage = getDefaultWeaponDamage
  minRange = 1
  maxRange = 5
  usesUntilReloadMax = 4
  usesUntilReloadCurrent = usesUntilReloadMax

  override def toString: String = "M8 Avenger"

  override def getDefaultWeaponDamage: Int = 20
}
