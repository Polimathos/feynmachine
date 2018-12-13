package model

import com.ogmacorp.ogmaneo._
import java.io._

import org.apache.spark.mllib.linalg.DenseMatrix

class FeynmanMachine (val h: Int, val w: Int) {

  val res = new Resources()
  res.create(ComputeSystem.DeviceType._gpu)

  val arch = new Architect
  arch.initialize(1234, res)

  val inputParams = arch.addInputLayer(new Vec2i(w, h))
  inputParams.setValue("in_p_alpha", 0.02f)
  inputParams.setValue("in_p_radius", 8)

  for (i <- 0 to 2){
    val layerParams = arch.addHigherLayer(new Vec2i(32, 32), SparseFeaturesType._chunk)
    layerParams.setValue("sfc_numSamples", 2)
  }

  val hierarchy = arch.generateHierarchy

  private val serializationEnabled = true

  if (serializationEnabled) {
    val f = new File("Example.opr")
    if (f.exists && !f.isDirectory) {
      System.out.println("Loading hierarchy from Example.opr")
      hierarchy.load(res.getComputeSystem, "Example.opr")
    }
  }
  val inputField = new ValueField2D(new Vec2i(w, h))
  def hierarchyStep(inputArray:Array[Double]):vectorf = {
    var ind1 = 0
    for (arr <- inputArray.grouped(w)) {
      var ind2 = 0
      for (elem <- arr){
        inputField.setValue(new Vec2i(ind1,ind2), elem.toFloat)
        ind2 += 1
      }
      ind1 += 1
    }
    val inputVector = new vectorvf
    inputVector.add(inputField)
    hierarchy.activate(inputVector)
    hierarchy.learn(inputVector)

    val prediction = hierarchy.getPredictions.get(0).getData
  }
//  val inputField = new ValueField2D(new Vec2i(w, h))

/*  for {
    y <- 0 to h
    x <- 0 to w
  } {
    inputField.setValue(new Vec2i(x, y), (y * w) + x)
  }
  */


/*
  System.out.println("Stepping the hierarchy...")

  for (i <- 0 to numSimSteps) {
    val inputVector = new vectorvf
    inputVector.add(inputField)
    hierarchy.activate(inputVector)
    hierarchy.learn(inputVector)
    //System.out.print(".");
  }
*/

//  System.out.println()

  def hierarchyStep() = {
    val inputField = new ValueField2D(new Vec2i(w, h))
  }

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
  }
}