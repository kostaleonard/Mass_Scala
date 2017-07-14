package armor

import powers._

/**
  * Created by Leonard on 6/4/2017.
  */
object Armor {
  //Theoretical value for the maximum armor rating achievable
  val THEORETICAL_MAX_ARMOR_RATING = 100
  val DEFAULT_TURNS_UNTIL_SHIELD_RECOVERY = 3
}
abstract class Armor {
  //More powerful armor should simply have a higher shield rating.
  //Armor rating determines the extent to which health/shield damage is blocked.
  //It should be used in the Weapon class's damage calculation.
  protected var shieldCurrent = 0
  protected var shieldMax = 0
  protected var armorRatingCurrent = 0
  protected var armorRatingMax = 0
  protected var armorRatingPenaltyInReserve = 0
  protected var shieldRecoveryRateCurrent = 0
  protected var shieldRecoveryRateStandard = 0
  protected var shieldRecoveryRatePenaltyInReserve = 0
  protected var turnsUntilShieldRecovery = 0
  protected val defaultTurnsUntilShieldRecovery = Armor.DEFAULT_TURNS_UNTIL_SHIELD_RECOVERY

  def getArmorRatingCurrent: Int = armorRatingCurrent

  def getArmorRatingMax: Int = armorRatingMax

  def getShieldCurrent: Int = shieldCurrent

  def getShieldMax: Int = shieldMax

  def setStatsToMax: Unit = {
    shieldCurrent = shieldMax
    armorRatingCurrent = armorRatingMax
    shieldRecoveryRateCurrent = shieldRecoveryRateStandard
    armorRatingPenaltyInReserve = 0
    shieldRecoveryRatePenaltyInReserve = 0
    turnsUntilShieldRecovery = 0
  }

  def setMaxStats(powers: scala.collection.mutable.Set[Power]): Unit = {
    def setMaxStatsByDefaults: Unit = {
      armorRatingMax = getBaseArmorRating
      shieldMax = getBaseShields
      shieldRecoveryRateStandard = getBaseShieldRecoveryRate
    }

    def setMaxStatsByPowers: Unit = {
      def addBonuses(power: Power): Unit = {
        def addBonus(bonus: Bonus): Unit = bonus match {
          case DoubleBonus(b1, b2) =>
            addBonus(b1)
            addBonus(b2)
          case ShieldBonus(amount) => shieldMax = (shieldMax * (1.0f + amount)).toInt
          case LightArmorShieldBonus(amount) => if(isLightArmor) shieldMax = (shieldMax * (1.0f + amount)).toInt
          case HeavyArmorShieldBonus(amount) => if(isHeavyArmor) shieldMax = (shieldMax * (1.0f + amount)).toInt
          case ShieldRecoveryRateBonus(amount) => shieldRecoveryRateStandard = (shieldRecoveryRateStandard * (1.0f + amount)).toInt
          case ArmorRatingPercentBonus(amount) => armorRatingMax = (armorRatingMax * (1.0f + amount)).toInt
          case LightArmorArmorRatingPercentBonus(amount) => if(isLightArmor) armorRatingMax = (armorRatingMax * (1.0f + amount)).toInt
          case HeavyArmorArmorRatingPercentBonus(amount) => if(isHeavyArmor) armorRatingMax = (armorRatingMax * (1.0f + amount)).toInt
          case _ => ; //Do nothing--this Bonus is handled elsewhere
        }
        power.getBonuses.foreach(addBonus)
      }

      powers.foreach(pow => pow match{
        case pas: PassivePower => addBonuses(pas)
        case _ => ;
      }
      )
    }

    setMaxStatsByDefaults
    setMaxStatsByPowers
  }

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

  def takeShieldRecoveryRatePenalty(amount: Int): Unit = {
    val newRating = shieldRecoveryRateCurrent - amount
    if(newRating < 0) shieldRecoveryRatePenaltyInReserve += newRating
    shieldRecoveryRateCurrent = newRating max 0
  }

  def removeShieldRecoveryRatePenalty(amount: Int): Unit = {
    shieldRecoveryRateCurrent = (shieldRecoveryRateCurrent + amount) min shieldRecoveryRateStandard
    if(shieldRecoveryRatePenaltyInReserve < 0){
      val amountThatCanBeTaken = shieldRecoveryRateCurrent
      val amountThatWillBeTaken = amountThatCanBeTaken min -shieldRecoveryRatePenaltyInReserve
      shieldRecoveryRateCurrent -= amountThatWillBeTaken
      shieldRecoveryRatePenaltyInReserve += amountThatWillBeTaken
    }
  }

  //Abstract methods
  override def toString: String
  def isHeavyArmor: Boolean
  def isLightArmor: Boolean
  def getBaseArmorRating: Int
  def getBaseShields: Int
  def getBaseShieldRecoveryRate: Int
}
