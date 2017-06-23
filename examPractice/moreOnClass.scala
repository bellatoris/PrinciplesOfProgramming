object Main {
  def main(args: Array[String]): Unit = {
    class Primes(val prime: Int, val primes: List[Int]) {
      def getNext: Primes = {
        val p = computeNextPrime(prime + 2)
        new Primes(p, primes ++ (p :: Nil))
      }
      def computeNextPrime(n: Int): Int = {
        if (primes.forall((p: Int) => n%p != 0)) n
        else computeNextPrime(n + 2)
      }
    }

    def nthPrime(n: Int): Int = {
      def go(primes: Primes, k: Int): Int = {
        if (k <= 1) primes.prime
        else go(primes.getNext, k - 1)
      }

      if (n == 0) 2 else go(new Primes(3, List(3)), n)
    }

    println(nthPrime(10000))
  }
}
