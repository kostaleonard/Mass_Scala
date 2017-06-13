package armor

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class LightArmor extends Armor {
  override def isHeavyArmor: Boolean = false
  override def isLightArmor: Boolean = true
}
