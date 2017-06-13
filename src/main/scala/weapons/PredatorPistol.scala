package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class PredatorPistol extends Pistol {
  baseDamage = 10
  minRange = 1
  maxRange = 3
  magazineSize = 4

  override def toString: String = "M9 Predator"
}
