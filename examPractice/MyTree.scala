object Main {
  def main(args: Array[String]): Unit = {
    class MyTree[A](val value: A, val left: Option[MyTree[A]],
                    val right: Option[MyTree[A]]) {
    }

    object MyTree {
      def apply[A](value: A, left: Option[MyTree[A]], right: Option[MyTree[A]]) {
        new MyTree[A](value, left, right)
      }
    }

    val a = MyTree(3, None, None)
  }
}
