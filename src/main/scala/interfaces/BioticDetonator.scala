package interfaces

/**
  * Created by Leonard on 6/22/2017.
  */
trait BioticDetonator {
  protected var bioticDetonatorPowerMultiplier = 1.0f

  def damageByCombinedPowerLevel(level: Int): Int = (level * bioticDetonatorPowerMultiplier).toInt
}
