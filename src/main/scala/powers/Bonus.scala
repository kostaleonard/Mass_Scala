package powers

/**
  * Created by Leonard on 6/10/2017.
  */
//NOTE: CANNOT ADD TWO OF THE SAME BONUS WITH THE SAME AMOUNT
//unless you chain bonuses with DoubleBonus
sealed trait Bonus

//This bonus is used to chain bonuses of the same value.
//This is done automatically in Power.
case class DoubleBonus(bonus1: Bonus, bonus2: Bonus) extends Bonus

case class HpPercentBonus(amount: Float) extends Bonus

case class EezoPercentBonus(amount: Float) extends Bonus

case class MovementBonus(amount: Int) extends Bonus

case class WeaponDamageBonus(amount: Float) extends Bonus

case class MeleeDamageBonus(amount: Float) extends Bonus

case class GrenadeDamageBonus(amount: Float) extends Bonus

case class GunDamageBonus(amount: Float) extends Bonus

case class ShieldBonus(amount: Float) extends Bonus

case class LightArmorShieldBonus(amount: Float) extends Bonus

case class HeavyArmorShieldBonus(amount: Float) extends Bonus

case class ShieldRecoveryRateBonus(amount: Float) extends Bonus

case class LightArmorShieldRecoveryRateBonus(amount: Float) extends Bonus

case class HeavyArmorShieldRecoveryRateBonus(amount: Float) extends Bonus

case class ArmorRatingBonus(amount: Float) extends Bonus

case class LightArmorArmorRatingBonus(amount: Float) extends Bonus

case class HeavyArmorArmorRatingBonus(amount: Float) extends Bonus
