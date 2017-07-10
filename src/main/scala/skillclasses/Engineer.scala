package skillclasses

import powers.{CryoAmmo, Fitness, Inferno}
import weapons.{CrusaderShotgun, PredatorPistol}

/**
  * Created by Leonard on 6/4/2017.
  */
class Engineer extends SkillClass {
  availableWeapons.add(new PredatorPistol)
  availableWeapons.add(new CrusaderShotgun)

  availablePowers.add(new CryoAmmo)
  availablePowers.add(new Inferno)
  availablePowers.add(new Fitness)

  override def toString: String = "Engineer"

  override def getBaseHp: Int = 10
  override def getHpGrowthRate: Int = 2
  override def getHpNumberOfLevelsBeforeIncrement: Int = 1
  override def getBaseEezo: Int = 10
  override def getEezoGrowthRate: Int = 2
  override def getEezoNumberOfLevelsBeforeIncrement: Int = 1
  override def getBaseMovement: Int = 5
  override def getMovementGrowthRate: Int = 1
  override def getMovementNumberOfLevelsBeforeIncrement: Int = 5
}
