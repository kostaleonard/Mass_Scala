package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class CrusaderShotgun extends Shotgun {
  baseDamage = 40
  minRange = 1
  maxRange = 2
  magazineSize = 2

  override def toString: String = "M5 Crusader"
}
