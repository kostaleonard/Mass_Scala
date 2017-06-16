package fighter

import org.scalatest._

/**
  * Created by Leonard on 6/16/2017.
  */
class FighterSpec extends FlatSpec with Matchers {

  def randomFighterFixture = new{
    val f1 = Fighter.random(1)
    val f2 = Fighter.random(2)
  }

  "A Fighter" should "be able to attack other Fighters." in {
    //TODO attack fighters test
  }
}
