package fighter

import powers.Power

/**
  * Created by Leonard on 6/4/2017.
  */
class PowerTracker {
  private val learnedPowers = scala.collection.mutable.Set.empty[Power]
  private var availablePowerPoints: Int = 0

  def levelUp: Unit = setAvailablePowerPoints(getAvailablePowerPoints + 1)

  def getAvailablePowerPoints: Int = availablePowerPoints

  def setAvailablePowerPoints(p: Int): Unit = availablePowerPoints = p

  def getPowers: scala.collection.mutable.Set[Power] = learnedPowers

  def learnPower(power: Power): Unit = learnedPowers.add(power)
}
