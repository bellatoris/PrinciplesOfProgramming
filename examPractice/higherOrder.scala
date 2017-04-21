import scala.annotation._


object Main {
  def main(args: Array[String]): Unit = {
    def sum(f: Int => Int, n: Int): Int = 
      if (n <= 0) 0 else f(n) + sum(f, n - 1)

    val f1 = sum(x => x, _: Int)
    println(f1(10))
    println((sum(x => x, _: Int))(10))
    println(sum(x => x, 10))
    println(sum(x => x * x, 10))

    println(((x: Int) => x * x)(100))

    def sumLinear(n: Int) = sum(x => x, n)

    def mapReduce(map: Int => Int, reduce: (Int, Int) => Int, init: Int, 
                  a: Int, b: Int): Int = {
      if (a <= b) reduce(map(b), mapReduce(map, reduce, init, a, b - 1))
      else init 
    }

    def mapReduce2(map: Int => Int, reduce: (Int, Int) => Int, init: Int,
                   a: Int, b: Int): Int = {
      @tailrec
      def nestedMapReduce(acc: Int, n: Int): Int = {
        if (n <= b) nestedMapReduce(reduce(acc, map(n)), n + 1)
        else acc
      }

      nestedMapReduce(init, a)
    }

    println(mapReduce(x => x, _ + _, 0, 0, 10))
    println(mapReduce2(x => x, _ + _, 0, 0, 10))
    println(mapReduce2(x => x, _ * _, 1, 1, 3))
  }
}
