package fighter

import effects._

/**
  * Created by Leonard on 6/30/2017.
  */
class EffectTracker {
  protected val activeEffects = scala.collection.mutable.Set.empty[Effect]

  def getActiveEffects: scala.collection.mutable.Set[Effect] = activeEffects

  def addEffectDoAction(effect: Effect): Boolean = {
    if(!effect.canStack && activeEffects.exists(_.isSameEffect(effect)))
      throw new UnsupportedOperationException("Cannot add multiple instances of " + effect.toString)
    effect.doInitialAction
    activeEffects.add(effect)
  }

  def removeEffectDoAction(effect: Effect): Boolean = {
    effect.doRemovalAction
    activeEffects.remove(effect)
  }

  def clearBioticInitiators: Unit = {
    //Removes all active biotic initiators (presumably after a successful detonation).
    activeEffects --= getActiveBioticInitiators
  }

  def clearAllEffects: Unit = activeEffects.clear

  def isBurned: Boolean = activeEffects.exists(effect => effect match{
    case b: Burned => true
    case _ => false
  })

  def isChilled: Boolean = activeEffects.exists(effect => effect match{
    case c: Chilled => true
    case _ => false
  })

  def isBleeding: Boolean = activeEffects.exists(effect => effect match{
    case b: Bleeding => true
    case _ => false
  })

  def isElectrocuted: Boolean = activeEffects.exists(effect => effect match{
    case e: Electrocuted => true
    case _ => false
  })

  def getActiveBioticInitiators: scala.collection.mutable.Set[BioticInitiatorEffect] = {
    def isBioticInitiator(effect: Effect): Boolean = effect match {
      case initiator: BioticInitiatorEffect => true
      case _ => false
    }
    def asBioticInitiator(effect: Effect): BioticInitiatorEffect = effect match {
      case initiator: BioticInitiatorEffect => initiator
      case _ => throw new UnsupportedOperationException("Cannot convert generic Effect to BioticInitiatorEffect")
    }
    activeEffects
      .filter(isBioticInitiator)
      .map[BioticInitiatorEffect, scala.collection.mutable.Set[BioticInitiatorEffect]](asBioticInitiator)
  }

  def doTurnlyActions: Unit = {
    activeEffects.foreach{ effect =>
      effect.doTurnAction
      val newEffectOption = effect.decrementTurnsRemaining
      newEffectOption match{
        case Some(newEffect) =>
          activeEffects.remove(effect)
          activeEffects.add(newEffect)
        case None =>
          removeEffectDoAction(effect)
      }
    }
  }
}
