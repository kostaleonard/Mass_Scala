package views

import board.{Tile,GrassPlains, Mountains, Location}
import model.Model

/**
  * Created by Leonard on 6/3/2017.
  */
class PrintView(model: Model) extends View(model) {

  override def showStartScreen: Unit = {
    System.out.println("======== MASS ========")
    System.out.println("Press any key to begin")
  }

  override def showMainMenu: Unit = {
    System.out.println("======== Main Menu ========")
    System.out.println("c: Customize party")
    System.out.println("r: Play random board")
  }

  override def showBoard: Unit = {
    def showTile(t: Tile): Unit = {
      t match{
        case g: GrassPlains => System.out.print('_')
        case m: Mountains => System.out.print('^')
        case _ => System.out.print('?')
      }
    }
    def showFighter(c: Char): Unit = {
      System.out.print(c)
    }
    System.out.println("======== Board ========")
    model.getCurrentBoard match {
      case Some(board) =>
        /*
        //Do not show fighters:
        board.getTiles.foreach{
          row => row.foreach(showTile(_))
          System.out.println
        }
        */
        //Show fighters:
        val playerFighterLocs = board.getPlayerPartyLocationsMap
        val enemyFighterLocs = board.getEnemyPartyLocationsMap
        for(r <- board.getTiles.indices){
          for(c <- board.getTiles(r).indices){
            //Check to see if there is a Fighter in this cell
            if(playerFighterLocs.contains(Location(r, c))) showFighter('P')
            else if(enemyFighterLocs.contains(Location(r, c))) showFighter('E')
            else showTile(board.getTiles(r)(c))
          }
          System.out.println
        }
      case None => ;
    }
  }

  override def showPlayerParty: Unit = {
    System.out.println("======== Party ========")
    val party = model.getPlayerParty
    for(fighter <- party.getFighters){
      System.out.println(fighter.toString)
    }
  }

  override def showBoardCommands: Unit = {
    System.out.println("======== Commands ========")
    System.out.println("q: Quit")
    System.out.println("e: End turn")
    System.out.println("m: Move fighter")
    System.out.println("a: Attack")
  }

  override def render: Unit = {
    //TODO implement the views.PrintView class render function.
    System.out.println("Rendering to stdout...")
  }
}