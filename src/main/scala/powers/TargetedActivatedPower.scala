package powers
import board.Board
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
abstract class TargetedActivatedPower extends ActivatedPower{
  override def usePower(attacker: Fighter, targetOption: Option[Fighter], board: Board): Unit = {
    if(targetOption.isEmpty) throw new UnsupportedOperationException("Cannot use TargetedActivatedPower without target.")
    super.usePower(attacker, targetOption, board)
  }

  override def isTargeted: Boolean = true
}
