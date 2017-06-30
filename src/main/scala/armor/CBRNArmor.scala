package armor

/**
  * Created by Leonard on 6/4/2017.
  */
class CBRNArmor extends HeavyArmor {
  armorRatingMax = 50
  armorRatingCurrent = armorRatingMax
  shieldMax = 50
  shieldCurrent = shieldMax
  shieldRecoveryRateStandard = 10
  shieldRecoveryRateCurrent = shieldRecoveryRateStandard

  override def toString: String = "CBRN Armor"
}
