object Main {
  def main(args: Array[String]): Unit = {
    def mapReduce(reduce: (Int, Int) => Int, init: Int)(map: Int => Int)(a: Int, b: Int): Int = {
      if (a <= b) mapReduce(reduce, init)(map)(a+1, b)
      else init
    }  

    def sum = mapReduce(_ + _, 0) _
    val product = mapReduce(_ * _, 1) _

  }
}
