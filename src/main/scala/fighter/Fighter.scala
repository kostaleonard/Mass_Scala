package fighter

import armor.{Armor, CBRNArmor, N7Armor}
import skillclasses.{Engineer, SkillClass, Soldier}
import weapons._
import powers._
import board.{Board, Location, Tile}
import effects.{BioticInitiatorEffect, Effect}

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
    f.addWeapon(new PredatorPistol)
    rand = Math.random
    if(rand < 0.3) f.addWeapon(new AvengerAssaultRifle)
    rand = Math.random
    if(rand < 0.2) f.addWeapon(new MantisSniperRifle)
    rand = Math.random
    if(rand < 0.3) f.addWeapon(new CrusaderShotgun)
    rand = Math.random
    if(rand < 0.3) f.addWeapon(new FragGrenade)
    rand = Math.random
    if(rand < 0.9) f.addWeapon(new OmniBlade)

    //Add powers
    f.skillClass.getAvailablePowers.foreach(f.learnPower)
    f.getPowers.foreach(_.levelUp(Power.LVL_1))

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

    f.resetFighter
    f
  }
}
class Fighter(level: Int) {
  protected var name: String = ""
  protected val expTracker: EXPTracker = EXPTracker.create(level)
  protected val statTracker = new StatTracker
  protected val powerTracker = new PowerTracker
  protected val effectTracker = new EffectTracker
  protected var skillClass: SkillClass = new Soldier
  protected val weaponTracker = new WeaponTracker
  protected var armor: Option[Armor] = None: Option[Armor]
  protected var location = Location(0, 0)
  //TODO add race
  resetFighter

  def resetFighter: Unit = {
    setMaxStats
    setCurrentStatsToMax
    reloadAllWeapons
    discontinueAllSustainedPowers
    clearAllEffects
  }

  def reloadAllWeapons: Unit = weaponTracker.reloadAllWeapons

  def discontinueAllSustainedPowers: Unit = powerTracker.discontinueAllSustainedPowers(this)

  def setMaxStats: Unit = {
    //Change stats to the appropriate amounts based on level, skillClass, race, and powers
    //TODO add race
    statTracker.setMaxStats(getLevel, skillClass, getPowers)
    armor.map(_.setMaxStats(getPowers))
    weaponTracker.setMaxStats(getPowers)
  }

  def setCurrentStatsToMax: Unit = {
    //Set the current stats of this fighter to their maxes
    statTracker.setCurrentStatsToMax
    armor.map(_.setStatsToMax)
  }

  def getEezoCurrent: Int = statTracker.getEezoCurrent

  def loseEezo(amount: Int): Unit = statTracker.loseEezo(amount)

  def canUseEezo(amount: Int): Boolean = statTracker.canUseEezo(amount)

  def takeEezoRechargePenalty(amount: Int): Unit = statTracker.takeEezoRechargePenalty(amount)

  def removeEezoRechargePenalty(amount: Int): Unit = statTracker.removeEezoRechargePenalty(amount)

  def takeMovementPenalty(amount: Int): Unit = statTracker.takeMovementPenalty(amount)

  def removeMovementPenalty(amount: Int): Unit = statTracker.removeMovementPenalty(amount)

  def setEezoRecoveryRateCurrent(rate: Int): Unit = statTracker.setEezoRecoveryRateCurrent(rate)

  def getEezoRecoveryRateCurrent: Int = statTracker.getEezoRecoveryRateCurrent

  def getArmor: Option[Armor] = armor

  def getPowers: scala.collection.mutable.Set[Power] = powerTracker.getPowers

  def getSustainedPowersInUse: scala.collection.mutable.Set[SustainedPower] = powerTracker.getSustainedPowersInUse

  def learnPower(power: Power): Unit = powerTracker.learnPower(power)

  def getWeapons: scala.collection.mutable.Set[Weapon] = weaponTracker.getWeapons

  def getLevel: Int = expTracker.getLevel

  def getLocation: Location = location

  def setLocation(loc: Location): Unit = location = loc

  def addWeapon(weapon: Weapon): Boolean = weaponTracker.addWeapon(weapon)

  def removeWeapon(weapon: Weapon): Boolean = weaponTracker.removeWeapon(weapon)

  //TODO calculate the damage given to the Fighter that kills you (also better name?)
  def expGivenToVictoriousFighter: Int = 50

  def crowFliesDistance(other: Fighter): Int = {
    //Returns the distance as the crow flies from this to other.
    //Used to make sure that weapons are not firing outside of their designated range.
    //NOT used in movement calculations (by default--bonuses could change this).
    getLocation.crowFliesDistance(other.getLocation)
  }

  def canMove: Boolean = this.statTracker.getCanFighterMove

  def canAttack: Boolean = this.statTracker.getCanFighterAttack

