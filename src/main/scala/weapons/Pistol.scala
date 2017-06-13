package weapons

/**
  * Created by Leonard on 6/4/2017.
  */
abstract class Pistol extends Gun {
  override def isPistol: Boolean = true
  override def isAssaultRifle: Boolean = false
  override def isSniperRifle: Boolean = false
  override def isShotgun: Boolean = false
}
