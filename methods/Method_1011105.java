public Iterable<Integer> numbers(final int max){
  return Sequence.fromClosure(new ISequenceClosure<Integer>(){
    public Iterable<Integer> iterable(){
      return new Iterable<Integer>(){
        public Iterator<Integer> iterator(){
          return new YieldingIterator<Integer>(){
            protected boolean moveToNext(){
              __loop__:               do {
                __switch__: switch (this.__CP__) {
case -1:
                  assert false : "Internal error";
                return false;
case 12:
              if (_11_n > max) {
                this.__CP__=13;
                break;
              }
            this.__CP__=14;
          break;
case 8:
        if (true) {
          this.__CP__=9;
          break;
        }
      this.__CP__=3;
    break;
case 3:
  if (false) {
    this.__CP__=2;
    break;
  }
this.__CP__=1;
break;
case 4:
this.__CP__=5;
this.yield(0);
return true;
case 14:
this.__CP__=16;
this.yield(_11_n);
return true;
case 0:
this.__CP__=2;
break;
case 2:
this.__CP__=4;
break;
case 5:
this._6_n_=0;
this._7_n__=1;
this.__CP__=8;
break;
case 9:
this._11_n=_6_n_ + _7_n__;
this.__CP__=12;
break;
case 16:
_7_n__=_6_n_;
_6_n_=_11_n;
this.__CP__=8;
break;
case 13:
this.__CP__=1;
break;
default :
break __loop__;
}
}
 while (true);
return false;
}
}
;
}
}
;
}
}
);
}
