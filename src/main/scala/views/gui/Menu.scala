package views.gui

import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Leonard on 10/8/2018.
  */
object Menu{
  val DEFAULT_WIDTH = 300
  val DEFAULT_HEIGHT = 300
}
abstract class Menu[A<:MenuItem] {
  protected val menuItems = ArrayBuffer.empty[A]
  protected var width = Menu.DEFAULT_WIDTH
  protected var height = Menu.DEFAULT_HEIGHT
  protected var selectedMenuItem = 0
  protected var isActive = true
  //protected var isVisible = true

  def getMenuItems: ArrayBuffer[A] = menuItems

  def appendMenuItem(menuItem: A): Unit = menuItems.append(menuItem)

  def removeMenuItem(index: Int): A = menuItems.remove(index)

  def getWidth: Int = width

  def getHeight: Int = height

  def getImage: BufferedImage

  def makeSelection: Unit = if(isActive) menuItems(selectedMenuItem).guiAction.functionToCall()

  def scrollDown: Unit = if(isActive && selectedMenuItem < menuItems.length - 1){
    val nextSelectableIndex = menuItems.indices.find{ i => i > selectedMenuItem && menuItems(i).isSelectable }
    nextSelectableIndex.map(index => selectedMenuItem = index)
  }

  def scrollUp: Unit = if(isActive && selectedMenuItem > 0){
    val nextSelectableIndex = menuItems.indices.reverse.find{ i => i < selectedMenuItem && menuItems(i).isSelectable }
    nextSelectableIndex.map(index => selectedMenuItem = index)
  }
}
