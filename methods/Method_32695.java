/** 
 * Reads encoding generated by writeMillis.
 */
static long readMillis(DataInput in) throws IOException {
  int v=in.readUnsignedByte();
switch (v >> 6) {
case 0:
default :
    v=(v << (32 - 6)) >> (32 - 6);
  return v * (30 * 60000L);
case 1:
v=(v << (32 - 6)) >> (32 - 30);
v|=(in.readUnsignedByte()) << 16;
v|=(in.readUnsignedByte()) << 8;
v|=(in.readUnsignedByte());
return v * 60000L;
case 2:
long w=(((long)v) << (64 - 6)) >> (64 - 38);
w|=(in.readUnsignedByte()) << 24;
w|=(in.readUnsignedByte()) << 16;
w|=(in.readUnsignedByte()) << 8;
w|=(in.readUnsignedByte());
return w * 1000L;
case 3:
return in.readLong();
}
}
