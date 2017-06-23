import scala.language.higherKinds
import scala.language.implicitConversions

object Main {
  def main(args: Array[String]): Unit = {
    abstract class Dyn2[S[_, _], A] {
      type Data
      val d: Data
      val i: S[Data, A]
    }
    object Dyn2 {
      implicit def apply[S[_, _], D, A](dd: D)(implicit ii: S[D, A]): Dyn2[S, A] =
      new Dyn2[S, A] {
        type Data = D
        val d = dd
        val i = ii
      }
      implicit def methods[S[_, _], A](d: Dyn2[S, A]): S[d.Data, A] = d.i
    }
    abstract class Dyn[S[_]] {
      type Data
      val d: Data
      val i: S[Data]
    }
    object Dyn {
      implicit def apply[S[_], D](dd: D)(implicit ii: S[D]): Dyn[S] =
      new Dyn[S] {
        type Data = D
        val d = dd
        val i = ii
      }
      implicit def methods[S[_]](d: Dyn[S]): S[d.Data] = d.i
    }
    abstract class Iter[I, A] {
      def getValue(i: I): Option[A]
      def getNext(i: I): I
    }

    abstract class Iterable[R, A] {
      def iter(a: R): Dyn2[Iter, A]
      def get(a: A): A = a
    }

    implicit def listIter[A]: Iter[List[A], A] = new Iter[List[A], A] {
      def getValue(a: List[A]) = a.headOption
      def getNext(a: List[A]) = a.tail
    }

    implicit def iterIterable[I, A](implicit proxy: Iter[I, A]): Iterable[I, A] =
    new Iterable[I, A] { 
      def iter(a: I) = a
    }

    abstract class Report[R] {
      type A
      def it: Iterable[R, A]
      def get(a: A): A
    }

    val l = List(3, 1, 4)
    val b = iterIterable[List[Int], Int](listIter[Int])
    val a = b.iter(l)
    b.get(a.i.getValue(a.d).get)
    implicit def listReport = new Report[List[Int]] {
      type A = Int
      def it: Iterable[List[Int], A] = new Iterable[List[Int], A] {
        def iter(a: List[Int]) = a
      }
      def get(a: A): A = a
    }

    val e = Dyn(l)
    val cs = e.i.it.iter(e.d)
    e.i.get(cs.i.getValue(cs.d).get)

  }
}
