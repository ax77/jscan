package strtox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralParser;

public class TestFloatingEv {

  private IntLiteral numinfo(String input) {
    return IntLiteralParser.parse(input);
  }

  @Test
  public void testHexEv0() {
    assertEquals(22275.074219f, numinfo("0x1.5c0c4cp+14F").getCfloat(), 0.0001f);
    assertEquals(25931.240234f, numinfo("0x1.952cf6p+14F").getCfloat(), 0.0001f);
    assertEquals(22392.224609f, numinfo("0x1.5de0e6p+14F").getCfloat(), 0.0001f);
    assertEquals(19063.197266f, numinfo("0x1.29dccap+14F").getCfloat(), 0.0001f);
    assertEquals(11799.931641f, numinfo("0x1.70bf74p+13f").getCfloat(), 0.0001f);

    assertEquals(23103f, numinfo("0x1.68fcp+14").getCfloat(), 0.0001f);
    assertEquals(18433.193359f, numinfo("0x1.2004c6p+14").getCfloat(), 0.0001f);
    assertEquals(31482.277344f, numinfo("0x1.ebe91cp+14").getCfloat(), 0.0001f);
    assertEquals(6718.183594f, numinfo("0x1.a3e2fp+12").getCfloat(), 0.0001f);
    assertEquals(28352.068359f, numinfo("0x1.bb0046p+14").getCfloat(), 0.0001f);
    assertEquals(23374.929688f, numinfo("0x1.6d3bb8p+14").getCfloat(), 0.0001f);
    assertEquals(16373.817383f, numinfo("0x1.ffae8ap+13").getCfloat(), 0.0001f);
    assertEquals(12444.158203f, numinfo("0x1.84e144p+13").getCfloat(), 0.0001f);
    assertEquals(28790.072266f, numinfo("0x1.c1d84ap+14").getCfloat(), 0.0001f);
    assertEquals(9869.788086f, numinfo("0x1.346e4ep+13").getCfloat(), 0.0001f);
    assertEquals(16481.945312f, numinfo("0x1.0187c8p+14").getCfloat(), 0.0001f);
    assertEquals(6253.748047f, numinfo("0x1.86dbf8p+12").getCfloat(), 0.0001f);
    assertEquals(22727.787109f, numinfo("0x1.631f26p+14").getCfloat(), 0.0001f);
    assertEquals(3411.360596f, numinfo("0x1.aa6b8ap+11").getCfloat(), 0.0001f);
    assertEquals(25829.351562f, numinfo("0x1.939568p+14").getCfloat(), 0.0001f);
    assertEquals(2081.968262f, numinfo("0x1.043efcp+11").getCfloat(), 0.0001f);
    assertEquals(6653.857422f, numinfo("0x1.9fddb8p+12").getCfloat(), 0.0001f);
    assertEquals(6813.074219f, numinfo("0x1.a9d13p+12").getCfloat(), 0.0001f);
    assertEquals(12239.101562f, numinfo("0x1.7e78dp+13").getCfloat(), 0.0001f);
    assertEquals(5674.621582f, numinfo("0x1.62a9f2p+12").getCfloat(), 0.0001f);
    assertEquals(12879.367188f, numinfo("0x1.927afp+13").getCfloat(), 0.0001f);
    assertEquals(12284.673828f, numinfo("0x1.7fe564p+13").getCfloat(), 0.0001f);
    assertEquals(9292.875977f, numinfo("0x1.226702p+13").getCfloat(), 0.0001f);
    assertEquals(240.964996f, numinfo("0x1.e1ee14p+7").getCfloat(), 0.0001f);
    assertEquals(10213.733398f, numinfo("0x1.3f2ddep+13").getCfloat(), 0.0001f);
    assertEquals(2346.991699f, numinfo("0x1.255fbcp+11").getCfloat(), 0.0001f);
    assertEquals(17090.84375f, numinfo("0x1.0b0b6p+14").getCfloat(), 0.0001f);
    assertEquals(93.588097f, numinfo("0x1.765a36p+6").getCfloat(), 0.0001f);
    assertEquals(7994.140137f, numinfo("0x1.f3a23ep+12").getCfloat(), 0.0001f);
    assertEquals(29610.626953f, numinfo("0x1.ceaa82p+14").getCfloat(), 0.0001f);
    assertEquals(26781.753906f, numinfo("0x1.a27704p+14").getCfloat(), 0.0001f);
    assertEquals(3148.178955f, numinfo("0x1.8985bap+11").getCfloat(), 0.0001f);
  }

