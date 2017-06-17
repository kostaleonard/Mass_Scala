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

case object Wait extends Action{
  //That was easy!
  override def doAction: Unit = {}
}

case class UseWeapon(weapon: Weapon, attacker: Fighter, target: Fighter, board: Board) extends Action {
  override def doAction: Unit = {
    if(!attacker.getWeapons(weapon))
      throw new UnsupportedOperationException("Fighter must be using a Weapon in their inventory.")
    weapon.doAttack(attacker, target, board)
  }
}

case class UseActivatedPower(power: ActivatedPower, attacker: Fighter, targetOption: Option[Fighter], board: Board) extends Action {
  override def doAction: Unit = {
    if(!attacker.getPowers(power))
      throw new UnsupportedOperationException("Fighter must be using a learned Power.")
    power.usePower(attacker, targetOption, board)
  }
}

case class UseSustainedPower(power: SustainedPower, attacker: Fighter) extends Action {
  override def doAction: Unit = {
    if(!attacker.getPowers(power))
      throw new UnsupportedOperationException("Fighter must be using a learned Power.")
    power.usePower(attacker)
  }
}

case class DiscontinueSustainedPower(power: SustainedPower, attacker: Fighter) extends Action {
  override def doAction: Unit = {
    if(!attacker.getPowers(power))
      throw new UnsupportedOperationException("Fighter must be using a learned Power.")
    power.discontinuePower(attacker)
  }
}