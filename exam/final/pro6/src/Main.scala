package pp201701.fin
import pp201701.fin.Data.DataBundle._
import pp201701.fin.Data.BoxObj._

/*
 * ** The submitted code should be runnable. Before upload, you MUST check it
      whether Test.scala runs successfully. Otherwise you may get 0 points for
      all problems.

 * ** Compress the 'src' folder only. Don't put .sh files in the zip file.
      You can put the .iml file together, if you feel difficulty for some reasons.

 * ** Use only the features taught in class. Especially, don't use imperative
      features such as var, while, for, return, and so on. You may get deduction
      in your points.

 * ** Do not use equivalent built-in features in Scala. The core logic should be
      implemented by you.

 */

object Main {
  /*
   Problem 6: Define type classes of Dict and Report for OrdDictionary.
   OrdDictionary is a wrapper of D, which comes with Dict[D, K, V] but its implemention is unknown.
   The iteration order of OrdDictionary should follow the order of insertion.

   Assume that we can compare K's elements with "=".

   Hint: Use 'keys' in order to store the order of insertion.
   */

  class OrdDictionary[D, K](val d:D, val keys : List[K])

  implicit def DictOrdDictionary[D, K, V](implicit DictD : Dict[D, K, V]) :
      Dict[OrdDictionary[D, K], K, V] = new Dict[OrdDictionary[D, K], K, V] {
    def empty: OrdDictionary[D, K] =  new OrdDictionary[D, K](DictD.empty, Nil)
    def add(d: OrdDictionary[D, K], k: K, v: V): OrdDictionary[D, K] = {
      val newDic = DictD.add(d.d, k, v)
      val newKeys = d.keys.filter(key => key != k) ++ (k :: Nil)
      new OrdDictionary[D, K](newDic, newKeys)
    }
    def lookup(d: OrdDictionary[D, K], k: K): Option[V] = DictD.lookup(d.d, k)
  }

  implicit def dicIter[D, K, V](implicit DictD: Dict[D, K, V]): Iter[(D, List[K]), (K, V)] =
  new Iter[(D, List[K]), (K, V)] {
    def getValue(i: (D, List[K])): Option[(K, V)] = i._2 match {
      case Nil => None
      case hd :: tl => Some((hd, DictD.lookup(i._1, hd).get))
    }
    def getNext(i: (D, List[K])): (D, List[K]) = (i._1, i._2.tail)
  }

  def ReportOrdDictionary[D, K, V](ord_title : String, keyToString:(K)=>String,
    valToString:(V)=>String)(implicit DictD: Dict[D, K, V]) :
      Report[OrdDictionary[D, K]] = 
  new Report[OrdDictionary[D, K]] {
    type A = (K, V)
    def title(r: OrdDictionary[D, K]): String = ord_title
    def it: Iterable[OrdDictionary[D, K], A] = new Iterable[OrdDictionary[D, K], A] {
      def iter(a: OrdDictionary[D, K]) = (a.d, a.keys)
    }
    def keyval: KeyVal[A] = new KeyVal[A] {
      def get_key(a: A): String = keyToString(a._1)
      def get_val(a: A): String = valToString(a._2)
    }
  }
}
