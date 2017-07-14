package fighter

import board.Board
import powers.{ActivatedPower, PassivePower, Power, SustainedPower}

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

  def canUsePower(power: Power, attacker: Fighter): Boolean = learnedPowers(power) && power.canUse && (power match {
    case act: ActivatedPower => attacker.canUseEezo(act.getEezoCost)
    case sus: SustainedPower => if(sus.isInUse) true else attacker.canUseEezo(sus.getEezoCost)
    case pas: PassivePower => false
    case _ => throw new UnsupportedOperationException("Unrecognized Power type.")
  })

  def discontinueAllSustainedPowers(attacker: Fighter): Unit = learnedPowers.foreach(pow => pow match{
    case sus: SustainedPower => if(sus.isInUse) sus.discontinuePower(attacker)
    case _ => ;
  })

  def getSustainedPowersInUse: scala.collection.mutable.Set[SustainedPower] = {
    learnedPowers
      .filter(_.isSustained)
      .map[SustainedPower, scala.collection.mutable.Set[SustainedPower]](pow => pow match{
        case sus: SustainedPower => sus
        case _ => ???
      })
      .filter(_.isInUse)
  }

  def useActivatedPower(power: ActivatedPower, targetOption: Option[Fighter], board: Board, attacker: Fighter): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!learnedPowers(power))
      throw new UnsupportedOperationException("Cannot use a power that is not learned")
    power.usePower(attacker, targetOption, board)
  }

  def useSustainedPower(power: SustainedPower, attacker: Fighter): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!learnedPowers(power))
      throw new UnsupportedOperationException("Cannot use a power that is not in the inventory")
    if(power.isInUse)
      throw new UnsupportedOperationException("Cannot use a power that is already in use")
    power.usePower(attacker)
  }

  def discontinueSustainedPower(power: SustainedPower, attacker: Fighter): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot discontinue a power that is not in the inventory")
    if(!power.isInUse)
      throw new UnsupportedOperationException("Cannot discontinue a power that is not in use")
    power.discontinuePower(attacker)
  }
}
