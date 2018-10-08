package actions

import board.Board
import fighter.Fighter
import powers.{ActivatedPower, SustainedPower}
import weapons.Weapon

/**
  * Created by Leonard on 6/17/2017.
  */
sealed trait Action {
  def doAction: Unit
}

case class Wait(attacker: Fighter) extends Action{
  //That was easy!
  override def doAction: Unit = attacker.waitOneTurn

  override def toString: String = "Wait"
}

case class UseWeapon(weapon: Weapon, attacker: Fighter, target: Fighter, board: Board) extends Action {
  override def doAction: Unit = {
    if(!attacker.getWeapons(weapon))
      throw new UnsupportedOperationException("Fighter must be using a Weapon in their inventory.")
    attacker.useWeapon(weapon, target, board)
  }

  override def toString: String = "Use " + weapon.toString
}

case class ReloadWeapon(weapon: Weapon, attacker: Fighter) extends Action {
  override def doAction: Unit = {
    if(!attacker.getWeapons(weapon))
      throw new UnsupportedOperationException("Fighter must be reloading a Weapon in their inventory.")
    attacker.reload(weapon)
  }

  override def toString: String = "Reload " + weapon.toString
}

case class UseActivatedPower(power: ActivatedPower, attacker: Fighter, targetOption: Option[Fighter], board: Board) extends Action {
  override def doAction: Unit = {
    if(!attacker.getPowers(power))
      throw new UnsupportedOperationException("Fighter must be using a learned Power.")
    attacker.useActivatedPower(power, targetOption, board)
  }

  override def toString: String = "Use " + power.toString
}

case class UseSustainedPower(power: SustainedPower, attacker: Fighter) extends Action {
  override def doAction: Unit = {
    if(!attacker.getPowers(power))
      throw new UnsupportedOperationException("Fighter must be using a learned Power.")
    attacker.useSustainedPower(power)
  }

  override def toString: String = "Sustain " + power.toString
}

case class DiscontinueSustainedPower(power: SustainedPower, attacker: Fighter) extends Action {
  override def doAction: Unit = {
    if(!attacker.getPowers(power))
      throw new UnsupportedOperationException("Fighter must be using a learned Power.")
    attacker.discontinueSustainedPower(power)
  }

  override def toString: String = "Discontinue " + power.toString
}