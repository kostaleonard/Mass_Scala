package powers

/**
  * Created by Leonard on 6/4/2017.
  */
object Power {
  val MAX_POWER_LEVEL = 6
  val LVL_1 = 1
  val LVL_2 = 2
  val LVL_3 = 3
  val LVL_4A = 4
  val LVL_4B = 5
  val LVL_5A = 6
  val LVL_5B = 7
  val LVL_6A = 8
  val LVL_6B = 9
}
abstract class Power {
  protected var level = 0
  protected var eezoCost = 0
  protected val levelUpChoices = scala.collection.mutable.Set.empty[Int]
  protected val bonuses = scala.collection.mutable.Set.empty[Bonus]

  def canUse: Boolean = getLevel > 0
  def getLevel: Int = level
  def getEezoCost: Int = eezoCost
  def getBonuses: scala.collection.mutable.Set[Bonus] = bonuses
  def addBonus(bonus: Bonus): Unit = {
    if(bonuses(bonus)){
      //This bonus is already in bonuses.
      //Use DoubleBonus to make sure it gets counted twice.
      bonuses.remove(bonus)
      bonuses.add(DoubleBonus(bonus, bonus))
    }
    else bonuses.add(bonus)
  }
  def clearBonuses: Unit = bonuses.clear
  def levelUp(choice: Int): Unit = {
    level += 1
    levelUpChoices.add(choice)
    addChoiceEffect(choice)
  }

  //Abstract methods
  override def toString: String
  def getDescription: String
  def isPassive: Boolean
  def isActivated: Boolean
  def isSustained: Boolean
  //Choice is one of the values registered in the Power object above.
  def choiceName(choice: Int): String
  def choiceDescription(choice: Int): String
  def addChoiceEffect(choice: Int): Unit
}
