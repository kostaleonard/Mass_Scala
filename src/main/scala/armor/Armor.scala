package armor

/**
  * Created by Leonard on 6/4/2017.
  */
object Armor {
  //Theoretical value for the maximum armor rating achievable
  val THEORETICAL_MAX_ARMOR_RATING = 100
}
abstract class Armor {
  //More powerful armor should simply have a higher shield rating.
  //Armor rating determines the extent to which health/shield damage is blocked.
  //It should be used in the Weapon class's damage calculation.
  protected var armorRating = 0
  protected var shieldCurrent = 0
  protected var shieldMax = 0
  protected var shieldRecoveryRateCurrent = 0
  protected var shieldRecoveryRateStandard = 0
  protected var turnsUntilShieldRecovery = 0
  protected val defaultTurnsUntilShieldRecovery = 3

  def armorProtectionModifier: Float = {
    //Returns the percent damage deflected by armor based on armor rating (from 0 to 1.0)
    val horizontalAsymptote = 100 //Cannot reach 100% damage
    val yInterceptController = -1 * horizontalAsymptote //Set to cross through the origin
    val protectionRateExponent = -0.008f //Must be negative. As this increases in magnitude, armorRating has more bang for the buck.
    val protectionRateBase = 2 //Increasing this makes lower armor rating more effective when compared to higher armor rating.
    val normalizer = 100.0f //Used to ensure that the return value falls between 0.0 and 1.0
    ((horizontalAsymptote + yInterceptController * math.pow(protectionRateBase, protectionRateExponent * armorRating))/normalizer).toFloat
  }

  def isShieldActive: Boolean = this.shieldCurrent > 0

  def takeShieldDamage(amount: Int): Unit = {
    //The shield will absorb all of an attack,
    //even if the attack would do more damage than the current shield level.
    //Once the shield is "cracked", damage goes directly to health.
    this.shieldCurrent -= amount
    this.shieldCurrent = this.shieldCurrent max 0
  }

  def canRecoverShields: Boolean = turnsUntilShieldRecovery == 0

  def setTurnsUntilShieldRecovery(turns: Int): Unit = turnsUntilShieldRecovery = turns

  def getTurnsUntilShieldRecovery: Int = turnsUntilShieldRecovery

  def recoverShields: Unit = {
    //Assume that this was called because either:
    //1.canRecoverShields returns true (the Fighter has been undamaged for several turns).
    //2.Some power caused early shield recovery.
    //Does NOT set the turns to recovery to 0, by default.
    shieldCurrent += shieldRecoveryRateCurrent
    shieldCurrent = shieldCurrent min shieldMax
  }

  //Abstract methods
  override def toString: String
  def isHeavyArmor: Boolean
  def isLightArmor: Boolean
}
