package fighter

import armor.{Armor, CBRNArmor, N7Armor}
import skillclasses.{Engineer, SkillClass, Soldier}
import weapons._
import powers.Power
import board.Location

/**
  * Created by Leonard on 6/3/2017.
  */
object Fighter {
  def random(level: Int): Fighter = {
    //TODO add support for factory creation of Fighters
    val f = new Fighter(level)
    var rand = Math.random
    //Add skill class
    if(rand < 0.4) f.skillClass = new Soldier
    else if(rand < 0.8) f.skillClass = new Engineer
    else f.skillClass = new Soldier

    //Add armor
    rand = Math.random
    if(rand < 0.4) f.armor = Some(new CBRNArmor)
    else if(rand < 0.8) f.armor = Some(new N7Armor)
    else f.armor = None

    //Add weapons
    rand = Math.random
    if(rand < 0.8) f.weapons.add(new PredatorPistol)
    rand = Math.random
    if(rand < 0.3) f.weapons.add(new AvengerAssaultRifle)
    rand = Math.random
    if(rand < 0.2) f.weapons.add(new MantisSniperRifle)
    rand = Math.random
    if(rand < 0.3) f.weapons.add(new CrusaderShotgun)
    rand = Math.random
    if(rand < 0.3) f.weapons.add(new FragGrenade)
    rand = Math.random
    if(rand < 0.9) f.weapons.add(new OmniBlade)

    //Add powers
    //TODO add powers to random fighters

    //Select name
    rand = Math.random
    if(rand < 0.1) f.name = "Blue Suns Raider"
    else if(rand < 0.2) f.name = "Blue Suns Commander"
    else if(rand < 0.3) f.name = "Blue Suns Plunderer"
    else if(rand < 0.4) f.name = "Eclipse Hunter"
    else if(rand < 0.5) f.name = "Eclipse Heretic"
    else if(rand < 0.6) f.name = "Eclipse Demon"
    else if(rand < 0.8) f.name = "Blood Pack Initiate"
    else if(rand < 0.9) f.name = "Blood Pack Chieftan"
    else f.name = "Blood Pack Tank"

    f.instantiateFighter
    f
  }
}
class Fighter(level: Int) {
  protected var name: String = ""
  protected val expTracker: EXPTracker = EXPTracker.create(level)
  protected val statTracker = new StatTracker
  protected val powerTracker = new PowerTracker
  protected var skillClass: SkillClass = new Soldier
  protected val weapons = scala.collection.mutable.Set.empty[Weapon]
  protected var armor: Option[Armor] = None: Option[Armor]
  protected var location = Location(0, 0)
  //TODO add race
  instantiateFighter

  def instantiateFighter: Unit = {
    calculateStats
    setCurrentStatsToMax
  }

  def calculateStats: Unit = {
    //Change stats to the appropriate amounts based on level, skillClass, race, and powers
    statTracker.calculateStats(getLevel, skillClass, getPowers)
  }

  def setCurrentStatsToMax: Unit = {
    //Set the current stats of this fighter to their maxes
    statTracker.setCurrentStatsToMax
  }

  def getPowers: scala.collection.mutable.Set[Power] = powerTracker.getPowers

  def getLevel: Int = expTracker.getLevel

  def getLocation: Location = location

  def setLocation(loc: Location): Unit = location = loc

  def canMove: Boolean = this.statTracker.canFighterMove

  def canAttack: Boolean = this.statTracker.canFighterAttack

  def gainEXP(amount: Int): Unit = {
    this.expTracker.gainEXP(amount)
    if(this.expTracker.canLevelUp){
      this.expTracker.levelUp
      this.powerTracker.levelUp
      //StatTracker recalculates maximum stats
      calculateStats
    }
  }

  def takeDamage(amount: Int): Unit = {
    //If there are any shields remaining, send damage to shields.
    //If there are no shields remaining, send damage to health.
    if(this.armor.nonEmpty && this.armor.get.isShieldActive) this.armor.get.takeShieldDamage(amount)
    else this.statTracker.takeHpDamage(amount)
  }

  override def toString: String = {
    //TODO Fighter toString
    var s = name
    s += "(" + skillClass.toString + "): "
    s += "\t"
    s += "HP=" + statTracker.getHpCurrent + "/" + statTracker.getHpMax
    s += "\t"
    s += "E0=" + statTracker.getEezoCurrent + "/" + statTracker.getEezoMax
    s
  }
}
