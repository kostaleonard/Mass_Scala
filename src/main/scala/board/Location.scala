package board

/**
  * Created by Leonard on 6/4/2017.
  */
case class Location(row: Int, col: Int) {
  override def equals(obj: scala.Any): Boolean = {
    obj match{
      case loc: Location => this.row == loc.row && this.col == loc.col
      case _ => super.equals(obj)
    }
  }

  def inBounds(array: Array[Array[Tile]]): Boolean = {
    row >= 0 && row < array.length && col >= 0 && col < array(row).length
  }
}
