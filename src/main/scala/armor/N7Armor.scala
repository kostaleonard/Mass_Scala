package armor

/**
  * Created by Leonard on 6/4/2017.
  */
class N7Armor extends LightArmor {
  armorRatingMax = 25
  armorRatingCurrent = armorRatingMax
  shieldMax = 30
  shieldCurrent = shieldMax
  shieldRecoveryRateStandard = 5
  shieldRecoveryRateCurrent = shieldRecoveryRateStandard

  override def toString: String = "N7 Armor"
}
