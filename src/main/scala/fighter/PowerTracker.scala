package fighter

import powers.{Power, SustainedPower}

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

  def getSustainedPowersInUse: scala.collection.mutable.Set[SustainedPower] = {
    learnedPowers
      .filter(_.isSustained)
      .map[SustainedPower, scala.collection.mutable.Set[SustainedPower]](pow => pow match{
        case sus: SustainedPower => sus
        case _ => ???
      })
      .filter(_.isInUse)
  }
}
