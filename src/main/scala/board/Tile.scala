package board

import fighter.Fighter

/**
  * Created by Leonard on 6/3/2017.
  */
sealed trait Tile {
  private var fighter: Option[Fighter] = None

  def getFighter: Option[Fighter] = fighter
  def setFighter(f: Option[Fighter]): Unit = fighter = f

  //Abstract methods
  def getDefenseModifier: Float
  def getAttackModifier: Float
  def getMovementCost: Int
}

class GrassPlains extends Tile {
  override def getAttackModifier: Float = 1.0f
  override def getDefenseModifier: Float = 1.0f
  override def getMovementCost: Int = 1
}

class Mountains extends Tile {
  override def getAttackModifier: Float = 1.5f
  override def getDefenseModifier: Float = 1.5f
  override def getMovementCost: Int = 2
}