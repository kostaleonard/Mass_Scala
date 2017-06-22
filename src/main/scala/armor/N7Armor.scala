package armor

/**
  * Created by Leonard on 6/4/2017.
  */
class N7Armor extends LightArmor {
  armorRating = 25
  shieldMax = 30
  shieldCurrent = shieldMax
  shieldRecoveryRateStandard = 5
  shieldRecoveryRateCurrent = shieldRecoveryRateStandard

  override def toString: String = "N7 Armor"
}
