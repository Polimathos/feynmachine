package body

import org.apache.spark._
import org.apache.spark.streaming._
import com.ogmacorp.ogmaneo._

import java.io._

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

  val numSimSteps = 100
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

  val hierarchy = arch.generateHierarchy

  val inputField = new ValueField2D(new Vec2i(w, h))

  var y = 0
  while ( {
    y < h
  }) {
    var x = 0
    while ( {
      x < w
    }) {
      inputField.setValue(new Vec2i(x, y), (y * w) + x)

      {
        x += 1
      }
    }

    {
      y += 1
    }
  }
  private val serializationEnabled = false

  if (serializationEnabled) {
    val f = new File("Example.opr")
    if (f.exists && !f.isDirectory) {
      System.out.println("Loading hierarchy from Example.opr")
      hierarchy.load(res.getComputeSystem, "Example.opr")
    }
  }

  System.out.println("Stepping the hierarchy...")
  var i2 = 0
  while ( {
    i2 < numSimSteps
  }) {
    val inputVector = new vectorvf
    inputVector.add(inputField)
    hierarchy.activate(inputVector)
    hierarchy.learn(inputVector)
    //System.out.print(".");

    {
      i += 1
    }
  }
  System.out.println()

  val prediction = hierarchy.getPredictions.get(0)

  while (true) {
    System.out.print("Input      :")
    var ye = 0
    while ( {
      ye < h
    }) {
      var x = 0
      while ( {
        x < w
      }) {
        System.out.printf(" %.2f", inputField.getValue(new Vec2i(x, ye)).toString)

        {
          x += 1
        }
      }

      {
        ye += 1
      }
    }
    System.out.println()
    System.out.print("Prediction :")
    var y = 0
    while ( {
      y < h
    }) {
      var x = 0
      while ( {
        x < w
      }) {
        System.out.printf(" %.2f", prediction.getValue(new Vec2i(x, y)).toString)

        {
          x += 1; x - 1
        }
      }

      {
        y += 1; y - 1
      }
    }
    System.out.println()
    if (serializationEnabled) {
      System.out.println("Saving hierarchy to Example.opr")
      hierarchy.save(res.getComputeSystem, "Example.opr")
    }
  }
}