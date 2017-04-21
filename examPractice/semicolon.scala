object Main {
  def main(args: Array[String]): Unit = {
    def square(x: Int): Int = x * x

    {
      val r = {
        val t = 10; val s = square(5); t +
        s }
      println(r)
    }

    /*
    val r = {
      val t = 10; val s = square(5); t 
      + s }
    */
    {
      val r = {
        val t = 10; val s = square(5); (t +
        s) }
    }
  }
}
