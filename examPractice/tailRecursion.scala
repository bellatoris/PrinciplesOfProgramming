import scala.annotation._ 


object Main {
  def main(args: Array[String]): Unit = {
    def sum(n: Int): Int = 
      if (n <= 0) 0 else n + sum(n - 1)

    def sum2(n: Int): Int = {
      @tailrec
      def nestedSum(idx: Int, acc: Int): Int = 
        if (idx > n) acc else nestedSum(idx + 1, acc + idx)
      nestedSum(0, 0)
    }
    
    def sum3(n: Int): Int = {
      @tailrec
      def nestedSum(idx: Int, acc: Int): Int =
        if (idx <= 0) acc else nestedSum(idx - 1, acc + idx)

      nestedSum(n, 0)
    }

    println(sum2(100000000)) 
    println(sum3(100000000))  
  }
}
