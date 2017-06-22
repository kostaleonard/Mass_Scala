package powers

import board.Board
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
abstract class UntargetedActivatedPower extends ActivatedPower {
  override def usePower(attacker: Fighter, targetOption: Option[Fighter], board: Board): Unit = {
    if(targetOption.nonEmpty) throw new UnsupportedOperationException("Cannot use UntargetedActivatedPower with target.")
    super.usePower(attacker, targetOption, board)
  }

  override def isTargeted: Boolean = false
}
