package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class AvengerAssaultRifle extends AssaultRifle {
  baseDamage = 20
  minRange = 1
  maxRange = 5
  magazineSize = 3

  override def toString: String = "M8 Avenger"
}
