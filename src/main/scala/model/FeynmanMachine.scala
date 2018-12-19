package model

import com.ogmacorp.ogmaneo._
import java.io._

import org.apache.spark.mllib.linalg.DenseMatrix

class FeynmanMachine (val h: Int, val w: Int) {

  val res = new Resources()
  res.create(ComputeSystem.DeviceType._gpu)

  val arch = new Architect
  arch.initialize(Configuration.architectSeed, res)

  val inputOutside = arch.addInputLayer(new Vec2i(w, h))
  inputOutside.setValue("in_p_alpha", Configuration.in_p_alpha)
  inputOutside.setValue("in_p_radius", Configuration.in_p_radius)

  val inputInside = arch.addInputLayer(new Vec2i(w, h))
  inputInside.setValue("in_p_alpha", Configuration.in_p_alpha)
  inputInside.setValue("in_p_radius", Configuration.in_p_radius)

  for (i <- 0 to Configuration.layerNumber){
    val layerParams = arch.addHigherLayer(
      new Vec2i(Configuration.encoderSize,Configuration.encoderSize),
      SparseFeaturesType._chunk)
    layerParams.setValue("sfc_numSamples", Configuration.sfc_numSamples)
    layerParams.setValue("sfc_chunkSize", Configuration.sfc_chunkSize)
    layerParams.setValue("sfc_ff_radius", Configuration.sfc_ff_radius)
    layerParams.setValue("sfc_gamma", Configuration.sfc_gamma)
    layerParams.setValue("hl_poolSteps", Configuration.hl_poolSteps)
    layerParams.setValue("p_alpha", Configuration.p_alpha)
    layerParams.setValue("p_beta", Configuration.p_beta)
    layerParams.setValue("p_radius", Configuration.p_radius)

  }

  val hierarchy = arch.generateHierarchy

  private val serializationEnabled = true

  if (serializationEnabled) {
    val f = new File("savedHierarchy.opr")
    if (f.exists && !f.isDirectory) {
      System.out.println("Loading hierarchy from savedHierarchy.opr")
      hierarchy.load(res.getComputeSystem, "savedHierarchy.opr")
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

  def saveHierarchy: Unit ={
    if (serializationEnabled) {
      System.out.println("Saving hierarchy to savedHierarchy.opr")
      hierarchy.save(res.getComputeSystem, "savedHierarchy.opr")
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