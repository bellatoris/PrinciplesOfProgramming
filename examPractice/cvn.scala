object Main {
  def main(args: Array[String]): Unit = {
    def loop: Int = loop
    def one(x: Int, y: => Int) = 1
    one(1 + 2, loop)
    //one(loop, 1 + 2) divergence

    val fun: (Boolean, => Int) => Int = (x, y) => if (x) y else 0
  }
}
