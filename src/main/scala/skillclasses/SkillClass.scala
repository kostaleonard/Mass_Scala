package skillclasses

import powers.Power
import weapons.Weapon

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class SkillClass {
  protected val availablePowers = scala.collection.mutable.Set.empty[Power]
  protected val availableWeapons = scala.collection.mutable.Set.empty[Weapon]

  def getAvailablePowers: scala.collection.mutable.Set[Power] = this.availablePowers
  def getAvailableWeapons: scala.collection.mutable.Set[Weapon] = this.availableWeapons

  //Abstract methods
  //Reflect stats at level 1
  override def toString: String
  def getBaseHp: Int
  def getHpGrowthRate: Int
  def getHpNumberOfLevelsBeforeIncrement: Int
  def getBaseEezo: Int
  def getEezoGrowthRate: Int
  def getEezoNumberOfLevelsBeforeIncrement: Int
  def getBaseMovement: Int
  def getMovementGrowthRate: Int
  def getMovementNumberOfLevelsBeforeIncrement: Int
  def getBaseEezoRecoveryRate: Int
  def getEezoRecoveryRateGrowthRate: Int
  def getEezoRecoveryRateNumberOfLevelsBeforeIncrement: Int
}
