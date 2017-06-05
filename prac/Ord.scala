object Main {
  def main(args: Array[String]): Unit = {
    trait Ord[A] {
      def cmp(that: Ord[A]): Int
      def getValue: A

      def ===(that: Ord[A]): Boolean = (this.cmp(that)) == 0
      def <(that: Ord[A]): Boolean = (this cmp that) < 0
      def >(that: Ord[A]): Boolean = (this cmp that) > 0
      def <=(that: Ord[A]): Boolean = (this cmp that) <= 0
      def >=(that: Ord[A]): Boolean = (this cmp that) >= 0
    }

    def max3[A](a: Ord[A], b: Ord[A], c: Ord[A]): Ord[A] = 
      if (a <= b) { if (b <= c) c else b }
      else { if (a <= c) c else a }

    class OInt(val getInt: Int) extends Ord[OInt] {
      def cmp(that: Ord[OInt]) = getInt.compare(that.getValue.getInt)
      def getValue = this
    }

    println(max3(new OInt(3), new OInt(2), new OInt(10)).getValue.getInt)

    class Bag[A <: Ord[A]] protected (val toList: List[A]) {
      def this() = this(Nil)
      def add(x: A): Bag[A] = {
        def go(elmts: List[A]): List[A] = elmts match {
          case Nil => x :: Nil
          case e :: _ if (x < e) => x :: elmts
          case e :: _ if (x === e) => elmts
          case e :: rest => e :: go(rest)
        }
        new Bag(go(toList))
      }
    }
    
    val emp = new Bag[OInt]()
    val b = emp.add(new OInt(3)).add(new OInt(2)).add(new OInt(10))
    println(b.toList.map((x) => x.getInt))
  }
}
