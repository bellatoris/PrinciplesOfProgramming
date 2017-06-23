import scala.language.higherKinds
import scala.language.implicitConversions

object Main {
  def main(args: Array[String]): Unit = {
    abstract class Iter[I, A] {
      def getValue(i: I): Option[A]
      def getNext(i: I): I
    }

    def sumElements[I](xs: I)(implicit proxy: Iter[I, Int]): Int = proxy.getValue(xs) match {
      case None => 0
      case Some(n) => n + sumElements(proxy.getNext(xs))
    }

    def printElements[I, A](xs: I)(implicit proxy: Iter[I, A]): Unit = proxy.getValue(xs) match {
      case None =>
      case Some(n) => {
        println(n)
        printElements(proxy.getNext(xs))
      }
    }

    implicit def listIter[A]: Iter[List[A], A] = new Iter[List[A], A] {
      def getValue(a: List[A]) = a.headOption
      def getNext(a: List[A]) = a.tail
    }
  
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
      implicit def methods[S[_, _], A](d: Dyn2[S, A]): S[d.Data, A] = {
        d.i
      }
    }

    def incIter(max: Int): Iter[Int, Int] = new Iter[Int, Int] {
      def getValue(i: Int) = if (i <= max) Some(i) else None
      def getNext(i: Int) = i + 1
    }

    def getMyIter(isInc: Boolean): Dyn2[Iter, Int] = {
      if (isInc) Dyn2(0)(incIter(10))
      else List(3, 1, 4)
    }

    
    val i1 = getMyIter(true)
    printElements(i1.d)
    // i1.getValue(i1.getNext(i1.d)) // implicitly conversion

    /*
    val i2 = getMyIter(false)
    printElements(i2.d)(i2.i)
    i2.getValue(i2.getNext(i2.d))
    */

    abstract class Dyn[S[_]] {
      type Data
      val d: Data
      val i: S[Data]
    }

    object Dyn {
      implicit def apply[D, S[_]](dd: D)(implicit ii: S[D]): Dyn[S] = new Dyn[S] {
        type Data = D
        val d = dd
        val i = ii
      }
      implicit def methods[S[_]](d: Dyn[S]): S[d.Data] = d.i
    }

    abstract class Iterable[R, A] {
      def iter(a: R): Dyn2[Iter, A]
    }

    def sumElements2[R](xs: R)(implicit proxy: Iterable[R, Int]) = {
      val cs = proxy.iter(xs)
      sumElements(cs.d)(cs.i)
    }

    def printElements2[R, A](xs: R)(implicit proxy: Iterable[R, A]) = {
      val cs = proxy.iter(xs)
      printElements(cs.d)(cs.i)
    }

    sealed abstract class MyTree[A] 
    case class Empty[A]() extends MyTree[A]
    case class Node[A](v: A, left: MyTree[A], right: MyTree[A]) extends MyTree[A]

    implicit def treeIterable[A]: Iterable[MyTree[A], A] = new Iterable[MyTree[A], A] {
      def iter(a: MyTree[A]) = {
        def go(I: MyTree[A]): List[A] = I match {
          case Empty() => Nil
          case Node(v, l, r) => v :: (go(l) ++ go(r))
        }
        go(a) // Dyn2(go(a))(listIter[A])
      }
    }

    val t : MyTree[Int] = Node(3,Node(4,Empty(),Empty()),Node(2,Empty(),Empty()))
    // sumElements2(t) //sumElements2(t)(treeIterable[Int]) 
    // printElements2(t) //printElements2(t)(treeIterable[Int])

    implicit def iterIterable[I, A](implicit proxy: Iter[I, A]): Iterable[I, A] = 
    new Iterable[I, A] {
      def iter(a: I) = a //Dyn2(a)(proxy)
    }

    val l = List(3, 5, 2, 1)
    // sumElements2(l)
    // printElements2(l)

  }
}
