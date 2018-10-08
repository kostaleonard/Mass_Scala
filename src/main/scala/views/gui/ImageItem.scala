package views.gui

import java.awt.image.BufferedImage

/**
  * Created by Leonard on 10/8/2018.
  */
case class ImageItem(image: BufferedImage, width: Int, height: Int, guiAction: GuiAction, isSelectable: Boolean = true)
