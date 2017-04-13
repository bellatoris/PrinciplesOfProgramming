object Main {
  def main(args: Array[String]): Unit = {
    class foo_type(x: Int, y: Int) {
      val a: Int = x
      def b: Int = a + y
      def g(k: Int) = k
      def f(z: Int): Int = b + y + z + g(z)
    }

    class gee_type(x: Int) extends foo_type(x + 1, x + 2) {
      // override def f(z: Int): Int = b + z
      // or, override def f(z: Int) = super.f(z) * 2

      // This is overloading
      def f(z: Any): Int = 77 
    }

    class gee_type2(x: Int) extends foo_type(x + 1, x + 2) {
        val c = 53;
        // override def f(z: Int): Int = c;
        override def g(k: Int) = k + 100
    }

    def test(f: foo_type) = println(f.f(30))
    test(new foo_type(31, 32))
    test(new gee_type(30))
    test(new gee_type2(30))

    println(new gee_type(30).f("hi"))
    println(new gee_type2(0).f(0))
  }
}
