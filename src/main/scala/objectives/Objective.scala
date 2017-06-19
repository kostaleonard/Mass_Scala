package objectives

import board.{Board, Location}

/**
  * Created by Leonard on 6/19/2017.
  */
sealed trait Objective {
  def isComplete(board: Board): Boolean
}

case class Survive(numTurns: Int) extends Objective {
  override def isComplete(board: Board): Boolean = numTurns <= 0
}

case object SurviveUntilPlayerPartyDeath extends Objective {
  override def isComplete(board: Board): Boolean = board.getPlayerParty.getFighters.isEmpty
}

case object ClearBoard extends Objective {
  override def isComplete(board: Board): Boolean = board.isClearOfEnemies
}

case class Seize(keyLocation: Location) extends Objective {
  //TODO Seize implementation
  override def isComplete(board: Board): Boolean = ???
}
