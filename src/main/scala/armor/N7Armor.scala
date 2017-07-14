package armor

/**
  * Created by Leonard on 6/4/2017.
  */
class N7Armor extends LightArmor {
  override def toString: String = "N7 Armor"
  override def getBaseArmorRating: Int = 25
  override def getBaseShields: Int = 30
  override def getBaseShieldRecoveryRate: Int = 5
}
