package fighter

import effects.Effect

/**
  * Created by Leonard on 6/30/2017.
  */
class EffectTracker {
  protected val activeEffects = scala.collection.mutable.Set.empty[Effect]

  def getActiveEffects: scala.collection.mutable.Set[Effect] = activeEffects

  def addEffect(effect: Effect): Boolean = activeEffects.add(effect)

  def removeEffect(effect: Effect): Boolean = activeEffects.remove(effect)
}
