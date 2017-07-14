package skillclasses

import powers.{CryoAmmo, Fitness}
import weapons.{AvengerAssaultRifle, CrusaderShotgun, MantisSniperRifle, PredatorPistol}

/**
  * Created by Leonard on 6/4/2017.
  */
object Soldier {
  //TODO factory creation method.
}
class Soldier extends SkillClass{
  availableWeapons.add(new PredatorPistol)
  availableWeapons.add(new AvengerAssaultRifle)
  availableWeapons.add(new MantisSniperRifle)
  availableWeapons.add(new CrusaderShotgun)

  availablePowers.add(new CryoAmmo)
  availablePowers.add(new Fitness)

  override def toString: String = "Soldier"

  override def getBaseHp: Int = 15
  override def getHpGrowthRate: Int = 3
  override def getHpNumberOfLevelsBeforeIncrement: Int = 1
  override def getBaseEezo: Int = 0
  override def getEezoGrowthRate: Int = 0
  override def getEezoNumberOfLevelsBeforeIncrement: Int = 1
  override def getBaseMovement: Int = 7
  override def getMovementGrowthRate: Int = 1
  override def getMovementNumberOfLevelsBeforeIncrement: Int = 5
  override def getBaseEezoRecoveryRate: Int = 1
  override def getEezoRecoveryRateGrowthRate: Int = 1
  override def getEezoRecoveryRateNumberOfLevelsBeforeIncrement: Int = 3
}
