package armor

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Armor {
  //More powerful armor should simply have a higher shield rating.
  //Armor rating determines the extent to which health damage is blocked.
  //It should be used in the Weapon class's damage calculation.
  protected var armorRating = 0
  protected var shieldCurrent = 0
  protected var shieldMax = 0
  protected var shieldRecoveryRateCurrent = 0
  protected var shieldRecoveryRateMax = 0

  def isShieldActive: Boolean = this.shieldCurrent > 0

  def takeShieldDamage(amount: Int): Unit = {
    this.shieldCurrent -= amount
    this.shieldCurrent = this.shieldCurrent max 0
  }

  //Abstract methods
  override def toString: String
  def isHeavyArmor: Boolean
  def isLightArmor: Boolean
}
