package interfaces

/**
  * Created by Leonard on 6/22/2017.
  */
trait Ranged {
  protected var minRange: Int = 0
  protected var maxRange: Int = 0
  def getMinRange: Int = minRange
  def getMaxRange: Int = maxRange
  def inRange(crowFliesDistance: Int): Boolean = crowFliesDistance >= minRange && crowFliesDistance <= maxRange
}
