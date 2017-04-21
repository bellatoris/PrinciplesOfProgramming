object Main {
  def main(args: Array[String]): Unit = {
    /* raise error, a is value, the comiper want to evaluate b.
    val a = b
    val b = 2
    */

    def a = b    // call-by-name, it's not evalue right now.
    println(a)
    def b = 2

    def c: Int = d
    def d: Int = c

    /* raise error, e is value, the compiler want to evaluate f
    val e = f
    def f = 2
    */

    /* raise error, why?
    def g = h
    val h = 4 
    */
    {
      val t = 0
      def square(x: Int): Int = t + x * x
      val x = square(5)

      val r = {
        val t = 10
        val s = square(5)
        t + x // 35
      }
      val y = t + r // 35
      println(y)
    }

    {
      val t = 0
      def f(x: Int) = t + g(x)
      def g(x: Int) = x * x
      val x = f(5) // 25
      val r = {
        val t = 10
        val s = f(5) // 25
        t + s // 35
      }
      val y = t + r // 35
      println(y)
    }   
  }

  val hi = 3; val hi2 = 4; val hi4 = 5
}

