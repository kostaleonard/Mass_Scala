package interfaces

import board.Board
import effects.{Burned, Chilled}
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
trait Freezer {
  protected var chillChance = 0.0f
  protected var armorRatingPenalty = 0
  protected var movementPenalty = 0
  protected var enemyEezoRecoveryPenalty = 0
  protected var freezeDuration = 0

  def chillCheck: Boolean = math.random < chillChance

  def doChill(attached: Fighter, target: Fighter, board: Board): Unit = {
    if(!target.isChilled){
      val chilled = Chilled(target, freezeDuration, armorRatingPenalty, movementPenalty, enemyEezoRecoveryPenalty)
      target.addEffect(chilled)
    }
  }
}
