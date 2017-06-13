package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class SniperRifle extends Gun {
  override def isPistol: Boolean = false
  override def isAssaultRifle: Boolean = false
  override def isSniperRifle: Boolean = true
  override def isShotgun: Boolean = false
}
