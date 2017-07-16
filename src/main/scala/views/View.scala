package views

import actions.Action
import board.Location
import fighter.Fighter
import model.Model
import model.Model.RESOURCE_ROOT_DIRECTORY

/**
  * Created by Leonard on 6/3/2017.
  *
  * Any View and View subclasses should implement methods that return BufferedImage objects.
  * The ViewManager will be responsible for displaying these in a JFrame.
  *
  * NOTE: Use a 1600x900 (16:9) ratio for graphics.
  * Then use the following to adjust for screen size:
  *
  * https://gamedev.stackexchange.com/questions/83583/how-to-resize-my-2d-game-to-fit-the-screen
  * This resource states that you can render any graphics to a BufferedImage object,
  * which can be resized by calling the 10 argument drawImage method (which allows for scaling).
  *
  * https://gamedev.stackexchange.com/questions/45498/making-a-2d-game-with-responsive-resolution?rq=1
  * This resource gives a nice mathematical description of how to resize while preserving the aspect ratio.
  * So, rather than just resizing to fit the computer screen both vertically and horizontally,
  * you can preserve the 16:9 ratio. This will, however, create unused space in the background.
  */
object View{
  //Preserve a 16:9 aspect ratio in graphics.
  val FRAME_DESIGN_WIDTH = 1600
  val FRAME_DESIGN_HEIGHT = 900
  val IMAGES_DIRECTORY = "images"

  def getSourcePath(imageFilename: String): String = RESOURCE_ROOT_DIRECTORY + "/" + IMAGES_DIRECTORY + "/" + imageFilename
}
abstract class View(model: Model) {
  private var this.model: Model = model
}
