package powers

/**
  * Created by Leonard on 6/10/2017.
  */
//NOTE: CANNOT ADD TWO OF THE SAME BONUS WITH THE SAME AMOUNT
//unless you chain bonuses with DoubleBonus
sealed trait Bonus

//This bonus is used to chain bonuses of the same value.
//This is done automatically in Power.

//StatTracker
case class DoubleBonus(bonus1: Bonus, bonus2: Bonus) extends Bonus

case class HpPercentBonus(amount: Float) extends Bonus

case class EezoPercentBonus(amount: Float) extends Bonus

case class EezoRecoveryRatePercentBonus(amount: Float) extends Bonus

case class MovementBonus(amount: Int) extends Bonus

//WeaponTracker
case class WeaponDamagePercentBonus(amount: Float) extends Bonus

case class MeleeDamagePercentBonus(amount: Float) extends Bonus

case class GrenadeDamagePercentBonus(amount: Float) extends Bonus

case class GunDamagePercentBonus(amount: Float) extends Bonus

case class CriticalHitPercentBonus(amount: Float) extends Bonus //TODO critical hit bonus

//Armor
case class ShieldBonus(amount: Float) extends Bonus

case class LightArmorShieldBonus(amount: Float) extends Bonus

case class HeavyArmorShieldBonus(amount: Float) extends Bonus

case class ShieldRecoveryRateBonus(amount: Float) extends Bonus

case class ArmorRatingPercentBonus(amount: Float) extends Bonus

case class LightArmorArmorRatingPercentBonus(amount: Float) extends Bonus

case class HeavyArmorArmorRatingPercentBonus(amount: Float) extends Bonus

//PowerTracker
case class PowerDamagePercentBonus(amount: Float) extends Bonus