  @Test
  public void testDecFloatEv0() {
    assertEquals(22275.074219f, numinfo("2.22750742e+4f").getCdouble(), 0.0001);
    assertEquals(25931.240234f, numinfo("2.59312402e+4f").getCdouble(), 0.0001);
    assertEquals(22392.224609f, numinfo("2.23922246e+4F").getCdouble(), 0.0001);
    assertEquals(19063.197266f, numinfo("1.90631973e+4f").getCdouble(), 0.0001);
    assertEquals(11799.931641f, numinfo("1.17999316e+4F").getCdouble(), 0.0001);
  }

  @Test
  public void testDecFloatEv1() {
    assertEquals(9080.25f, numinfo("9080.25f").getCfloat(), 0.0001f);
    assertEquals(9.08e+03, numinfo("9.08e+03").getCdouble(), 0.0001);
    assertEquals(27513, numinfo("0b00000000000000000110101101111001").getClong());
    assertEquals(27513, numinfo("27513").getClong());
    assertEquals(27513, numinfo("0x6b79").getClong());
    assertEquals(27513, numinfo("065571").getClong());
    assertEquals(21919.17f, numinfo("21919.17f").getCfloat(), 0.0001f);
    assertEquals(2.19e+04, numinfo("2.19e+04").getCdouble(), 0.0001);
    assertEquals(6176, numinfo("0b00000000000000000001100000100000").getClong());
    assertEquals(6176, numinfo("6176").getClong());
    assertEquals(6176, numinfo("0x1820").getClong());
    assertEquals(6176, numinfo("014040").getClong());
    assertEquals(28939.78f, numinfo("28939.78f").getCfloat(), 0.0001f);
    assertEquals(2.89e+04, numinfo("2.89e+04").getCdouble(), 0.0001);
    assertEquals(9436, numinfo("0b00000000000000000010010011011100").getClong());
    assertEquals(9436, numinfo("9436").getClong());
    assertEquals(9436, numinfo("0x24dc").getClong());
    assertEquals(9436, numinfo("022334").getClong());
    assertEquals(19314.72f, numinfo("19314.72f").getCfloat(), 0.0001f);
    assertEquals(1.93e+04, numinfo("1.93e+04").getCdouble(), 0.0001);
    assertEquals(29010, numinfo("0b00000000000000000111000101010010").getClong());
    assertEquals(29010, numinfo("29010").getClong());
    assertEquals(29010, numinfo("0x7152").getClong());
    assertEquals(29010, numinfo("070522").getClong());
    assertEquals(16478.61f, numinfo("16478.61f").getCfloat(), 0.0001f);
    assertEquals(1.65e+04, numinfo("1.65e+04").getCdouble(), 0.0001);
    assertEquals(7199, numinfo("0b00000000000000000001110000011111").getClong());
    assertEquals(7199, numinfo("7199").getClong());
    assertEquals(7199, numinfo("0x1c1f").getClong());
    assertEquals(7199, numinfo("016037").getClong());
    assertEquals(17029.35f, numinfo("17029.35f").getCfloat(), 0.0001f);
    assertEquals(1.70e+04, numinfo("1.70e+04").getCdouble(), 0.0001);
    assertEquals(964, numinfo("0b00000000000000000000001111000100").getClong());
    assertEquals(964, numinfo("964").getClong());
    assertEquals(964, numinfo("0x03c4").getClong());
    assertEquals(964, numinfo("01704").getClong());
    assertEquals(6376.44f, numinfo("6376.44f").getCfloat(), 0.0001f);
    assertEquals(6.38e+03, numinfo("6.38e+03").getCdouble(), 0.0001);
    assertEquals(29062, numinfo("0b00000000000000000111000110000110").getClong());
    assertEquals(29062, numinfo("29062").getClong());
    assertEquals(29062, numinfo("0x7186").getClong());
    assertEquals(29062, numinfo("070606").getClong());
    assertEquals(25741.47f, numinfo("25741.47f").getCfloat(), 0.0001f);
    assertEquals(2.57e+04, numinfo("2.57e+04").getCdouble(), 0.0001);
    assertEquals(17730, numinfo("0b00000000000000000100010101000010").getClong());
    assertEquals(17730, numinfo("17730").getClong());
    assertEquals(17730, numinfo("0x4542").getClong());
    assertEquals(17730, numinfo("042502").getClong());
    assertEquals(2573.33f, numinfo("2573.33f").getCfloat(), 0.0001f);
    assertEquals(2.57e+03, numinfo("2.57e+03").getCdouble(), 0.0001);
    assertEquals(16886, numinfo("0b00000000000000000100000111110110").getClong());
    assertEquals(16886, numinfo("16886").getClong());
    assertEquals(16886, numinfo("0x41f6").getClong());
    assertEquals(16886, numinfo("040766").getClong());
    assertEquals(18071.29f, numinfo("18071.29f").getCfloat(), 0.0001f);
    assertEquals(1.81e+04, numinfo("1.81e+04").getCdouble(), 0.0001);
    assertEquals(12992, numinfo("0b00000000000000000011001011000000").getClong());
    assertEquals(12992, numinfo("12992").getClong());
    assertEquals(12992, numinfo("0x32c0").getClong());
    assertEquals(12992, numinfo("031300").getClong());
    assertEquals(14059.90f, numinfo("14059.90f").getCfloat(), 0.0001f);
    assertEquals(1.41e+04, numinfo("1.41e+04").getCdouble(), 0.0001);
    assertEquals(26485, numinfo("0b00000000000000000110011101110101").getClong());
    assertEquals(26485, numinfo("26485").getClong());
    assertEquals(26485, numinfo("0x6775").getClong());
    assertEquals(26485, numinfo("063565").getClong());
    assertEquals(27951.44f, numinfo("27951.44f").getCfloat(), 0.0001f);
    assertEquals(2.80e+04, numinfo("2.80e+04").getCdouble(), 0.0001);
    assertEquals(4018, numinfo("0b00000000000000000000111110110010").getClong());
    assertEquals(4018, numinfo("4018").getClong());
    assertEquals(4018, numinfo("0x0fb2").getClong());
    assertEquals(4018, numinfo("07662").getClong());
    assertEquals(1757.01f, numinfo("1757.01f").getCfloat(), 0.0001f);
    assertEquals(1.76e+03, numinfo("1.76e+03").getCdouble(), 0.0001);
    assertEquals(15497, numinfo("0b00000000000000000011110010001001").getClong());
    assertEquals(15497, numinfo("15497").getClong());
    assertEquals(15497, numinfo("0x3c89").getClong());
    assertEquals(15497, numinfo("036211").getClong());
    assertEquals(20443.44f, numinfo("20443.44f").getCfloat(), 0.0001f);
    assertEquals(2.04e+04, numinfo("2.04e+04").getCdouble(), 0.0001);
    assertEquals(21875, numinfo("0b00000000000000000101010101110011").getClong());
    assertEquals(21875, numinfo("21875").getClong());
    assertEquals(21875, numinfo("0x5573").getClong());
    assertEquals(21875, numinfo("052563").getClong());
    assertEquals(30300.33f, numinfo("30300.33f").getCfloat(), 0.0001f);
    assertEquals(3.03e+04, numinfo("3.03e+04").getCdouble(), 0.0001);
    assertEquals(27451, numinfo("0b00000000000000000110101100111011").getClong());
    assertEquals(27451, numinfo("27451").getClong());
    assertEquals(27451, numinfo("0x6b3b").getClong());
    assertEquals(27451, numinfo("065473").getClong());
    assertEquals(10355.83f, numinfo("10355.83f").getCfloat(), 0.0001f);
    assertEquals(1.04e+04, numinfo("1.04e+04").getCdouble(), 0.0001);
    assertEquals(22603, numinfo("0b00000000000000000101100001001011").getClong());
    assertEquals(22603, numinfo("22603").getClong());
    assertEquals(22603, numinfo("0x584b").getClong());
    assertEquals(22603, numinfo("054113").getClong());
    assertEquals(9731.13f, numinfo("9731.13f").getCfloat(), 0.0001f);
    assertEquals(9.73e+03, numinfo("9.73e+03").getCdouble(), 0.0001);
    assertEquals(5429, numinfo("0b00000000000000000001010100110101").getClong());
    assertEquals(5429, numinfo("5429").getClong());
    assertEquals(5429, numinfo("0x1535").getClong());
    assertEquals(5429, numinfo("012465").getClong());
    assertEquals(13435.94f, numinfo("13435.94f").getCfloat(), 0.0001f);
    assertEquals(1.34e+04, numinfo("1.34e+04").getCdouble(), 0.0001);
    assertEquals(9776, numinfo("0b00000000000000000010011000110000").getClong());
    assertEquals(9776, numinfo("9776").getClong());
    assertEquals(9776, numinfo("0x2630").getClong());
    assertEquals(9776, numinfo("023060").getClong());
    assertEquals(22735.62f, numinfo("22735.62f").getCfloat(), 0.0001f);
    assertEquals(2.27e+04, numinfo("2.27e+04").getCdouble(), 0.0001);
    assertEquals(5896, numinfo("0b00000000000000000001011100001000").getClong());
    assertEquals(5896, numinfo("5896").getClong());
    assertEquals(5896, numinfo("0x1708").getClong());
    assertEquals(5896, numinfo("013410").getClong());
    assertEquals(18793.54f, numinfo("18793.54f").getCfloat(), 0.0001f);
    assertEquals(1.88e+04, numinfo("1.88e+04").getCdouble(), 0.0001);
    assertEquals(15821, numinfo("0b00000000000000000011110111001101").getClong());
    assertEquals(15821, numinfo("15821").getClong());
    assertEquals(15821, numinfo("0x3dcd").getClong());
    assertEquals(15821, numinfo("036715").getClong());
    assertEquals(22312.02f, numinfo("22312.02f").getCfloat(), 0.0001f);
    assertEquals(2.23e+04, numinfo("2.23e+04").getCdouble(), 0.0001);
    assertEquals(9356, numinfo("0b00000000000000000010010010001100").getClong());
    assertEquals(9356, numinfo("9356").getClong());
    assertEquals(9356, numinfo("0x248c").getClong());
    assertEquals(9356, numinfo("022214").getClong());
    assertEquals(3962.23f, numinfo("3962.23f").getCfloat(), 0.0001f);
    assertEquals(3.96e+03, numinfo("3.96e+03").getCdouble(), 0.0001);
    assertEquals(32722, numinfo("0b00000000000000000111111111010010").getClong());
    assertEquals(32722, numinfo("32722").getClong());
    assertEquals(32722, numinfo("0x7fd2").getClong());
    assertEquals(32722, numinfo("077722").getClong());
    assertEquals(8497.88f, numinfo("8497.88f").getCfloat(), 0.0001f);
    assertEquals(8.50e+03, numinfo("8.50e+03").getCdouble(), 0.0001);
    assertEquals(1666, numinfo("0b00000000000000000000011010000010").getClong());
    assertEquals(1666, numinfo("1666").getClong());
    assertEquals(1666, numinfo("0x0682").getClong());
    assertEquals(1666, numinfo("03202").getClong());
    assertEquals(28671.40f, numinfo("28671.40f").getCfloat(), 0.0001f);
    assertEquals(2.87e+04, numinfo("2.87e+04").getCdouble(), 0.0001);
    assertEquals(1370, numinfo("0b00000000000000000000010101011010").getClong());
    assertEquals(1370, numinfo("1370").getClong());
    assertEquals(1370, numinfo("0x055a").getClong());
    assertEquals(1370, numinfo("02532").getClong());
    assertEquals(8524.29f, numinfo("8524.29f").getCfloat(), 0.0001f);
    assertEquals(8.52e+03, numinfo("8.52e+03").getCdouble(), 0.0001);
    assertEquals(17050, numinfo("0b00000000000000000100001010011010").getClong());
    assertEquals(17050, numinfo("17050").getClong());
    assertEquals(17050, numinfo("0x429a").getClong());
    assertEquals(17050, numinfo("041232").getClong());
    assertEquals(4146.22f, numinfo("4146.22f").getCfloat(), 0.0001f);
    assertEquals(4.15e+03, numinfo("4.15e+03").getCdouble(), 0.0001);
    assertEquals(8993, numinfo("0b00000000000000000010001100100001").getClong());
    assertEquals(8993, numinfo("8993").getClong());
    assertEquals(8993, numinfo("0x2321").getClong());
    assertEquals(8993, numinfo("021441").getClong());
    assertEquals(10431.85f, numinfo("10431.85f").getCfloat(), 0.0001f);
    assertEquals(1.04e+04, numinfo("1.04e+04").getCdouble(), 0.0001);
    assertEquals(5740, numinfo("0b00000000000000000001011001101100").getClong());
    assertEquals(5740, numinfo("5740").getClong());
    assertEquals(5740, numinfo("0x166c").getClong());
    assertEquals(5740, numinfo("013154").getClong());
    assertEquals(22875.55f, numinfo("22875.55f").getCfloat(), 0.0001f);
    assertEquals(2.29e+04, numinfo("2.29e+04").getCdouble(), 0.0001);
    assertEquals(26486, numinfo("0b00000000000000000110011101110110").getClong());
    assertEquals(26486, numinfo("26486").getClong());
    assertEquals(26486, numinfo("0x6776").getClong());
    assertEquals(26486, numinfo("063566").getClong());
    assertEquals(2515.97f, numinfo("2515.97f").getCfloat(), 0.0001f);
    assertEquals(2.52e+03, numinfo("2.52e+03").getCdouble(), 0.0001);
    assertEquals(28797, numinfo("0b00000000000000000111000001111101").getClong());
    assertEquals(28797, numinfo("28797").getClong());
    assertEquals(28797, numinfo("0x707d").getClong());
    assertEquals(28797, numinfo("070175").getClong());
    assertEquals(14168.74f, numinfo("14168.74f").getCfloat(), 0.0001f);
    assertEquals(1.42e+04, numinfo("1.42e+04").getCdouble(), 0.0001);
    assertEquals(14870, numinfo("0b00000000000000000011101000010110").getClong());
    assertEquals(14870, numinfo("14870").getClong());
    assertEquals(14870, numinfo("0x3a16").getClong());
    assertEquals(14870, numinfo("035026").getClong());
    assertEquals(10253.04f, numinfo("10253.04f").getCfloat(), 0.0001f);
    assertEquals(1.03e+04, numinfo("1.03e+04").getCdouble(), 0.0001);
    assertEquals(11159, numinfo("0b00000000000000000010101110010111").getClong());
    assertEquals(11159, numinfo("11159").getClong());
    assertEquals(11159, numinfo("0x2b97").getClong());
    assertEquals(11159, numinfo("025627").getClong());
    assertEquals(2568.12f, numinfo("2568.12f").getCfloat(), 0.0001f);
    assertEquals(2.57e+03, numinfo("2.57e+03").getCdouble(), 0.0001);
    assertEquals(13999, numinfo("0b00000000000000000011011010101111").getClong());
    assertEquals(13999, numinfo("13999").getClong());
    assertEquals(13999, numinfo("0x36af").getClong());
    assertEquals(13999, numinfo("033257").getClong());
  }

