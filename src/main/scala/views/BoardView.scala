package views

import java.awt.{Color, Font, Graphics2D}
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import actions.Action
import board.Location
import controller.KeyMappings
import fighter.Fighter
import model.Model
import views.objects.BoardPainter

/**
  * Created by Leonard on 7/29/2017.
  */
class BoardView(model: Model) extends View(model) {
  if(model.getCurrentBoard.isEmpty) throw new UnsupportedOperationException("Cannot draw empty board.")
  protected val bufferedImage = new BufferedImage(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  protected val backgroundImage = ImageIO.read(new File(View.getSourcePath("boardBackground.jpg")))
    .getScaledInstance(View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, BufferedImage.TYPE_INT_RGB)
  protected val boardPainter = new BoardPainter(model.getCurrentBoard.get)
  protected var boardOffset_X = 100
  protected var boardOffset_Y = 50
  //TODO the cursor should probably start over one of the players.
  protected var cursorLoc = Location(0, 0)
  protected var selectedLocOpt: Option[Location] = None
  protected var moveLocations = scala.collection.immutable.Set.empty[Location]
  protected var actionLocationsMap = scala.collection.immutable.Map.empty[Location, scala.collection.immutable.Set[Action]]

  setupBoardPainter

  def setupBoardPainter: Unit = {
    boardPainter.setCursorLocOpt(Some(cursorLoc))
  }

  override def getImage: BufferedImage = getBoardImage

  def getBoardImage: BufferedImage = {
    val g2d = bufferedImage.getGraphics.asInstanceOf[Graphics2D]
    g2d.drawImage(backgroundImage, 0, 0, View.FRAME_DESIGN_WIDTH, View.FRAME_DESIGN_HEIGHT, null)
    val boardImage = boardPainter.getImage
    g2d.drawImage(boardImage, boardOffset_X, boardOffset_Y, boardImage.getWidth, boardImage.getHeight, null)
    g2d.dispose()
    bufferedImage
  }

  def moveCursorToLocation(loc: Location): Unit = {
    if(loc.inBounds(model.getCurrentBoard.get.getTiles)){
      cursorLoc = loc
      boardPainter.setCursorLocOpt(Some(cursorLoc))
    }
  }

  def setSelectedLocationOpt(opt: Option[Location]): Unit = {
    selectedLocOpt = opt
    boardPainter.setSelectedLocOpt(selectedLocOpt)
  }

  def setMoveLocations(locations: Set[Location]): Unit = {
    moveLocations = locations
    boardPainter.setMoveLocations(moveLocations)
  }

  def setActionLocationsMap(map: Map[Location, Set[Action]]): Unit = {
    actionLocationsMap = map
    boardPainter.setActionLocationsMap(actionLocationsMap)
  }

  def getSelectedFighterOpt: Option[Fighter] = selectedLocOpt match{
    case Some(loc) => model.getCurrentBoard.get.fighterAt(loc)
    case None => None
  }

  def getCursorLoc: Location = cursorLoc

  def getActionLocationMap: Map[Location, Set[Action]] = actionLocationsMap

  def cursorSelect: Unit = {
    val board = model.getCurrentBoard.get
    selectedLocOpt match{
      case Some(selectedLoc) =>
        if(moveLocations(cursorLoc)){
          board.moveFighterTo(board.fighterAt(selectedLoc).get, cursorLoc)
          cursorDeselect
          cursorSelect
        }
        else if(actionLocationsMap.contains(cursorLoc)){
          setNextView(Some(new BoardActionMenuView(model, this)))
        }
        else{
          cursorDeselect
          cursorSelect
        }
      case None =>
        setSelectedLocationOpt(Some(cursorLoc))
        board.fighterAt(cursorLoc).map{ fighter =>
          setMoveLocations(board.availableMoveLocations(fighter))
          setActionLocationsMap(board.availableActionsLocationMap(fighter))
        }
    }
  }

  def cursorDeselect: Unit = {
    setSelectedLocationOpt(None)
    setMoveLocations(Set.empty[Location])
    setActionLocationsMap(Map.empty[Location, Set[Action]])
  }

  override def keyPressed(keyCode: Int): Unit = keyCode match{
    case KeyMappings.A_KEY => cursorSelect
    case KeyMappings.B_KEY => cursorDeselect
    case KeyMappings.UP_KEY => moveCursorToLocation(cursorLoc.copy(row = cursorLoc.row - 1))
    case KeyMappings.DOWN_KEY => moveCursorToLocation(cursorLoc.copy(row = cursorLoc.row + 1))
    case KeyMappings.LEFT_KEY => moveCursorToLocation(cursorLoc.copy(col = cursorLoc.col - 1))
    case KeyMappings.RIGHT_KEY => moveCursorToLocation(cursorLoc.copy(col = cursorLoc.col + 1))
    case _ => ;
  }

  override def keyReleased(keyCode: Int): Unit = {}

  override def keyHeld(keyCode: Int): Unit = keyCode match {
    case KeyMappings.UP_KEY => moveCursorToLocation(cursorLoc.copy(row = cursorLoc.row - 1))
    case KeyMappings.DOWN_KEY => moveCursorToLocation(cursorLoc.copy(row = cursorLoc.row + 1))
    case KeyMappings.LEFT_KEY => moveCursorToLocation(cursorLoc.copy(col = cursorLoc.col - 1))
    case KeyMappings.RIGHT_KEY => moveCursorToLocation(cursorLoc.copy(col = cursorLoc.col + 1))
    case _ => ;
  }
}
