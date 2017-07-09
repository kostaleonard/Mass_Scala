package interfaces

import board.Board
import effects.BioticInitiatorEffect
import fighter.Fighter
import powers.TargetedActivatedPower

/**
  * Created by Leonard on 6/22/2017.
  */
trait BioticInitiator {
  protected var bioticInitiatorDuration = 0
  protected var bioticInitiatorPower: TargetedActivatedPower

  def doBioticInitiator(attacker: Fighter, target: Fighter, board: Board): Unit = {
    val initiator = BioticInitiatorEffect(target, bioticInitiatorDuration, bioticInitiatorPower)
    target.addEffect(initiator)
  }
}
