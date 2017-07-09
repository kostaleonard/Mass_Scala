package interfaces

import board.Board
import effects.Burned
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
trait Burner {
  protected var burnChance = 0.0f
  protected var armorRatingPenalty = 0
  protected var burnDamagePerTurn = 0
  protected var burnDuration = 0
  protected var frozenTargetDamageBonus = 1.0f

  def getFrozenTargetDamageBonus: Float = frozenTargetDamageBonus

  def burnCheck: Boolean = math.random < burnChance

  def doBurn(attacker: Fighter, target: Fighter, board: Board): Unit = {
    if(!target.isBurned){
      val burned = Burned(target, burnDuration, armorRatingPenalty, burnDamagePerTurn)
      target.addEffect(burned)
    }
  }
}
