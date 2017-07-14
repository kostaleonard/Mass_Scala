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
  private var eezoRecoveryRateCurrent = 1
  private var eezoRecoveryRateMax = 1
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
  private var eezoRecoveryRateGrowthRate = 1
  private var eezoRecoveryRateNumberOfLevelsBeforeIncrement = 1
  //Penalties in reserve
  //These are to keep track of when the respective stats would go negative.
  private var eezoRecoveryRatePenaltyInReserve = 0
  private var movementPenaltyInReserve = 0

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

  def setMovementCurrent(mo: Int): Unit = movementCurrent = mo

  def setMovementGrowthRate(mgr: Int): Unit = movementGrowthRate = mgr

  def setMovementNumberOfLevelsBeforeIncrement(levels: Int): Unit = movementNumberOfLevelsBeforeIncrement = levels

  def setEezoRecoveryRateCurrent(rate: Int): Unit = eezoRecoveryRateCurrent = rate

  def setEezoRecoveryRateMax(rate: Int): Unit = eezoRecoveryRateMax = rate

  def setEezoRecoveryRateGrowthRate(rate: Int): Unit = eezoRecoveryRateGrowthRate = rate

  def setEezoRecoveryRateNumberOfLevelsBeforeIncrement(levels: Int): Unit = eezoRecoveryRateNumberOfLevelsBeforeIncrement = levels

  def getEezoRecoveryRateCurrent: Int = eezoRecoveryRateCurrent

  def getEezoRecoveryRateMax: Int = eezoRecoveryRateMax

  def isAlive: Boolean = this.hpCurrent > 0

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
    hpCurrent = hpMax
    eezoCurrent = eezoMax
    movementCurrent = movementMax
    eezoRecoveryRateCurrent = eezoRecoveryRateMax
    canMove = true
    canAttack = true
    eezoRecoveryRatePenaltyInReserve = 0
    movementPenaltyInReserve = 0
  }

  def takeHpDamage(amount: Int): Unit = {
    this.hpCurrent -= amount
    this.hpCurrent = this.hpCurrent max 0
  }

  def loseEezo(amount: Int): Unit = {
    this.eezoCurrent -= amount
    this.eezoCurrent = this.eezoCurrent max 0
  }

  def takeEezoRechargePenalty(amount: Int): Unit = {
    val newRate = eezoRecoveryRateCurrent - amount
    if(newRate < 1) eezoRecoveryRatePenaltyInReserve -= (1 - newRate)
    eezoRecoveryRateCurrent = newRate max 1
  }

  def removeEezoRechargePenalty(amount: Int): Unit = {
    eezoRecoveryRateCurrent = (eezoRecoveryRateCurrent + amount) min eezoRecoveryRateMax
    if(eezoRecoveryRatePenaltyInReserve < 0){
      val amountThatCanBeTaken = eezoRecoveryRateCurrent - 1
      val amountThatWillBeTaken = amountThatCanBeTaken min -eezoRecoveryRatePenaltyInReserve
      eezoRecoveryRateCurrent -= amountThatWillBeTaken
      eezoRecoveryRatePenaltyInReserve += amountThatWillBeTaken
    }
  }

  def takeMovementPenalty(amount: Int): Unit = {
    val newRate = movementCurrent - amount
    if(newRate < 1) movementPenaltyInReserve -= (1 - newRate)
    movementCurrent = newRate max 1
  }

  def removeMovementPenalty(amount: Int): Unit = {
    movementCurrent = (movementCurrent + amount) min movementMax
    if(movementPenaltyInReserve < 0){
      val amountThatCanBeTaken = movementCurrent - 1
      val amountThatWillBeTaken = amountThatCanBeTaken min -movementPenaltyInReserve
      movementCurrent -= amountThatWillBeTaken
      movementPenaltyInReserve += amountThatWillBeTaken
    }
  }

  def doTurnlyActions: Unit = {
    //Allow Fighters to recover HP/shields, EEZO, and do any turnly effects.
    recoverEezo
  }

  def canUseEezo(amount: Int): Boolean = eezoCurrent >= amount

  def setMaxStats(level: Int, skillClass: SkillClass, powers: scala.collection.mutable.Set[Power]): Unit = {
    //TODO figure out if calculateStats is screwed up when I actually start using it
    //TODO incorporate race into this calculation
    //Change stats to the appropriate amounts based on level, skillClass, race, and powers.
    //This should be recalculated any time one of the above parameters changes.
    //i.e. leveling up, change of skill class, change of race, change or leveling up of passive powers
    //Could be initiated during battle, so does not modify current stats (only max stats).
    //In general, this should not be done during battle--no need to keep recalculating these stats.
    def setMaxStatsBySkillClass: Unit = {
      setHpMax(skillClass.getBaseHp)
      setHpGrowthRate(skillClass.getHpGrowthRate)
      setHpNumberOfLevelsBeforeIncrement(skillClass.getHpNumberOfLevelsBeforeIncrement)
      setEezoMax(skillClass.getBaseEezo)
      setEezoGrowthRate(skillClass.getEezoGrowthRate)
      setEezoNumberOfLevelsBeforeIncrement(skillClass.getEezoNumberOfLevelsBeforeIncrement)
      setMovementMax(skillClass.getBaseMovement)
      setMovementGrowthRate(skillClass.getMovementGrowthRate)
      setMovementNumberOfLevelsBeforeIncrement(skillClass.getMovementNumberOfLevelsBeforeIncrement)
      setEezoRecoveryRateMax(skillClass.getBaseEezoRecoveryRate)
      setEezoRecoveryRateGrowthRate(skillClass.getEezoRecoveryRateGrowthRate)
      setEezoRecoveryRateNumberOfLevelsBeforeIncrement(skillClass.getEezoRecoveryRateNumberOfLevelsBeforeIncrement)
    }
    def setMaxStatsByLevel: Unit = {
      for(i <- 2 to level){
        levelUp(i)
      }
    }
    def setMaxStatsByPassivePowers: Unit = {
      def addBonuses(power: Power): Unit = {
        def addBonus(bonus: Bonus): Unit = bonus match {
          case DoubleBonus(b1, b2) =>
            addBonus(b1)
            addBonus(b2)
          case HpPercentBonus(amount) => hpMax = (hpMax * (1.0f + amount)).toInt
          case EezoPercentBonus(amount) => eezoMax = (eezoMax * (1.0f + amount)).toInt
          case MovementBonus(amount) => movementMax += amount
          case EezoRecoveryRatePercentBonus(amount) => eezoRecoveryRateMax = (eezoRecoveryRateMax * (1.0f + amount)).toInt
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

    setMaxStatsBySkillClass
    setMaxStatsByLevel
    setMaxStatsByPassivePowers
  }

  def levelUp(currentLevel: Int): Unit = {
    def levelUpHp: Unit =
      if(currentLevel % hpNumberOfLevelsBeforeIncrement == 0) setHpMax(getHpMax + hpGrowthRate)
    def levelUpEezo: Unit =
      if(currentLevel % eezoNumberOfLevelsBeforeIncrement == 0) setEezoMax(getEezoMax + eezoGrowthRate)
    def levelUpMovement: Unit =
      if(currentLevel % movementNumberOfLevelsBeforeIncrement == 0) {
        setMovementMax(getMovementMax + movementGrowthRate)
        setMovementCurrent(getMovementCurrent + movementGrowthRate)
      }
    def levelUpEezoRecoveryRate: Unit =
      if(currentLevel % eezoRecoveryRateNumberOfLevelsBeforeIncrement == 0) {
        setEezoRecoveryRateMax(getEezoRecoveryRateMax + eezoRecoveryRateGrowthRate)
        setEezoRecoveryRateCurrent(getEezoRecoveryRateCurrent + eezoRecoveryRateCurrent)
      }

    levelUpHp
    levelUpEezo
    levelUpMovement
    levelUpEezoRecoveryRate
  }
}
