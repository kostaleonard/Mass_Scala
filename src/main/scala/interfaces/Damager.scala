package interfaces

import board.Board
import fighter.Fighter

/**
  * Created by Leonard on 6/22/2017.
  */
trait Damager {
  protected var baseDamage: Int = 0
  protected var accuracy: Float = 1.0f
  protected var damageMultiplierToShields = 1.0f
  protected var damageMultiplierToHp = 1.0f

  protected def accuracyCheck(attacker: Fighter, target: Fighter): Boolean = {
    //Returns true if the attack hit, based on accuracy.
    //Bonuses/Effects also accounted for here.
    //Subclasses may override.
    val check = math.random
    //TODO extra accuracy check logic goes here
    check < accuracy
  }

  def doAttack(attacker: Fighter, target: Fighter, board: Board): Unit = {
    //Default implementation may be overriden by subclasses for further functionality.
    //For example, subclasses may add effects to the attack, either to self or enemy.
    if(accuracyCheck(attacker, target)) {
      target.takeDamage(this.getAttackDamage(attacker, target, board))
      tryAddEffects(attacker, target, board)
      tryAreaOfEffect(attacker, target, board)
    }
  }

  protected def tryAddEffects(attacker: Fighter, target: Fighter, board: Board): Unit = {
    def tryAddBurner: Unit = this match{
      case burner: Burner => if(burner.burnCheck) burner.doBurn(attacker, target, board)
    }
    def tryAddElectrocuter: Unit = this match{
      case electrocuter: Electrocuter => if(electrocuter.electrocuteCheck) electrocuter.doElectrocute(attacker, target, board)
    }
    def tryAddFreezer: Unit = this match{
      case freezer: Freezer => if(freezer.chillCheck) freezer.doChill(attacker, target, board)
    }
    def tryAddBleeding: Unit = this match{
      case bleeder: Bleeder => if(bleeder.bleedCheck) bleeder.doBleed(attacker, target, board)
    }
    def tryAddBioticInitiator: Unit = this match{
      case bioticInitiator: BioticInitiator => bioticInitiator.doBioticInitiator(attacker, target, board)
    }

    tryAddBurner
    tryAddElectrocuter
    tryAddFreezer
    tryAddBleeding
    tryAddBioticInitiator
  }

  protected def tryAreaOfEffect(attacker: Fighter, target: Fighter, board: Board): Unit = this match{
    case areaOfEffect: AreaOfEffect => ??? //TODO add area of effect to damager
  }

  def getAttackDamage(attacker: Fighter, target: Fighter, board: Board): Int = {
    //Default implementation may be overriden by subclasses for further functionality.
    //Should take into account the bonuses/powers/skillclass/race of self and enemy.
    //Should also do appropriate damage based on whether or not shields are active.
    //Should also take into account armor rating.
    //If the target is not wearing any armor, this attack should do the base damage (plus Bonuses).
    //TODO update weapon attack damage as necessary
    var rollingDamageCalculation = 0

    //Armor
    target.getArmor match {
      case Some(armor) => rollingDamageCalculation = (baseDamage * (1.0f - armor.armorProtectionModifier)).toInt
      case None => rollingDamageCalculation = baseDamage
    }

    //Shield/HP modifier
    if(target.getArmor.nonEmpty && target.getArmor.get.getShieldCurrent > 0)
      rollingDamageCalculation = (rollingDamageCalculation * damageMultiplierToShields).toInt
    else
      rollingDamageCalculation = (rollingDamageCalculation * damageMultiplierToHp).toInt

    //Tile modifiers
    rollingDamageCalculation =
      (rollingDamageCalculation * board.getTiles(attacker.getLocation.row)(attacker.getLocation.col).getAttackModifier).toInt
    rollingDamageCalculation =
      (rollingDamageCalculation / board.getTiles(target.getLocation.row)(target.getLocation.col).getDefenseModifier).toInt

    //Interfaces
    def tryBurnFreezeCombo: Unit = this match {
      case burner: Burner =>
        if(target.isChilled) rollingDamageCalculation = (rollingDamageCalculation * burner.getFrozenTargetDamageBonus).toInt
    }
    def tryElectrocuteExplosionCombo: Unit = this match {
      case electrocuter: Electrocuter =>
        if(target.isBurned) rollingDamageCalculation += electrocuter.getTechExplosionDamage
    }
    def tryBioticDetonation: Unit = this match {
      case detonator: BioticDetonator => if(detonator.comboCheck(target)){
        rollingDamageCalculation += detonator.getBioticDetonationDamage(target)
        target.clearBioticInitiators
      }
    }

    tryBurnFreezeCombo
    tryElectrocuteExplosionCombo
    tryBioticDetonation

    //Bonuses
    //TODO update weapon attack damage with bonuses

    rollingDamageCalculation max 1
  }
}