  @Test
  public void testFloatingEv0() {
    /**/
    assertEquals(22275.074219f, numinfo("0x1.5c0c4cp+14F").getCfloat(), 0.0001f);
    assertEquals(22275.074219f, numinfo("2.22750742e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(25931.240234f, numinfo("0x1.952cf6p+14F").getCfloat(), 0.0001f);
    assertEquals(25931.240234f, numinfo("2.59312402e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(22392.224609f, numinfo("0x1.5de0e6p+14F").getCfloat(), 0.0001f);
    assertEquals(22392.224609f, numinfo("2.23922246e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(19063.197266f, numinfo("0x1.29dccap+14F").getCfloat(), 0.0001f);
    assertEquals(19063.197266f, numinfo("1.90631973e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(11799.931641f, numinfo("0x1.70bf74p+13f").getCfloat(), 0.0001f);
    assertEquals(11799.931641f, numinfo("1.17999316e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(10066.62207f, numinfo("0x1.3a94fap+13F").getCfloat(), 0.0001f);
    assertEquals(10066.62207f, numinfo("1.00666221e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(8632.577148f, numinfo("0x1.0dc49ep+13F").getCfloat(), 0.0001f);
    assertEquals(8632.577148f, numinfo("8.63257715e+3f").getCdouble(), 0.0001);
    /**/
    assertEquals(8892.573242f, numinfo("0x1.15e496p+13F").getCfloat(), 0.0001f);
    assertEquals(8892.573242f, numinfo("8.89257324e+3f").getCdouble(), 0.0001);
    /**/
    assertEquals(7369.494141f, numinfo("0x1.cc97e8p+12f").getCfloat(), 0.0001f);
    assertEquals(7369.494141f, numinfo("7.36949414e+3F").getCdouble(), 0.0001);
    /**/
    assertEquals(192.79509f, numinfo("0x1.819716p+7F").getCfloat(), 0.0001f);
    assertEquals(192.79509f, numinfo("1.92795090e+2f").getCdouble(), 0.0001);
    /**/
    assertEquals(7333.695312f, numinfo("0x1.ca5b2p+12F").getCfloat(), 0.0001f);
    assertEquals(7333.695312f, numinfo("7.33369531e+3F").getCdouble(), 0.0001);
    /**/
    assertEquals(5237.865723f, numinfo("0x1.475ddap+12f").getCfloat(), 0.0001f);
    assertEquals(5237.865723f, numinfo("5.23786572e+3F").getCdouble(), 0.0001);
    /**/
    assertEquals(6304.337891f, numinfo("0x1.8a0568p+12f").getCfloat(), 0.0001f);
    assertEquals(6304.337891f, numinfo("6.30433789e+3f").getCdouble(), 0.0001);
    /**/
    assertEquals(18713.681641f, numinfo("0x1.2466bap+14F").getCfloat(), 0.0001f);
    assertEquals(18713.681641f, numinfo("1.87136816e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(9898.96582f, numinfo("0x1.3557bap+13f").getCfloat(), 0.0001f);
    assertEquals(9898.96582f, numinfo("9.89896582e+3F").getCdouble(), 0.0001);
    /**/
    assertEquals(10961.639648f, numinfo("0x1.568d1ep+13f").getCfloat(), 0.0001f);
    assertEquals(10961.639648f, numinfo("1.09616396e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(5733.17627f, numinfo("0x1.6652d2p+12F").getCfloat(), 0.0001f);
    assertEquals(5733.17627f, numinfo("5.73317627e+3F").getCdouble(), 0.0001);
    /**/
    assertEquals(13044.808594f, numinfo("0x1.97a678p+13f").getCfloat(), 0.0001f);
    assertEquals(13044.808594f, numinfo("1.30448086e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(27424.332031f, numinfo("0x1.ac8154p+14f").getCfloat(), 0.0001f);
    assertEquals(27424.332031f, numinfo("2.74243320e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(31215.970703f, numinfo("0x1.e7bfe2p+14f").getCfloat(), 0.0001f);
    assertEquals(31215.970703f, numinfo("3.12159707e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(21612.880859f, numinfo("0x1.51b386p+14F").getCfloat(), 0.0001f);
    assertEquals(21612.880859f, numinfo("2.16128809e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(24764.498047f, numinfo("0x1.82f1fep+14f").getCfloat(), 0.0001f);
    assertEquals(24764.498047f, numinfo("2.47644980e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(21846.451172f, numinfo("0x1.5559cep+14f").getCfloat(), 0.0001f);
    assertEquals(21846.451172f, numinfo("2.18464512e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(15286.836914f, numinfo("0x1.ddb6b2p+13F").getCfloat(), 0.0001f);
    assertEquals(15286.836914f, numinfo("1.52868369e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(28908.193359f, numinfo("0x1.c3b0c6p+14F").getCfloat(), 0.0001f);
    assertEquals(28908.193359f, numinfo("2.89081934e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(32535.753906f, numinfo("0x1.fc5f04p+14F").getCfloat(), 0.0001f);
    assertEquals(32535.753906f, numinfo("3.25357539e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(10979.911133f, numinfo("0x1.571f4ap+13f").getCfloat(), 0.0001f);
    assertEquals(10979.911133f, numinfo("1.09799111e+4F").getCdouble(), 0.0001);
    /**/
    assertEquals(999.007751f, numinfo("0x1.f380fep+9f").getCfloat(), 0.0001f);
    assertEquals(999.007751f, numinfo("9.99007751e+2f").getCdouble(), 0.0001);
    /**/
    assertEquals(20000.134766f, numinfo("0x1.38808ap+14f").getCfloat(), 0.0001f);
    assertEquals(20000.134766f, numinfo("2.00001348e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(32249.044922f, numinfo("0x1.f7e42ep+14F").getCfloat(), 0.0001f);
    assertEquals(32249.044922f, numinfo("3.22490449e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(15002.004883f, numinfo("0x1.d4d00ap+13F").getCfloat(), 0.0001f);
    assertEquals(15002.004883f, numinfo("1.50020049e+4f").getCdouble(), 0.0001);
    /**/
    assertEquals(31173.992188f, numinfo("0x1.e717f8p+14F").getCfloat(), 0.0001f);
    assertEquals(31173.992188f, numinfo("3.11739922e+4F").getCdouble(), 0.0001);

  }

}
