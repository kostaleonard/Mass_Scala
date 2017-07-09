package interfaces

import board.Board
import effects.Electrocuted
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
trait Electrocuter {
  protected var electrocuteChance = 0.0f
  protected var techExplosionDamage = 0
  protected var shieldRecoveryPenalty = 0
  protected var electrocuteDuration = 0

  def getTechExplosionDamage: Int = techExplosionDamage

  def electrocuteCheck: Boolean = math.random < electrocuteChance

  def doElectrocute(attacker: Fighter, target: Fighter, board: Board): Unit = {
    if(!target.isElectrocuted){
      val electrocuted = Electrocuted(target, electrocuteDuration, shieldRecoveryPenalty)
      target.addEffect(electrocuted)
    }
  }
}
