package armor

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class HeavyArmor extends Armor {
  override def isLightArmor: Boolean = false
  override def isHeavyArmor: Boolean = true
}
