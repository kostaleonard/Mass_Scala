package fighter

/**
  * Created by Leonard on 6/4/2017.
  */
object EXPTracker {
  private val EXP_REQUIRED_CONSTANT = 1.5f
  private val EXP_REQUIRED_POWER = 2.0f

  def create(level: Int): EXPTracker = {
    val e = new EXPTracker
    e.level = level min 100
    e.currentEXP = e.getEXPAtLevel(e.level)
    e
  }
}

class EXPTracker {
  private var level = 1
  private var currentEXP = 0

  def getLevel: Int = level

  def getEXPAtLevel(lvl: Int): Int = {
    if(lvl >= 100) -1 else
      (EXPTracker.EXP_REQUIRED_CONSTANT * Math.pow(lvl, EXPTracker.EXP_REQUIRED_POWER)).toInt
  }

  def getNextLevelEXP: Int = this.getEXPAtLevel(this.level + 1)

  def levelUp: Unit = {
    if(this.level < 100) this.level += 1
  }

  def gainEXP(amount: Int): Unit = {
    this.currentEXP += amount
  }

  def canLevelUp: Boolean = this.currentEXP >= getNextLevelEXP
}
