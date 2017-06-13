package powers

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class PassivePower extends Power {
  override def isPassive: Boolean = true
  override def isActivated: Boolean = false
  override def isSustained: Boolean = false
}
