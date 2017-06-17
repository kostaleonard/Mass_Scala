package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class MantisSniperRifle extends SniperRifle {
  baseDamage = 70
  minRange = 3
  maxRange = 10
  usesUntilReloadMax = 1
  usesUntilReloadCurrent = usesUntilReloadMax

  override def toString: String = "M22 Mantis"
}
