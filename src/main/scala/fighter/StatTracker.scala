package fighter

import board.{GrassPlains, Mountains, Tile}
import skillclasses.SkillClass
import powers._

/**
  * Created by Leonard on 6/4/2017.
  */
object StatTracker {
  val DEFAULT_CROSSABLE_TILES: scala.collection.mutable.Set[Class[_ <: Tile]] = scala.collection.mutable.Set(
    (new GrassPlains).getClass,
    (new Mountains).getClass
  )
}
class StatTracker {
  private var hpCurrent = 1
  private var hpMax = 1
  private var eezoCurrent = 1
  private var eezoMax = 1
  private var movementCurrent = 1
  private var movementMax = 1
  private var canMove = true
  private var canAttack = true
  private val crossableTiles = StatTracker.DEFAULT_CROSSABLE_TILES
  //Stat growth rates
  private var hpGrowthRate = 1
  private var hpNumberOfLevelsBeforeIncrement = 1
  private var eezoGrowthRate = 1
  private var eezoNumberOfLevelsBeforeIncrement = 1
  private var movementGrowthRate = 1
  private var movementNumberOfLevelsBeforeIncrement = 1
  //Per turn recovery rate
  private var eezoRecoveryRateCurrent = 1
  private var eezoRecoveryRateMax = 1
  private var eezoRecoveryRateGrowthRate = 1
  private var eezoRecoveryRateNumberOfLevelsBeforeIncrement = 1

  def recoverEezo: Unit = eezoCurrent = (eezoCurrent + eezoRecoveryRateCurrent) min eezoMax

  def setHpMax(hp: Int): Unit = hpMax = hp

  def setHpCurrent(hp: Int): Unit = hpCurrent = hp

  def setHpGrowthRate(hpgr: Int): Unit = hpGrowthRate = hpgr

  def setHpNumberOfLevelsBeforeIncrement(levels: Int): Unit = hpNumberOfLevelsBeforeIncrement = levels

  def setEezoMax(e0: Int): Unit = eezoMax = e0

  def setEezoCurrent(e0: Int): Unit = eezoCurrent = e0

  def setEezoGrowthRate(e0gr: Int): Unit = eezoGrowthRate = e0gr

  def setEezoNumberOfLevelsBeforeIncrement(levels: Int): Unit = eezoNumberOfLevelsBeforeIncrement = levels

  def setMovementMax(mo: Int): Unit = movementMax = mo

  def setMovementGrowthRate(mgr: Int): Unit = movementGrowthRate = mgr

  def setMovementNumberOfLevelsBeforeIncrement(levels: Int): Unit = movementNumberOfLevelsBeforeIncrement = levels

  def isAlive: Boolean = this.hpCurrent <= 0

  def getHpCurrent: Int = hpCurrent

  def getHpMax: Int = hpMax

  def getEezoCurrent: Int = eezoCurrent

  def getEezoMax: Int = eezoMax

  def getMovementCurrent: Int = movementCurrent

  def getMovementMax: Int = movementMax

  def getCanFighterMove: Boolean = canMove

  def getCanFighterAttack: Boolean = canAttack

  def setCanFighterMove(b: Boolean): Unit = canMove = b

  def setCanFighterAttack(b: Boolean): Unit = canAttack = b

  def getCrossableTiles: scala.collection.mutable.Set[Class[_ <: Tile]] = crossableTiles

  def addCrossableTile(tile: Class[_ <: Tile]): Unit = crossableTiles.add(tile)

  def removeCrossableTile(tile: Class[_ <: Tile]): Unit = crossableTiles.remove(tile)

  def canCross(tile: Class[_ <: Tile]): Boolean = crossableTiles(tile)

  def setCurrentStatsToMax: Unit = {
    //Set the current stats of this fighter to their maxes
    def setHpToMax: Unit = hpCurrent = hpMax
    def setEezoToMax: Unit = eezoCurrent = eezoMax
    def setMovementToMax: Unit = movementCurrent = movementMax

    setHpToMax
    setEezoToMax
    setMovementToMax
    canMove = true
    canAttack = true
  }

  def takeHpDamage(amount: Int): Unit = {
    this.hpCurrent -= amount
    this.hpCurrent = this.hpCurrent max 0
  }

  def loseEezo(amount: Int): Unit = {
    this.eezoCurrent -= amount
    this.eezoCurrent = this.eezoCurrent max 0
  }

  def canUseEezo(amount: Int): Boolean = eezoCurrent >= amount

  def calculateStats(level: Int, skillClass: SkillClass, powers: scala.collection.mutable.Set[Power]): Unit = {
    //TODO incorporate race into this calculation
    //Change stats to the appropriate amounts based on level, skillClass, race, and powers
    //This should be recalculated any time one of the above parameters changes
    //i.e. leveling up, change of skill class, change of race, change or leveling up of powers
    def calculateStatsBySkillClass: Unit = {
      setHpMax(skillClass.getBaseHp)
      setHpGrowthRate(skillClass.getHpGrowthRate)
      setHpNumberOfLevelsBeforeIncrement(skillClass.getHpNumberOfLevelsBeforeIncrement)
      setEezoMax(skillClass.getBaseEezo)
      setEezoGrowthRate(skillClass.getEezoGrowthRate)
      setEezoNumberOfLevelsBeforeIncrement(skillClass.getEezoNumberOfLevelsBeforeIncrement)
      setMovementMax(skillClass.getBaseMovement)
      setMovementGrowthRate(skillClass.getMovementGrowthRate)
      setMovementNumberOfLevelsBeforeIncrement(skillClass.getMovementNumberOfLevelsBeforeIncrement)
      //TODO add eezo recovery rate into skill class
    }
    def calculateStatsByLevel: Unit = {
      for(i <- 2 to level){
        levelUp(i)
      }
    }
    def calculateStatsByPowers: Unit = {
      def addBonuses(power: Power): Unit = {
        def addBonus(bonus: Bonus): Unit = bonus match {
          case DoubleBonus(b1, b2) =>
            addBonus(b1)
            addBonus(b2)
          case HpPercentBonus(amount) => hpMax = (hpMax * amount).toInt
          case WeaponDamageBonus(amount) => ; //Do nothing--this is handled in Weapon
          case _ => ???
        }
        power.getBonuses.foreach(addBonus)
      }

      powers.foreach(pow => pow match{
        case act: ActivatedPower =>
          //Pretty sure that we can safely do nothing here, barring anything crazy.
          ;
        case sus: SustainedPower =>
          //Check to see if the power is in use.
          //If it is, then we may need to do something to our stats.
          //TODO sustained powers changing stats
          if(sus.isInUse) addBonuses(sus)
        case pas: PassivePower =>
          //We will DEFINITELY need to change stats here.
          //TODO passive powers changing stats
          addBonuses(pas)
        case _ =>
          //This is an unrecognized kind of power.
          ???
        }
      )
    }

    calculateStatsBySkillClass
    calculateStatsByLevel
    calculateStatsByPowers
  }

  def levelUp(currentLevel: Int): Unit = {
    def levelUpHp: Unit =
      if(currentLevel % hpNumberOfLevelsBeforeIncrement == 0) setHpMax(getHpMax + hpGrowthRate)
    def levelUpEezo: Unit =
      if(currentLevel % eezoNumberOfLevelsBeforeIncrement == 0) setEezoMax(getEezoMax + eezoGrowthRate)
    def levelUpMovement: Unit =
      if(currentLevel % movementNumberOfLevelsBeforeIncrement == 0) setMovementMax(getMovementMax + movementGrowthRate)

    levelUpHp
    levelUpEezo
    levelUpMovement
  }
}
