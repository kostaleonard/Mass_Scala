package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class OmniBlade extends MeleeWeapon {
  baseDamage = getDefaultWeaponDamage

  override def toString: String = "OmniBlade"

  override def getDefaultWeaponDamage: Int = 100 //TODO decrease Omni-Blade damage
}
