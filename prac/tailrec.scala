import scala.annotation.tailrec
object Main {
  def main(args: Array[String]): Unit = {
    @tailrec
    def tail(n: Int): Int = {
      if (n < 0) n
      else tail(n - 1)
    }

    @tailrec
    def tail2(n: Int): Int = {
      if (tail2(n) < 0) n
      else tail2(n - 1)
    }
  }
}
