package effects

/**
  * Created by Leonard on 6/17/2017.
  */
//Temporary effects inflicted by weapons and powers.
sealed trait Effect

case class Berserk(turnsRemaining: Int) extends Effect

case class Burned(turnsRemaining: Int, armorRatingPenalty: Int, damagePerTurn: Int) extends Effect

case class Chilled(turnsRemaining: Int, armorRatingPenalty: Int, movementPenalty: Int) extends Effect

case class Frozen(turnsRemaining: Int, armorRatingPenalty: Int) extends Effect

case class Bleeding(turnsRemaining: Int, damagePerTurn: Int) extends Effect

case class Poisoned(turnsRemaining: Int, damagePerTurn: Int) extends Effect