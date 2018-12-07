package body

import org.apache.spark._
import org.apache.spark.streaming._
import com.ogmacorp.ogmaneo._

object Main extends App {
/*  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val ssc = new StreamingContext(conf, Seconds(1))

  val lines = ssc.socketTextStream("localhost", 9999)

  val words = lines.flatMap(_.split(" "))

  val pairs = words.map(word => (word, 1))
  val wordCounts = pairs.reduceByKey(_ + _)

  wordCounts.print()

  ssc.start()

  ssc.awaitTermination()*/

  val res = new Resources()
  res.create(ComputeSystem.DeviceType._gpu)

  val arch = new Architect
  arch.initialize(1234, res)

  val w = 4
  val h = 4

  val inputParams = arch.addInputLayer(new Vec2i(w, h))
  inputParams.setValue("in_p_alpha", 0.02f)
  inputParams.setValue("in_p_radius", 8)

  var i = 0
  while ( {
    i < 2
  }) {
    val layerParams = arch.addHigherLayer(new Vec2i(32, 32), SparseFeaturesType._chunk)
    layerParams.setValue("sfc_numSamples", 2)

    {
      i += 1; i - 1
    }
  }
}