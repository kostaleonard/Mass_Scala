package powers

/**
  * Created by Leonard on 6/4/2017.
  */
class Fitness extends PassivePower {
  override def toString: String = "Fitness"

  override def getDescription: String = "Be stronger, tougher, and deadlier."

  override def choiceName(choice: Int): String = choice match {
    case Power.LVL_1 => "Fitness"
    case Power.LVL_2 => "Durability"
    case Power.LVL_3 => "Melee Damage"
    case Power.LVL_4A => "CQB Mastery"
    case Power.LVL_4B => "Endurance"
    case Power.LVL_5A => "Light Armor"
    case Power.LVL_5B => "Heavy Armor"
    case Power.LVL_6A => "Martial Artist"
    case Power.LVL_6B => "Tank"
    case _ => ???
  }

  override def choiceDescription(choice: Int): String = choice match {
    case Power.LVL_1 => "Health and shield bonus: +15%"
    case Power.LVL_2 => "Health and shield bonus: +25%"
    case Power.LVL_3 => "Melee damage bonus: +20%"
    case Power.LVL_4A => "Melee damage bonus: +40%"
    case Power.LVL_4B => "Movement bonus: +2"
    case Power.LVL_5A => "Light armor bonus: 30%"
    case Power.LVL_5B => "Heavy armor bonus: 30%"
    case Power.LVL_6A => "Melee damage bonus: +80%"
    case Power.LVL_6B => "Health and shield bonus: +50%"
    case _ => ???
  }

  override def addChoiceEffect(choice: Int): Unit = choice match {
    case Power.LVL_1 =>
      addBonus(HpPercentBonus(0.15f))
      addBonus(ShieldBonus(0.15f))
    case Power.LVL_2 =>
      addBonus(HpPercentBonus(0.25f))
      addBonus(ShieldBonus(0.25f))
    case Power.LVL_3 =>
      addBonus(MeleeDamageBonus(0.2f))
    case Power.LVL_4A =>
      addBonus(MeleeDamageBonus(0.4f))
    case Power.LVL_4B =>
      addBonus(MovementBonus(2))
    case Power.LVL_5A =>
      addBonus(LightArmorArmorRatingBonus(0.3f))
    case Power.LVL_5B =>
      addBonus(HeavyArmorArmorRatingBonus(0.3f))
    case Power.LVL_6A =>
      addBonus(MeleeDamageBonus(0.8f))
    case Power.LVL_6B =>
      addBonus(HpPercentBonus(0.5f))
      addBonus(ShieldBonus(0.5f))
    case _ => ???
  }
}
