object Main {
  def main(args: Array[String]): Unit = {
    def and(x: Boolean, y: => Boolean): Boolean = 
      if (x) y else false

    def or(x: Boolean, y: => Boolean): Boolean =
      if (x) true else y

    def loop: Int = loop

    println(and(false, loop == 1))
    println(or(true, loop == 1))
  }
}
