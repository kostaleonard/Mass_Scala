package fighter

import armor.{Armor, CBRNArmor, N7Armor}
import skillclasses.{Engineer, SkillClass, Soldier}
import weapons._
import powers.{ActivatedPower, PassivePower, Power, SustainedPower}
import board.{Board, Location, Tile}

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
    f.weapons.add(new PredatorPistol)
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

  def setEezoRecoveryRateCurrent(rate: Int): Unit = statTracker.setEezoRecoveryRateCurrent(rate)

  def getEezoRecoveryRateCurrent: Int = statTracker.getEezoRecoveryRateCurrent

  def getArmor: Option[Armor] = armor

  def getPowers: scala.collection.mutable.Set[Power] = powerTracker.getPowers

  def getWeapons: scala.collection.mutable.Set[Weapon] = weapons

  def getLevel: Int = expTracker.getLevel

  def getLocation: Location = location

  def setLocation(loc: Location): Unit = location = loc

  def crowFliesDistance(other: Fighter): Int = {
    //Returns the distance as the crow flies from this to other.
    //Used to make sure that weapons are not firing outside of their designated range.
    //NOT used in movement calculations (by default--bonuses could change this).
    getLocation.crowFliesDistance(other.getLocation)
  }

  def canMove: Boolean = this.statTracker.getCanFighterMove

  def canAttack: Boolean = this.statTracker.getCanFighterAttack

  def canUsePower(power: Power): Boolean = power.getLevel > 0 && (power match {
    case act: ActivatedPower => statTracker.canUseEezo(act.getEezoCost)
    case sus: SustainedPower => if(sus.isInUse) true else statTracker.canUseEezo(sus.getEezoCost)
    case pas: PassivePower => false
    case _ => throw new UnsupportedOperationException("Unrecognized Power type.")
  })

  def canUseWeapon(weapon: Weapon): Boolean = weapon match {
    case melee: MeleeWeapon => true //Can always use melee weapons
    case grenade: Grenade => grenade.getAmmunitionCurrent > 0
    case gun: Gun => gun.isLoaded
    case _ => throw new UnsupportedOperationException("Unrecognized Weapon type.")
  }

  def reload(weapon: Weapon): Unit = {
    //Make sure weapon is in this Fighter's inventory
    if(!weapons(weapon))
      throw new UnsupportedOperationException("Cannot attack with a weapon that is not in the inventory")
    weapon match {
      case melee: MeleeWeapon =>
        throw new UnsupportedOperationException("Cannot reload a melee weapon")
      case grenade: Grenade =>
        throw new UnsupportedOperationException("Cannot reload a grenade")
      case gun: Gun =>
        if(!gun.canReload) throw new UnsupportedOperationException("Cannot reload a fully loaded gun")
        gun.reload
        setCanFighterAttack(false)
      case _ => throw new UnsupportedOperationException("Unrecognized Weapon type.")
    }
  }

  def useWeapon(weapon: Weapon, target: Fighter, board: Board): Unit = {
    //Make sure weapon is in this Fighter's inventory
    if(!weapons(weapon))
      throw new UnsupportedOperationException("Cannot attack with a weapon that is not in the inventory")
    weapon.doAttack(this, target, board)
    setCanFighterAttack(false)
  }

  def useActivatedPower(power: ActivatedPower, targetOption: Option[Fighter], board: Board): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot use a power that is not in the inventory")
    power.usePower(this, targetOption, board)
    setCanFighterAttack(false)
  }

  def useSustainedPower(power: SustainedPower): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot use a power that is not in the inventory")
    if(power.isInUse)
      throw new UnsupportedOperationException("Cannot use a power that is already in use")
    power.usePower(this)
    setCanFighterAttack(false)
  }

  def discontinueSustainedPower(power: SustainedPower): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot discontinue a power that is not in the inventory")
    if(!power.isInUse)
      throw new UnsupportedOperationException("Cannot discontinue a power that is not in use")
    power.discontinuePower(this)
    setCanFighterAttack(false)
  }

  def waitOneTurn: Unit = setCanFighterAttack(false)

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

  def getMovementCurrent: Int = statTracker.getMovementCurrent

  def getMovementMax: Int = statTracker.getMovementMax

  def getCrossableTiles: scala.collection.mutable.Set[Class[_ <: Tile]] = statTracker.getCrossableTiles

  def canCross(tile: Class[_ <: Tile]): Boolean = statTracker.canCross(tile)

  def setCanFighterMove(b: Boolean): Unit = statTracker.setCanFighterMove(b)

  def setCanFighterAttack(b: Boolean): Unit = statTracker.setCanFighterAttack(b)

  def moveTo(loc: Location): Unit = {
    setLocation(loc)
    setCanFighterMove(false)
  }

  override def toString: String = {
    //TODO Fighter toString
    var s = name
    s += "(" + skillClass.toString + "): "
    s += "HP=" + statTracker.getHpCurrent + "/" + statTracker.getHpMax
    if(armor.nonEmpty) s += "(SH:" + armor.get.getShieldCurrent + "/" + armor.get.getShieldMax + ")"
    s += "\t"
    s += "E0=" + statTracker.getEezoCurrent + "/" + statTracker.getEezoMax
    s
  }
}
