object Main {
  def main(args: Array[String]): Unit = {
    val a: Int => Int = x => x
    val b = {
      def __noname(x: Int) = x
      __noname _
    }

    { 
      val t = 0
      val f = {
        val t = 10
        def g(x: Int): Int = x + t
        g _ }
      println(f(20))
    }

    {
      def account(balance: Int): Int => Int = {
        val takeOut: Int => Int = (money: Int) => {
          balance - money
        }
        takeOut
      }

      val a = account(300)
      println(a(20))
      println(account(300)(20))
    }

    { 
      val t = 0 
      def f(x: => Int) = t + x
      val r = {
        val t = 10
        f(t * t) }
      println(r)
    }
  }
}
