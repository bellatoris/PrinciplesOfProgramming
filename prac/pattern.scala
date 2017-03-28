object Main {
  def main(args: Array[String]): Unit = {
    def secondElmt(xs: List[Int]): Option[Int] = xs match {
      case Nil | x :: Nil => None   // doesn't work I need to ask about this
      case _ :: (x :: _) => Some(x)
    }

    val a = List(1, 2, 3, 4, 5)
    println(secondElmt(a))
  }
}
