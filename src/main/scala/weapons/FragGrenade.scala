package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class FragGrenade extends Grenade {
  baseDamage = getDefaultWeaponDamage
  minRange = 0
  maxRange = 3
  blastRadius = 1
  ammunitionMax = 3
  ammunitionCurrent = ammunitionMax

  override def toString: String = "M18 Fragmentation Grenade"

  override def getDefaultWeaponDamage: Int = 50
}
