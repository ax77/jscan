package jscan.buffers;

public interface IDataWriter {
  void o1(int v);

  void o2(int v);

  void o4(long v);

  void o8(long v);

  void oArr(int[] v);

  void oUtf(String v);

  void oUtf(String s, int len);

}
