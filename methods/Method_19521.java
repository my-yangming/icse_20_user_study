@OnCreateTransition static Transition animate(ComponentContext c,@State boolean autoBoundsTransitionEnabled){
  String[] transitionKeys=autoBoundsTransitionEnabled ? new String[]{TRANSITION_KEY_CONTAINER_1,TRANSITION_KEY_CHILD_1_1,TRANSITION_KEY_CHILD_1_2,TRANSITION_KEY_CHILD_1_3,TRANSITION_KEY_CONTAINER_2,TRANSITION_KEY_CHILD_2_1,TRANSITION_KEY_CHILD_2_2,TRANSITION_KEY_CONTAINER_3,TRANSITION_KEY_CHILD_3_1,TRANSITION_KEY_CONTAINER_4,TRANSITION_KEY_CONTAINER_4_1,TRANSITION_KEY_CONTAINER_4_2,TRANSITION_KEY_CHILD_4_1_1,TRANSITION_KEY_CHILD_4_1_2,TRANSITION_KEY_CHILD_4_2_1,TRANSITION_KEY_CHILD_4_2_2} : new String[]{TRANSITION_KEY_CONTAINER_1,TRANSITION_KEY_CHILD_2_2,TRANSITION_KEY_CHILD_3_1,TRANSITION_KEY_CHILD_4_2_2};
  return Transition.create(Transition.TransitionKeyType.GLOBAL,transitionKeys).animate(AnimatedProperties.WIDTH,AnimatedProperties.X).animator(Transition.timing(1000));
}