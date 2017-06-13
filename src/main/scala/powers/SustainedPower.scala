package powers

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class SustainedPower extends Power {
  protected var maxEezoPenalty = 0
  protected var eezoRechargePenalty = 0
  protected var inUse: Boolean = false

  def isInUse: Boolean = inUse

  override def isPassive: Boolean = false
  override def isActivated: Boolean = false
  override def isSustained: Boolean = true

  def getMaxEezoPenalty: Int = maxEezoPenalty
  def getEezoRechargePenalty: Int = eezoRechargePenalty
}
