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

object DictImpl {
  /*
   Problem 4: Define type classes of Dict and Report for Dictionary.
   Check Test.scala for the correct output.
   Example usage of HashMap:
   =======================================================================
   val empty_map = scala.collection.immutable.HashMap.empty[Int, String]
   // Insert key-value
   val map1 = empty_map + (1 -> "val1")
   // Find value by key
   val v : Option[String] = map1.get(1)
   
   =======================================================================
   You can obtain and use the Scala built-in 'Iterator' object for a HashMap object.
   =======================================================================
   val map : scala.collection.immutable.HashMap[K, V] = ..
   val i : Iterator[(K, V)] = map.iterator
   val (v, next_i) : (option[(K, V)], map.iterator) = if (i.hasNext) (Some(i.next), i) else (None, i)
   =======================================================================
   Iterator is a mutable class, but assume that getValue and getNext of an Iter object is used only once.
   */

  type Dictionary[K, V] = scala.collection.immutable.HashMap[K, V]

  implicit def DictDictionary[K, V] : Dict[Dictionary[K, V], K, V] = 
  new Dict[Dictionary[K, V], K, V] {
    def empty: Dictionary[K, V] = scala.collection.immutable.HashMap.empty[K, V]
    def add(d: Dictionary[K, V], k: K, v: V): Dictionary[K, V] = d + (k -> v)
    def lookup(d: Dictionary[K, V], k: K): Option[V] = d.get(k)
  }

  implicit def IteratorIter[A]: Iter[Iterator[A], A] = new Iter[Iterator[A], A] {
    def getValue(i: Iterator[A]): Option[A] = if (i.hasNext) (Some(i.next)) else None
    def getNext(i: Iterator[A]): Iterator[A] = i
  }

  def ReportDictionary[K, V](dtitle:String, keyToString: (K)=>String, valToString: (V)=>String) : Report[Dictionary[K, V]] = 
  new Report[Dictionary[K, V]] {
    type A = (K, V)
    def title(r: Dictionary[K, V]): String = dtitle

    def it: Iterable[Dictionary[K, V], (K, V)] = new Iterable[Dictionary[K, V], (K, V)] {
      def iter(a: Dictionary[K, V]): Box2[Iter, (K, V)] = a.iterator
    }

    def keyval: KeyVal[(K, V)] = new KeyVal[(K, V)] {
      def get_key(a: (K, V)) = keyToString(a._1)
      def get_val(a: (K, V)) = valToString(a._2)
    }
  }
}
