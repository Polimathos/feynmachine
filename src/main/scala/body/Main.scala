package body

import org.apache.spark._
import org.apache.spark.streaming._
import com.ogmacorp.ogmaneo._
import java.io._
import model.{FeynmanMachine,EncoderDecoder}

object Main extends App {
  val encoderDecoder = new EncoderDecoder
  val feynmanMachine = new FeynmanMachine(encoderDecoder.maxMessageLength,encoderDecoder.chars.length)


/*  val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
  val ssc = new StreamingContext(conf, Seconds(1))

  val lines = ssc.socketTextStream("localhost", 9999)

  val words = lines.flatMap(_.split(" "))

  val pairs = words.map(word => (word, 1))
  val wordCounts = pairs.reduceByKey(_ + _)

  wordCounts.print()

  ssc.start()

  ssc.awaitTermination()*/

/*  val numSimSteps = 100

  val res = new Resources()
  res.create(ComputeSystem.DeviceType._gpu)

  val arch = new Architect
  arch.initialize(1234, res)

  val w = 4
  val h = 4

  val inputParams = arch.addInputLayer(new Vec2i(w, h))
  inputParams.setValue("in_p_alpha", 0.02f)
  inputParams.setValue("in_p_radius", 8)

  for (i <- 0 to 2){
    val layerParams = arch.addHigherLayer(new Vec2i(32, 32), SparseFeaturesType._chunk)
    layerParams.setValue("sfc_numSamples", 2)
  }

  val hierarchy = arch.generateHierarchy

  val inputField = new ValueField2D(new Vec2i(w, h))

  for {
    y <- 0 to h
    x <- 0 to w
  } {
    inputField.setValue(new Vec2i(x, y), (y * w) + x)
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

  for (i <- 0 to numSimSteps) {
    val inputVector = new vectorvf
    inputVector.add(inputField)
    hierarchy.activate(inputVector)
    hierarchy.learn(inputVector)
    //System.out.print(".");
  }

  System.out.println()

  val prediction = hierarchy.getPredictions.get(0)

  for(i <- 0 to numSimSteps) {
    System.out.println(i)
    for {
      y <- 0 to h
      x <- 0 to w
    } {
      System.out.print(inputField.getValue(new Vec2i(x, y)).toString+" ")
    }
    System.out.println()

    System.out.print("Prediction :")
    for {
      y <- 0 to h
      x <- 0 to w
    } {
      System.out.print(prediction.getValue(new Vec2i(x, y)).toString+" ")
    }
    System.out.println()
    System.out.println(i)
    if (serializationEnabled) {
      System.out.println("Saving hierarchy to Example.opr")
      hierarchy.save(res.getComputeSystem, "Example.opr")
    }
  }*/
}