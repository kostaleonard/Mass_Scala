package effects

import fighter.Fighter
import powers.Power

/**
  * Created by Leonard on 6/17/2017.
  */
//Temporary effects inflicted by weapons and powers.
sealed trait Effect {
  def isSameEffect(other: Effect): Boolean = this.toString.equals(other.toString)

  //Abstract methods:
  def doInitialAction: Unit //Do the initial action, like decrementing the armor rating
  def doTurnAction: Unit //Do the turnly action, like damage
  def doRemovalAction: Unit //Do the removal action (usually the inverse of the initial action)
  def decrementTurnsRemaining: Option[Effect] //Return this Effect with turnsRemaining decremented, or None
  def canStack: Boolean //Return true if this Effect can be added to the same fighter multiple times
  override def toString: String
}

//TODO berserked
//case class Berserked(fighter: Fighter, turnsRemaining: Int) extends Effect

case class Burned(fighter: Fighter, turnsRemaining: Int, armorRatingPenalty: Int, damagePerTurn: Int) extends Effect {
  override def doInitialAction: Unit = fighter.takeArmorRatingPenalty(armorRatingPenalty)
  override def doTurnAction: Unit = fighter.takeDamage(damagePerTurn)
  override def doRemovalAction: Unit = fighter.removeArmorRatingPenalty(armorRatingPenalty)
  override def decrementTurnsRemaining: Option[Effect] =
    if(turnsRemaining > 0) Some(Burned(fighter, turnsRemaining - 1, armorRatingPenalty, damagePerTurn))
    else None
  override def canStack: Boolean = false
  override def toString: String = "Burned"
}

case class Chilled(fighter: Fighter, turnsRemaining: Int, armorRatingPenalty: Int, movementPenalty: Int, eezoRecoveryPenalty: Int) extends Effect {
  override def doInitialAction: Unit = {
    fighter.takeArmorRatingPenalty(armorRatingPenalty)
    fighter.takeMovementPenalty(movementPenalty)
    fighter.takeEezoRechargePenalty(eezoRecoveryPenalty)
  }
  override def doTurnAction: Unit = {}
  override def doRemovalAction: Unit = {
    fighter.removeArmorRatingPenalty(armorRatingPenalty)
    fighter.removeMovementPenalty(movementPenalty)
    fighter.removeEezoRechargePenalty(eezoRecoveryPenalty)
  }
  override def decrementTurnsRemaining: Option[Effect] =
    if(turnsRemaining > 0) Some(Chilled(fighter, turnsRemaining - 1, armorRatingPenalty, movementPenalty, eezoRecoveryPenalty))
    else None
  override def canStack: Boolean = false
  override def toString: String = "Chilled"
}

case class Bleeding(fighter: Fighter, turnsRemaining: Int, damagePerTurn: Int) extends Effect {
  override def doInitialAction: Unit = {}
  override def doTurnAction: Unit = fighter.takeDamage(damagePerTurn)
  override def doRemovalAction: Unit = {}
  override def decrementTurnsRemaining: Option[Effect] =
    if(turnsRemaining > 0) Some(Bleeding(fighter, turnsRemaining - 1, damagePerTurn))
    else None
  override def canStack: Boolean = false
  override def toString: String = "Bleeding"
}

case class Electrocuted(fighter: Fighter, turnsRemaining: Int, shieldRecoveryPenalty: Int) extends Effect {
  override def doInitialAction: Unit = {
    fighter.takeShieldRecoveryRatePenalty(shieldRecoveryPenalty)
  }
  override def doTurnAction: Unit = {}
  override def doRemovalAction: Unit = {
    fighter.removeShieldRecoveryRatePenalty(shieldRecoveryPenalty)
  }
  override def decrementTurnsRemaining: Option[Effect] =
    if(turnsRemaining > 0) Some(Electrocuted(fighter, turnsRemaining - 1, shieldRecoveryPenalty))
    else None
  override def canStack: Boolean = false
  override def toString: String = "Electrocuted"
}

case class BioticInitiatorEffect(fighter: Fighter, turnsRemaining: Int, initiatorPower: Power) extends Effect {
  override def doInitialAction: Unit = {}
  override def doTurnAction: Unit = {}
  override def doRemovalAction: Unit = {}
  override def decrementTurnsRemaining: Option[Effect] =
    if(turnsRemaining > 0) Some(BioticInitiatorEffect(fighter, turnsRemaining - 1, initiatorPower))
    else None
  override def canStack: Boolean = true
  override def toString: String = "Biotic Initiator"
}