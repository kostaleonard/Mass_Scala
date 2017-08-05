package views.objects

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import fighter.Fighter
import skillclasses.{Engineer, SkillClass, Soldier}
import views.View

/**
  * Created by Leonard on 8/5/2017.
  */
object FighterAvatar {
  val DEFAULT_HEIGHT = 150
  val DEFAULT_WIDTH = 150
  val SOLDIER_AVATAR = ImageIO.read(new File(View.getSourcePath("testFighter.png")))
    .getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  val ENGINEER_AVATAR = ImageIO.read(new File(View.getSourcePath("testFighter.png")))
    .getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)

  protected def getFriendlySoldierAvatar: Image = {
    val baseImage = SOLDIER_AVATAR
    //TODO add tints for friendly and enemy avatars
    ???
  }
}
class FighterAvatar(fighter: Fighter) {
  protected var height = FighterAvatar.DEFAULT_HEIGHT
  protected var width = FighterAvatar.DEFAULT_WIDTH

  def getHeight: Int = height

  def getWidth: Int = width

  def setHeight(h: Int): Unit = height = h

  def setWidth(w: Int): Unit = width = w

  protected def getBaseImage: Image = fighter.getSkillClass match {
    case soldier: Soldier => FighterAvatar.SOLDIER_AVATAR
    case engineer: Engineer => FighterAvatar.ENGINEER_AVATAR
    case _ => ???
  }

  //TODO tint the friendly image blue
  def getFriendlyImage: Image = getBaseImage

  //TODO tint the enemy image red
  def getEnemyImage: Image = getBaseImage
}
