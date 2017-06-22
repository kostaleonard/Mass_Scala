package fighter

/**
  * Created by Leonard on 6/3/2017.
  */
object Party {
  def empty: Party = {
    new Party
  }

  def random(size: Int): Party = {
    val party = new Party
    for(i <- 0 until size){
      party.addFighter(Fighter.random(1))
    }
    party
  }
}
class Party {
  private val fighters = scala.collection.mutable.Set.empty[Fighter]

  def getFighters: scala.collection.mutable.Set[Fighter] = fighters

  def addFighter(f: Fighter): Unit = fighters.add(f)

  def removeFighter(f: Fighter): Unit = fighters.remove(f)

  def recoverMovesAndActions: Unit = {
    //Allow Fighters to recover moves and actions so that they can use them next turn.
    fighters.foreach(_.recoverMovesAndActions)
  }

  def doTurnlyActions: Unit = {
    //Allow Fighters to recover HP/shields, EEZO, and do any turnly effects.
    fighters.foreach(_.doTurnlyActions)
  }
}
