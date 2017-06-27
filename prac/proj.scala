object Main {
  def main(args: Array[String]): Unit = {
    val a: Int = 3

    {
      val b: Int = {
        val c: Int = a
        c
      }
      val a: Int = 4

      println(b)
    }
  }
}

