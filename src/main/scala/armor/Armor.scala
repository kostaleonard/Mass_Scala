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
  protected var armorRatingCurrent = 0
  protected var armorRatingMax = 0
  protected var armorRatingPenaltyInReserve = 0
  protected var shieldCurrent = 0
  protected var shieldMax = 0
  protected var shieldRecoveryRateCurrent = 0
  protected var shieldRecoveryRateStandard = 0
  protected var turnsUntilShieldRecovery = 0
  protected val defaultTurnsUntilShieldRecovery = 3

  def getArmorRatingCurrent: Int = armorRatingCurrent

  def getArmorRatingMax: Int = armorRatingMax

  def getShieldCurrent: Int = shieldCurrent

  def getShieldMax: Int = shieldMax

  def armorProtectionModifier: Float = {
    //Returns the percent damage deflected by armor based on armor rating (from 0 to 1.0)
    val horizontalAsymptote = 100 //Cannot reach 100% damage
    val yInterceptController = -1 * horizontalAsymptote //Set to cross through the origin
    val protectionRateExponent = -0.008f //Must be negative. As this increases in magnitude, armorRating has more bang for the buck.
    val protectionRateBase = 2 //Increasing this makes lower armor rating more effective when compared to higher armor rating.
    val normalizer = 100.0f //Used to ensure that the return value falls between 0.0 and 1.0
    ((horizontalAsymptote + yInterceptController * math.pow(protectionRateBase, protectionRateExponent * armorRatingCurrent))/normalizer).toFloat
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

  def delayShieldRecovery: Unit = setTurnsUntilShieldRecovery(defaultTurnsUntilShieldRecovery)

  def approachShieldRecovery: Unit = if(getTurnsUntilShieldRecovery > 0) setTurnsUntilShieldRecovery(getTurnsUntilShieldRecovery - 1)

  def recoverShields: Unit = {
    //Assume that this was called because either:
    //1.canRecoverShields returns true (the Fighter has been undamaged for several turns).
    //2.Some power caused early shield recovery.
    //Does NOT set the turns to recovery to 0, by default.
    shieldCurrent += shieldRecoveryRateCurrent
    shieldCurrent = shieldCurrent min shieldMax
  }

  def doTurnlyActions: Unit = {
    //Allow Fighters to recover HP/shields, EEZO, and do any turnly effects.
    if(canRecoverShields) recoverShields
    else approachShieldRecovery
  }

  def takeArmorRatingPenalty(amount: Int): Unit = {
    val newRating = armorRatingCurrent - amount
    if(newRating < 0) armorRatingPenaltyInReserve += newRating
    armorRatingCurrent = newRating max 0
  }

  def removeArmorRatingPenalty(amount: Int): Unit = {
    armorRatingCurrent = (armorRatingCurrent + amount) min armorRatingMax
    if(armorRatingPenaltyInReserve < 0){
      val amountThatCanBeTaken = armorRatingCurrent
      val amountThatWillBeTaken = amountThatCanBeTaken min -armorRatingPenaltyInReserve
      armorRatingCurrent -= amountThatWillBeTaken
      armorRatingPenaltyInReserve += amountThatWillBeTaken
    }
  }

  //Abstract methods
  override def toString: String
  def isHeavyArmor: Boolean
  def isLightArmor: Boolean
}
