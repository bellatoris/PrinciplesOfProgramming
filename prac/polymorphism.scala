object Main {
  def main(args: Array[String]): Unit = {
    /*
    def id[A](x: A) = x

    id(3)
    id("abc")

    def applyn[A](f: A => A, n: Int, x: A): A = 
      n match {
        case 0 => x
        case _ => f(applyn(f, n - 1, x))
      }

    println(applyn((x: Int) => x + 1, 100, 3))
    println(applyn((x: String) => x + "!", 10, "gil"))
    println(applyn(id[String], 10, "hur"))

    def foo[A, B](f: A => A, x: (A, B)): (A, B) = 
      (applyn[A](f, 10, x._1), x._2)

    foo[String, Int]((x: String) => x + "!", ("abc", 10))
    */

    type Applyn = { def apply[A](f: A => A, n: Int, x: A): A }

    object applyn {
      def apply[A](f: A => A, n: Int, x: A): A = 
        n match {
          case 0 => x
          case _ => f(apply(f, n - 1, x))
        }
    }
  
    println(applyn((x: String) => x + "!", 10, "gil"))

    def foo(f: Applyn): String = {
      val a: String = f[String]((x: String) => x + "!", 10, "gil")
      val b: Int = f[Int]((x: Int) => x + 2, 10, 5)
      a + b.toString()
    }

    println(foo(applyn))
  }
}
