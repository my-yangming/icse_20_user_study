Pair<Configuration,Integer> parse() throws Exception {
  Configuration initialConfiguration=new Configuration(sentence,rootFirst);
  ArrayList<Configuration> beam=new ArrayList<Configuration>(beamWidth);
  beam.add(initialConfiguration);
  while (!ArcEager.isTerminal(beam)) {
    if (beamWidth != 1) {
      TreeSet<BeamElement> beamPreserver=new TreeSet<BeamElement>();
      sortBeam(beam,beamPreserver,false,null,beamWidth,rootFirst,featureLength,classifier,dependencyRelations);
      ArrayList<Configuration> repBeam=new ArrayList<Configuration>(beamWidth);
      for (      BeamElement beamElement : beamPreserver.descendingSet()) {
        if (repBeam.size() >= beamWidth)         break;
        int b=beamElement.index;
        int action=beamElement.action;
        int label=beamElement.label;
        float score=beamElement.score;
        Configuration newConfig=beam.get(b).clone();
        if (action == 0) {
          ArcEager.shift(newConfig.state);
          newConfig.addAction(0);
        }
 else         if (action == 1) {
          ArcEager.reduce(newConfig.state);
          newConfig.addAction(1);
        }
 else         if (action == 2) {
          ArcEager.rightArc(newConfig.state,label);
          newConfig.addAction(3 + label);
        }
 else         if (action == 3) {
          ArcEager.leftArc(newConfig.state,label);
          newConfig.addAction(3 + dependencyRelations.size() + label);
        }
 else         if (action == 4) {
          ArcEager.unShift(newConfig.state);
          newConfig.addAction(2);
        }
        newConfig.setScore(score);
        repBeam.add(newConfig);
      }
      beam=repBeam;
    }
 else {
      Configuration configuration=beam.get(0);
      State currentState=configuration.state;
      Object[] features=FeatureExtractor.extractAllParseFeatures(configuration,featureLength);
      float bestScore=Float.NEGATIVE_INFINITY;
      int bestAction=-1;
      boolean canShift=ArcEager.canDo(Action.Shift,currentState);
      boolean canReduce=ArcEager.canDo(Action.Reduce,currentState);
      boolean canRightArc=ArcEager.canDo(Action.RightArc,currentState);
      boolean canLeftArc=ArcEager.canDo(Action.LeftArc,currentState);
      if (!canShift && !canReduce && !canRightArc && !canLeftArc) {
        if (!currentState.stackEmpty()) {
          ArcEager.unShift(currentState);
          configuration.addAction(2);
        }
 else         if (!currentState.bufferEmpty() && currentState.stackEmpty()) {
          ArcEager.shift(currentState);
          configuration.addAction(0);
        }
      }
      if (canShift) {
        float score=classifier.shiftScore(features,true);
        if (score > bestScore) {
          bestScore=score;
          bestAction=0;
        }
      }
      if (canReduce) {
        float score=classifier.reduceScore(features,true);
        if (score > bestScore) {
          bestScore=score;
          bestAction=1;
        }
      }
      if (canRightArc) {
        float[] rightArcScores=classifier.rightArcScores(features,true);
        for (        int dependency : dependencyRelations) {
          float score=rightArcScores[dependency];
          if (score > bestScore) {
            bestScore=score;
            bestAction=3 + dependency;
          }
        }
      }
      if (ArcEager.canDo(Action.LeftArc,currentState)) {
        float[] leftArcScores=classifier.leftArcScores(features,true);
        for (        int dependency : dependencyRelations) {
          float score=leftArcScores[dependency];
          if (score > bestScore) {
            bestScore=score;
            bestAction=3 + dependencyRelations.size() + dependency;
          }
        }
      }
      if (bestAction != -1) {
        if (bestAction == 0) {
          ArcEager.shift(configuration.state);
        }
 else         if (bestAction == (1)) {
          ArcEager.reduce(configuration.state);
        }
 else {
          if (bestAction >= 3 + dependencyRelations.size()) {
            int label=bestAction - (3 + dependencyRelations.size());
            ArcEager.leftArc(configuration.state,label);
          }
 else {
            int label=bestAction - 3;
            ArcEager.rightArc(configuration.state,label);
          }
        }
        configuration.addScore(bestScore);
        configuration.addAction(bestAction);
      }
      if (beam.size() == 0) {
        System.out.println("WHY BEAM SIZE ZERO?");
      }
    }
  }
  Configuration bestConfiguration=null;
  float bestScore=Float.NEGATIVE_INFINITY;
  for (  Configuration configuration : beam) {
    if (configuration.getScore(true) > bestScore) {
      bestScore=configuration.getScore(true);
      bestConfiguration=configuration;
    }
  }
  return new Pair<Configuration,Integer>(bestConfiguration,id);
}
