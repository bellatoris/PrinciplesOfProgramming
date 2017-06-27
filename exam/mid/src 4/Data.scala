package pp201701.mid4.Data

object DataBundle {
  sealed abstract class IList
  case class INil() extends IList
  case class ICons(hd: Int, tl: IList) extends IList
}
