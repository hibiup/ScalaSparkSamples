package samples.spark

import org.scalatest.FlatSpec

class TestReadProperties extends FlatSpec{
    "Properties file" should "be able to be load from IO Monad" in {
        val res = Config("cc") flatMap  {c => Config(c) }
    }
}
