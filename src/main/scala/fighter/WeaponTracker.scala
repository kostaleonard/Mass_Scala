package fighter

import board.Board
import powers._
import weapons.{Grenade, Gun, MeleeWeapon, Weapon}

/**
  * Created by Leonard on 7/10/2017.
  */
class WeaponTracker {
  protected val weapons = scala.collection.mutable.Set.empty[Weapon]

  def getWeapons: scala.collection.mutable.Set[Weapon] = weapons

  def addWeapon(weapon: Weapon): Boolean = weapons.add(weapon)

  def removeWeapon(weapon: Weapon): Boolean = weapons.remove(weapon)

  def reloadAllWeapons: Unit = {
    weapons.filter(_.isGun).foreach(weapon => weapon match {
      case g: Gun => if(g.canReload) g.reload
      case _ => ???
    })
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
      case _ => throw new UnsupportedOperationException("Unrecognized Weapon type.")
    }
  }

  def canUseWeapon(weapon: Weapon): Boolean = weapons(weapon) && (weapon match {
    case melee: MeleeWeapon => true //Can always use melee weapons
    case grenade: Grenade => grenade.getAmmunitionCurrent > 0
    case gun: Gun => gun.isLoaded
    case _ => throw new UnsupportedOperationException("Unrecognized Weapon type.")
  })

  def useWeapon(weapon: Weapon, attacker: Fighter, target: Fighter, board: Board): Unit = {
    //Make sure weapon is in this Fighter's inventory
    if(!weapons(weapon))
      throw new UnsupportedOperationException("Cannot attack with a weapon that is not in the inventory")
    weapon.doAttack(attacker, target, board)
  }

  def setActiveAmmoPower(ammoPower: Option[AmmoPower]): Unit = {
    //All Guns in the inventory will get the same AmmoPower.
    //However, a Gun may NOT get the same AmmoPower if it is added to the inventory ex post facto.
    //This might be the case if the Gun is found during the mission somehow.
    weapons.filter(_.isGun).foreach(weapon => weapon match {
      case g: Gun => g.setActiveAmmoPower(ammoPower)
      case _ => ???
    })
  }

  def setMaxStats(powers: scala.collection.mutable.Set[Power]): Unit = {
    def setMaxStatsByDefaults: Unit = {
      weapons.foreach(w => w.setBaseDamage(w.getDefaultWeaponDamage))
    }

    def setMaxStatsByPowers: Unit = {
      def addBonuses(power: Power): Unit = {
        def addBonus(bonus: Bonus): Unit = bonus match {
          case DoubleBonus(b1, b2) =>
            addBonus(b1)
            addBonus(b2)
          case WeaponDamagePercentBonus(amount) =>
            weapons.foreach(w => w.setBaseDamage((w.getBaseDamage * (1.0f + amount)).toInt))
          case MeleeDamagePercentBonus(amount) =>
            weapons.filter(_.isMelee).foreach(w => w.setBaseDamage((w.getBaseDamage * (1.0f + amount)).toInt))
          case GrenadeDamagePercentBonus(amount) =>
            weapons.filter(_.isGrenade).foreach(w => w.setBaseDamage((w.getBaseDamage * (1.0f + amount)).toInt))
          case GunDamagePercentBonus(amount) =>
            weapons.filter(_.isGun).foreach(w => w.setBaseDamage((w.getBaseDamage * (1.0f + amount)).toInt))
          case _ => ; //Do nothing--this Bonus is handled elsewhere
        }
        power.getBonuses.foreach(addBonus)
      }

      powers.foreach(pow => pow match{
        case pas: PassivePower => addBonuses(pas)
        case _ => ;
      }
      )
    }

    setMaxStatsByDefaults
    setMaxStatsByPowers
  }
}
