package views

import java.awt.{Color, Graphics2D}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import actions.Action
import board.Location
import fighter.Fighter
import model.Model

/**
  * Created by Leonard on 7/16/2017.
  */
class MainMenuView(model: Model) extends View(model) {
  def getMainMenuImage: BufferedImage = {
    //getTestImage1
    getTestImage2
  }
}