  def canUsePower(power: Power): Boolean = power.canUse && (power match {
    case act: ActivatedPower => canUseEezo(act.getEezoCost)
    case sus: SustainedPower => if(sus.isInUse) true else canUseEezo(sus.getEezoCost)
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
    weaponTracker.reload(weapon)
    waitOneTurn
  }

  def useWeapon(weapon: Weapon, target: Fighter, board: Board): Unit = {
    weaponTracker.useWeapon(weapon, this, target, board)
    waitOneTurn
  }

  def useActivatedPower(power: ActivatedPower, targetOption: Option[Fighter], board: Board): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot use a power that is not in the inventory")
    power.usePower(this, targetOption, board)
    waitOneTurn
  }

  def useSustainedPower(power: SustainedPower): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot use a power that is not in the inventory")
    if(power.isInUse)
      throw new UnsupportedOperationException("Cannot use a power that is already in use")
    power.usePower(this)
    waitOneTurn
  }

  def discontinueSustainedPower(power: SustainedPower): Unit = {
    //Make sure power is in this Fighter's inventory
    if(!getPowers(power))
      throw new UnsupportedOperationException("Cannot discontinue a power that is not in the inventory")
    if(!power.isInUse)
      throw new UnsupportedOperationException("Cannot discontinue a power that is not in use")
    power.discontinuePower(this)
    waitOneTurn
  }

  def waitOneTurn: Unit = {
    setCanFighterAttack(false)
    setCanFighterMove(false)
  }

  def recoverMovesAndActions: Unit = {
    //Allow Fighters to recover moves and actions so that they can use them next turn.
    setCanFighterAttack(true)
    setCanFighterMove(true)
  }

  def gainEXP(amount: Int): Unit = {
    this.expTracker.gainEXP(amount)
    if(this.expTracker.canLevelUp){
      this.expTracker.levelUp
      this.powerTracker.levelUp
      //StatTracker recalculates maximum stats
      setMaxStats
    }
  }

  def takeDamage(amount: Int): Unit = {
    //If there are any shields remaining, send damage to shields.
    //If there are no shields remaining, send damage to health.
    if(this.armor.nonEmpty && this.armor.get.isShieldActive) this.armor.get.takeShieldDamage(amount)
    else this.statTracker.takeHpDamage(amount)
    delayShieldRecovery
  }

  def delayShieldRecovery: Unit = if(armor.nonEmpty) armor.get.delayShieldRecovery

  def getHpCurrent: Int = statTracker.getHpCurrent

  def getHpMax: Int = statTracker.getHpMax

  def isAlive: Boolean = statTracker.isAlive

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

  def doTurnlyActions: Unit = {
    //Allow Fighters to recover HP/shields, EEZO, and do any turnly effects.
    statTracker.doTurnlyActions
    if(armor.nonEmpty) armor.get.doTurnlyActions
    effectTracker.doTurnlyActions
  }

  def addEffect(effect: Effect): Boolean = effectTracker.addEffectDoAction(effect)

  def removeEffect(effect: Effect): Boolean = effectTracker.removeEffectDoAction(effect)

  def clearBioticInitiators: Unit = effectTracker.clearBioticInitiators

  def clearAllEffects: Unit = effectTracker.clearAllEffects

  def takeArmorRatingPenalty(amount: Int) = if(armor.nonEmpty) armor.get.takeArmorRatingPenalty(amount)

  def removeArmorRatingPenalty(amount: Int) = if(armor.nonEmpty) armor.get.removeArmorRatingPenalty(amount)

  def takeShieldRecoveryRatePenalty(amount: Int): Unit = if(armor.nonEmpty) armor.get.takeShieldRecoveryRatePenalty(amount)

  def removeShieldRecoveryRatePenalty(amount: Int): Unit = if(armor.nonEmpty) armor.get.removeShieldRecoveryRatePenalty(amount)

  def getActiveEffects: scala.collection.mutable.Set[Effect] = effectTracker.getActiveEffects

  def isBurned: Boolean = effectTracker.isBurned

  def isChilled: Boolean = effectTracker.isChilled

  def isBleeding: Boolean = effectTracker.isBleeding

  def isElectrocuted: Boolean = effectTracker.isElectrocuted

  def getActiveBioticInitiators: scala.collection.mutable.Set[BioticInitiatorEffect] = effectTracker.getActiveBioticInitiators

  def setActiveAmmoPower(ammoPower: Option[AmmoPower]): Unit = weaponTracker.setActiveAmmoPower(ammoPower)

  override def toString: String = {
    var s = name
    s += "(" + skillClass.toString + "): "
    s += "HP=" + statTracker.getHpCurrent + "/" + statTracker.getHpMax
    if(armor.nonEmpty) s += "(SH:" + armor.get.getShieldCurrent + "/" + armor.get.getShieldMax + ")"
    s += " "
    s += "E0=" + statTracker.getEezoCurrent + "/" + statTracker.getEezoMax
    val effects = getActiveEffects
    if(effects.nonEmpty) s += effects.mkString(" [", ",", "]")
    s
  }
}
