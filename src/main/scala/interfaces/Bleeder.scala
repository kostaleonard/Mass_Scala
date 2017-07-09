package interfaces

import board.Board
import effects.Bleeding
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
trait Bleeder {
  protected var bleedChance = 0.0f
  protected var bleedDamagePerTurn = 0
  protected var bleedDuration = 0

  def bleedCheck: Boolean = math.random < bleedChance

  def doBleed(attacker: Fighter, target: Fighter, board: Board): Unit = {
    if(!target.isBleeding){
      val bleeding = Bleeding(target, bleedDuration, bleedDamagePerTurn)
      target.addEffect(bleeding)
    }
  }
}
