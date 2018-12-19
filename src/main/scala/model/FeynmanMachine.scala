package model

import com.ogmacorp.ogmaneo._
import java.io._

import org.apache.spark.mllib.linalg.DenseMatrix

class FeynmanMachine (val h: Int, val w: Int) {

  val res = new Resources()
  res.create(ComputeSystem.DeviceType._gpu)

  val arch = new Architect
  arch.initialize(1234, res)

  val inputOutside = arch.addInputLayer(new Vec2i(w, h))
  inputOutside.setValue("in_p_alpha", 0.02f)
  inputOutside.setValue("in_p_radius", 8)

  val inputInside = arch.addInputLayer(new Vec2i(w, h))
  inputInside.setValue("in_p_alpha", 0.02f)
  inputInside.setValue("in_p_radius", 8)

  for (i <- 0 to 2){
    val layerParams = arch.addHigherLayer(new Vec2i(36, 36), SparseFeaturesType._chunk)
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

  val inputOutsideField = new ValueField2D(new Vec2i(w, h))
  val inputInsideField = new ValueField2D(new Vec2i(w, h))

  def fieldsValueSetter(zippedArrays:Array[(Double, Double)]): Unit ={
    var ind1 = 0
    for (zippedRow <- zippedArrays.grouped(h)) {
      var ind2 = 0
      for (elem <- zippedRow){
        inputOutsideField.setValue(new Vec2i(ind1,ind2), elem._1.toFloat)
        inputInsideField.setValue(new Vec2i(ind1,ind2), elem._2.toFloat)
        ind2 += 1
      }
      ind1 += 1
    }
  }

  def parsePrediction(prediction:vectorf):Array[Double] ={
    var responseArray = Array[Double]()
    for (el <- 0 to prediction.size().toInt-1){
      responseArray ++= Array(prediction.get(el).toDouble)
    }
    responseArray
  }

  def hierarchyStep(outsideArray:Array[Double],insideArray:Array[Double]):(Array[Double],Array[Double]) = {
    val zippedArrays = outsideArray.zip(insideArray)

    fieldsValueSetter(zippedArrays)

    val inputVector = new vectorvf
    inputVector.add(inputOutsideField)
    inputVector.add(inputInsideField)
    hierarchy.activate(inputVector)
    hierarchy.learn(inputVector)

    val predictionOutside = hierarchy.getPredictions.get(0).getData
    val predictionInside = hierarchy.getPredictions.get(1).getData

    val predictionOutsideArray = parsePrediction(predictionOutside)
    val predictionInsideArray = parsePrediction(predictionInside)

    if (serializationEnabled) {
      System.out.println("Saving hierarchy to Example.opr")
      hierarchy.save(res.getComputeSystem, "Example.opr")
    }
    (predictionOutsideArray,predictionInsideArray)

  }

  def trainingSwitch(input:String, currentMode:Boolean):Boolean={
    if (input.contains("!train")){
      return true
    }
    if (input.contains("!talk")){
      return false
    }
    return currentMode
  }
}