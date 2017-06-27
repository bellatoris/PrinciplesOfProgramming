package pp201701.fin3.Data
import scala.language.higherKinds
import scala.language.implicitConversions

// The name "Dyn" has changed into "Box".

object BoxObj {
  abstract class Box[S[_]] {
    type Data
    val d: Data
    val i: S[Data]
  }

  object Box {
    implicit def apply[D,S[_]](dd: D)(implicit ii: S[D]):
        Box[S] = new Box[S] {
      type Data = D
      val d = dd
      val i = ii
    }
    implicit def methods[S[_]](d: Box[S]): S[d.Data] = d.i
  }

  abstract class Box2[S[_,_],A] {
    type Data
    val d: Data
    val i: S[Data,A]
  }
  object Box2 {
    implicit def apply[S[_, _],D,A](dd: D)(implicit ii: S[D,A]):
        Box2[S,A] = new Box2[S,A] {
      type Data = D
      val d = dd
      val i = ii
    }
    implicit def methods[S[_, _],A](d: Box2[S,A]): S[d.Data,A] = d.i
  }
}

object DataBundle {
  import BoxObj._

  abstract class Iter[I,A] {
    def getValue(i: I): Option[A]
    def getNext(i: I): I
  }
  
  abstract class Iterable[R, A] {
    def iter(a:R): Box2[Iter, A]
  }

  abstract class KeyVal[A] {
    def get_key(a:A) : String
    def get_val(a:A) : String
  }

  abstract class Report[R] {
    type A
    def title(r:R) : String
    def it : Iterable[R, A]
    def keyval : KeyVal[A]
  }

}
