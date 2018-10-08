package views.gui

import java.awt.{Color, Font, Graphics2D}
import java.awt.image.BufferedImage

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Leonard on 10/8/2018.
  */
object ImageMenu{
  val DEFAULT_WIDTH = 300
  val DEFAULT_HEIGHT = 300
  val DEFAULT_TITLE_FONT = new Font(Font.MONOSPACED, Font.BOLD, 50)
  val DEFAULT_TITLE_FONT_COLOR = Color.BLACK
  val DEFAULT_MENUITEM_FONT = new Font(Font.MONOSPACED, Font.BOLD, 30)
  val DEFAULT_MENUITEM_FONT_COLOR = Color.BLACK
  val DEFAULT_MENU_BACKGROUND_COLOR = new Color(100, 100, 100, 200) //Grayish and slightly transparent
  val DEFAULT_HIGHLIGHT_COLOR = Color.YELLOW
  val DEFAULT_ACCENT_COLOR = Color.BLUE.brighter()
  val DEFAULT_BORDER_COLOR = Color.BLACK
  val DEFAULT_BORDER_THICKNESS = 3
  val DEFAULT_TITLE_SEPARATOR_THICKNESS = 2
  val DEFAULT_NONSELECTABLE_MENUITEM_COLOR = Color.GRAY.brighter()
}
class ImageMenu extends Menu[ImageItem] {
  override protected val menuItems = ArrayBuffer.empty[ImageItem]
  width = Menu.DEFAULT_WIDTH
  height = Menu.DEFAULT_HEIGHT
  protected var titleFont = ImageMenu.DEFAULT_TITLE_FONT
  protected var titleFontColor = BasicMenu.DEFAULT_TITLE_FONT_COLOR
  protected var menuItemFont = BasicMenu.DEFAULT_MENUITEM_FONT
  protected var menuItemFontColor = BasicMenu.DEFAULT_MENUITEM_FONT_COLOR
  protected var menuBackgroundColor = BasicMenu.DEFAULT_MENU_BACKGROUND_COLOR
  protected var highlightColor = BasicMenu.DEFAULT_HIGHLIGHT_COLOR
  protected var accentColor = BasicMenu.DEFAULT_ACCENT_COLOR
  protected var borderColor = BasicMenu.DEFAULT_BORDER_COLOR
  protected var borderThickness = BasicMenu.DEFAULT_BORDER_THICKNESS
  protected var titleSeparatorThickness = BasicMenu.DEFAULT_TITLE_SEPARATOR_THICKNESS
  protected var nonSelectableMenuItemColor = BasicMenu.DEFAULT_NONSELECTABLE_MENUITEM_COLOR
  //protected var titleDisplayed = true
  protected var wrapContentHeight = true //Will supersede this.height
  protected var wrapContentWidth = false //Will supersede this.width
  protected var titleString = "MENU"

  def setTitleString(title: String): Unit = titleString = title

  override def getWidth: Int = if(wrapContentWidth) getWrappedWidth else width

  override def getHeight: Int = if(wrapContentHeight) getWrappedHeight else height

  def setWrapContentHeight(b: Boolean): Unit = wrapContentHeight = b

  def setWrapContentWidth(b: Boolean): Unit = wrapContentWidth = b

  def getWrappedWidth: Int = {
    val buffer = borderThickness * 4
    val g2d = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).getGraphics.asInstanceOf[Graphics2D]
    val titleStringWidth = g2d.getFontMetrics(titleFont).stringWidth(titleString)
    val longestStringWidth = if(menuItems.isEmpty) 0 else menuItems.map(_.width).max
    (titleStringWidth max longestStringWidth) + buffer
  }

  def getWrappedHeight: Int = {
    val buffer = 0 //borderThickness * 2
    val g2d = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).getGraphics.asInstanceOf[Graphics2D]
    val titleStringHeight = g2d.getFontMetrics(titleFont).getHeight
    val menuItemHeight = menuItems.map(_.height).sum
    titleStringHeight + menuItemHeight + buffer
  }

  override def getImage: BufferedImage = {
    val bufferedImage = new BufferedImage(getWidth, getHeight, BufferedImage.TYPE_INT_RGB)
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.setColor(borderColor)
    g2d.fillRect(0, 0, getWidth, getHeight)
    g2d.setColor(menuBackgroundColor)
    g2d.fillRect(borderThickness, borderThickness, getWidth - 2 * borderThickness, getHeight - 2 * borderThickness)
    g2d.setColor(titleFontColor)
    g2d.setFont(titleFont)
    val titleHeight = g2d.getFontMetrics(titleFont).getHeight
    g2d.drawString(titleString, borderThickness * 2, (titleHeight * 3) / 4)
    g2d.setColor(borderColor)
    g2d.fillRect(0, titleHeight, getWidth, titleSeparatorThickness)
    val heightStartMenuItems = titleHeight
    g2d.setFont(menuItemFont)
    var heightStartThisMenuItem = heightStartMenuItems
    menuItems.indices.foreach{ i =>
      val imageItem = menuItems(i)
      if(selectedMenuItem == i && isActive){
        g2d.setColor(highlightColor)
        if(i == 0)
          g2d.fillRect(borderThickness, heightStartThisMenuItem + borderThickness, getWidth - 2 * borderThickness, imageItem.height - borderThickness)
        else if(i == menuItems.length - 1)
          g2d.fillRect(borderThickness, heightStartThisMenuItem, getWidth - 2 * borderThickness, imageItem.height - borderThickness)
        else
          g2d.fillRect(borderThickness, heightStartThisMenuItem, getWidth - 2 * borderThickness, imageItem.height)
      }
      if(imageItem.isSelectable) g2d.setColor(menuItemFontColor)
      else g2d.setColor(nonSelectableMenuItemColor)
      //g2d.drawString(menuItem.text, borderThickness * 2, heightStartThisMenuItem + (menuItemHeight * 3) / 4)
      g2d.drawImage(imageItem.image, borderThickness * 2, heightStartThisMenuItem, imageItem.width, imageItem.height, null)
      heightStartThisMenuItem += imageItem.height
    }
    g2d.dispose()
    bufferedImage
  }
}
