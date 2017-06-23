package interfaces

/**
  * Created by Leonard on 6/22/2017.
  */
trait Burner {
  protected var burnChance = 0.0f
  protected var armorRatingPenalty = 0
  protected var burnDamagePerTurn = 0
  protected var burnDuration = 0
  protected var frozenTargetDamageBonus = 1.0f
}
