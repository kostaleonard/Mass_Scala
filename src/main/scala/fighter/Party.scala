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
}
