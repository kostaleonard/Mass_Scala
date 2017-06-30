package effects

import fighter.Fighter
import powers.Power

/**
  * Created by Leonard on 6/17/2017.
  */
//Temporary effects inflicted by weapons and powers.
sealed trait Effect {
  //TODO implement Effects.
  //TODO ensure that Fighters use the addToFighter function when they calculate their stats. Otherwise won't work right.
  def doInitialAction: Unit //Do the initial action, like decrementing the armor rating
  def doTurnAction: Unit //Do the turnly action, like damage
  def doRemovalAction: Unit //Do the removal action (usually the inverse of the initial action)
  def decrementTurnsRemaining: Option[Effect] //Return this Effect with turnsRemaining decremented, or None
}

//case class Berserked(fighter: Fighter, turnsRemaining: Int) extends Effect

case class Burned(fighter: Fighter, turnsRemaining: Int, armorRatingPenalty: Int, damagePerTurn: Int) extends Effect {
  override def doInitialAction: Unit = fighter.takeArmorRatingPenalty(armorRatingPenalty)
  override def doTurnAction: Unit = fighter.takeDamage(damagePerTurn)
  override def doRemovalAction: Unit = fighter.removeArmorRatingPenalty(armorRatingPenalty)
  override def decrementTurnsRemaining: Option[Effect] =
    if(turnsRemaining > 0) Some(Burned(fighter, turnsRemaining - 1, armorRatingPenalty, damagePerTurn))
    else None
}

//case class Chilled(fighter: Fighter, turnsRemaining: Int, armorRatingPenalty: Int, movementPenalty: Int, eezoRecoveryPenalty: Int) extends Effect

//case class Bleeding(fighter: Fighter, turnsRemaining: Int, damagePerTurn: Int) extends Effect

//case class Electrocuted(fighter: Fighter, turnsRemaining: Int, shieldRecoveryPenalty: Int) extends Effect

//case class BioticInitiator(fighter: Fighter, turnsRemaining: Int, initiatorPower: Power) extends Effect