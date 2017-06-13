package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class AssaultRifle extends Gun {
  override def isPistol: Boolean = false
  override def isAssaultRifle: Boolean = true
  override def isSniperRifle: Boolean = false
  override def isShotgun: Boolean = false
}
