package armor

/**
  * Created by Leonard on 6/4/2017.
  */
class CBRNArmor extends HeavyArmor {
  override def toString: String = "CBRN Armor"
  override def getBaseArmorRating: Int = 50
  override def getBaseShields: Int = 50
  override def getBaseShieldRecoveryRate: Int = 10
}
