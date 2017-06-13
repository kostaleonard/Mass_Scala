package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
class FragGrenade extends Grenade {
  baseDamage = 50
  minRange = 0
  maxRange = 3
  blastRadius = 1

  override def toString: String = "M18 Fragmentation weapons.Grenade"
}
