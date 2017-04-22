import scala.annotation._

object Main {
  def main(args: Array[String]): Unit = {
    def sum(f: Int => Int, a: Int, b: Int): Int = {
      @tailrec
      def nestedSum(acc: Int, a: Int): Int = {
        if (a <= b) nestedSum(acc + f(a), a + 1)
        else acc
      }

      nestedSum(0, a)
    }

    println(sum(x => x, 0, 10))

    def sumLinear = sum(x => x, _: Int, _: Int)
    println(sumLinear(0, 10))
    println((sum(x => x, _: Int, 10))(0))

    {
      def sum(f: Int => Int): (Int, Int) => Int = {
        def sumF(a: Int, b: Int): Int = 
          if (a <= b) f(a) + sumF(a + 1, b)
          else 0
        sumF
      }
      println(sum(x => x)(0, 10))
    }

    {
      def sum(f: Int => Int)(a: Int, b: Int): Int = 
        if (a <= b) f(a) + sum(f)(a + 1, b) else 0

      println(sum(x => x)(0, 10))

      val a = sum(x => x)(_: Int, _: Int)
      println(a(0, 10))

      val b = sum(_: Int => Int)(0, 10)
      println(b(x => x))
    }

    def foo(x: Int, y: Int, z: Int)(a: Int, b: Int): Int =
      x + y + z + a + b

    val f1 = (x: Int, z: Int, b: Int) => foo(x, 1, z)(2, b)
    val f2 = foo(_: Int, 1, _: Int)(2, _:Int)
    val f3 = (x: Int, z: Int) => (b: Int) => foo(x, 1, z)(2, b)
    
    println(f3(1, 2)(3))

    {
      def mapReduce(map: Int => Int)(reduce: (Int, Int) => Int, init: Int)(a: Int, b: Int): Int = {
        @tailrec
        def nested(acc: Int, n: Int): Int = {
          if (n <= b) nested(reduce(map(n), acc), n + 1)
          else acc
        }
        nested(init, a)
      }
      
      def sum = mapReduce(_: Int => Int)(_ + _, 0) _
      def product = mapReduce(_: Int => Int)(_ * _, 1) _

      println(sum(x => x)(1, 10))
      println(product(x => x)(1, 3))
    }
  }
}
