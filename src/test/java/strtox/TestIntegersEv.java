package strtox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jscan.literals.IntLiteral;
import jscan.literals.IntLiteralParser;

public class TestIntegersEv {

  private IntLiteral numinfo(String input) {
    return IntLiteralParser.parse(input);
  }

  @Test
  public void testLong() {
    /**/
    assertEquals(13170, numinfo("13170").getClong());
    assertEquals(13170, numinfo("0b00000000000000000011001101110010lu").getClong());
    assertEquals(13170, numinfo("0x00003372llu").getClong());
    assertEquals(13170, numinfo("031562LU").getClong());
    assertEquals(13170, numinfo("13170l").getClong());
    /**/
    assertEquals(14378, numinfo("14378").getClong());
    assertEquals(14378, numinfo("0b00000000000000000011100000101010LLU").getClong());
    assertEquals(14378, numinfo("0x0000382au").getClong());
    assertEquals(14378, numinfo("034052U").getClong());
    assertEquals(14378, numinfo("14378ull").getClong());
    /**/
    assertEquals(29855, numinfo("29855").getClong());
    assertEquals(29855, numinfo("0b00000000000000000111010010011111LL").getClong());
    assertEquals(29855, numinfo("0x0000749fu").getClong());
    assertEquals(29855, numinfo("072237L").getClong());
    assertEquals(29855, numinfo("29855UL").getClong());
    /**/
    assertEquals(28997, numinfo("28997").getClong());
    assertEquals(28997, numinfo("0b00000000000000000111000101000101LU").getClong());
    assertEquals(28997, numinfo("0x00007145ull").getClong());
    assertEquals(28997, numinfo("070505lu").getClong());
    assertEquals(28997, numinfo("28997l").getClong());
    /**/
    assertEquals(21451, numinfo("21451").getClong());
    assertEquals(21451, numinfo("0b00000000000000000101001111001011ll").getClong());
    assertEquals(21451, numinfo("0x000053cbU").getClong());
    assertEquals(21451, numinfo("051713u").getClong());
    assertEquals(21451, numinfo("21451lu").getClong());
    /**/
    assertEquals(9144, numinfo("9144").getClong());
    assertEquals(9144, numinfo("0b00000000000000000010001110111000l").getClong());
    assertEquals(9144, numinfo("0x000023b8ul").getClong());
    assertEquals(9144, numinfo("021670ull").getClong());
    assertEquals(9144, numinfo("9144lu").getClong());
    /**/
    assertEquals(3904, numinfo("3904").getClong());
    assertEquals(3904, numinfo("0b00000000000000000000111101000000l").getClong());
    assertEquals(3904, numinfo("0x00000f40l").getClong());
    assertEquals(3904, numinfo("07500U").getClong());
    assertEquals(3904, numinfo("3904u").getClong());
    /**/
    assertEquals(12711, numinfo("12711").getClong());
    assertEquals(12711, numinfo("0b00000000000000000011000110100111LL").getClong());
    assertEquals(12711, numinfo("0x000031a7U").getClong());
    assertEquals(12711, numinfo("030647L").getClong());
    assertEquals(12711, numinfo("12711ul").getClong());
    /**/
    assertEquals(27819, numinfo("27819").getClong());
    assertEquals(27819, numinfo("0b00000000000000000110110010101011LL").getClong());
    assertEquals(27819, numinfo("0x00006cabLLU").getClong());
    assertEquals(27819, numinfo("066253LU").getClong());
    assertEquals(27819, numinfo("27819LLU").getClong());
    /**/
    assertEquals(15963, numinfo("15963").getClong());
    assertEquals(15963, numinfo("0b00000000000000000011111001011011UL").getClong());
    assertEquals(15963, numinfo("0x00003e5bull").getClong());
    assertEquals(15963, numinfo("037133l").getClong());
    assertEquals(15963, numinfo("15963llu").getClong());
    /**/
    assertEquals(2953, numinfo("2953").getClong());
    assertEquals(2953, numinfo("0b00000000000000000000101110001001ULL").getClong());
    assertEquals(2953, numinfo("0x00000b89ul").getClong());
    assertEquals(2953, numinfo("05611U").getClong());
    assertEquals(2953, numinfo("2953u").getClong());
    /**/
    assertEquals(30624, numinfo("30624").getClong());
    assertEquals(30624, numinfo("0b00000000000000000111011110100000L").getClong());
    assertEquals(30624, numinfo("0x000077a0LLU").getClong());
    assertEquals(30624, numinfo("073640LU").getClong());
    assertEquals(30624, numinfo("30624LU").getClong());
    /**/
    assertEquals(3136, numinfo("3136").getClong());
    assertEquals(3136, numinfo("0b00000000000000000000110001000000l").getClong());
    assertEquals(3136, numinfo("0x00000c40ULL").getClong());
    assertEquals(3136, numinfo("06100LU").getClong());
    assertEquals(3136, numinfo("3136ll").getClong());
    /**/
    assertEquals(23191, numinfo("23191").getClong());
    assertEquals(23191, numinfo("0b00000000000000000101101010010111u").getClong());
    assertEquals(23191, numinfo("0x00005a97UL").getClong());
    assertEquals(23191, numinfo("055227ul").getClong());
    assertEquals(23191, numinfo("23191UL").getClong());
    /**/
    assertEquals(2609, numinfo("2609").getClong());
    assertEquals(2609, numinfo("0b00000000000000000000101000110001ULL").getClong());
    assertEquals(2609, numinfo("0x00000a31llu").getClong());
    assertEquals(2609, numinfo("05061ll").getClong());
    assertEquals(2609, numinfo("2609ll").getClong());
    /**/
    assertEquals(21228, numinfo("21228").getClong());
    assertEquals(21228, numinfo("0b00000000000000000101001011101100L").getClong());
    assertEquals(21228, numinfo("0x000052ecU").getClong());
    assertEquals(21228, numinfo("051354lu").getClong());
    assertEquals(21228, numinfo("21228llu").getClong());
    /**/
    assertEquals(7357, numinfo("7357").getClong());
    assertEquals(7357, numinfo("0b00000000000000000001110010111101ULL").getClong());
    assertEquals(7357, numinfo("0x00001cbdU").getClong());
    assertEquals(7357, numinfo("016275u").getClong());
    assertEquals(7357, numinfo("7357LLU").getClong());
    /**/
    assertEquals(6968, numinfo("6968").getClong());
    assertEquals(6968, numinfo("0b00000000000000000001101100111000ll").getClong());
    assertEquals(6968, numinfo("0x00001b38l").getClong());
    assertEquals(6968, numinfo("015470LL").getClong());
    assertEquals(6968, numinfo("6968llu").getClong());
    /**/
    assertEquals(30711, numinfo("30711").getClong());
    assertEquals(30711, numinfo("0b00000000000000000111011111110111LU").getClong());
    assertEquals(30711, numinfo("0x000077f7ull").getClong());
    assertEquals(30711, numinfo("073767ll").getClong());
    assertEquals(30711, numinfo("30711U").getClong());
    /**/
    assertEquals(7272, numinfo("7272").getClong());
    assertEquals(7272, numinfo("0b00000000000000000001110001101000llu").getClong());
    assertEquals(7272, numinfo("0x00001c68LLU").getClong());
    assertEquals(7272, numinfo("016150LL").getClong());
    assertEquals(7272, numinfo("7272L").getClong());
    /**/
    assertEquals(21096, numinfo("21096").getClong());
    assertEquals(21096, numinfo("0b00000000000000000101001001101000u").getClong());
    assertEquals(21096, numinfo("0x00005268lu").getClong());
    assertEquals(21096, numinfo("051150LU").getClong());
    assertEquals(21096, numinfo("21096llu").getClong());
    /**/
    assertEquals(7416, numinfo("7416").getClong());
    assertEquals(7416, numinfo("0b00000000000000000001110011111000L").getClong());
    assertEquals(7416, numinfo("0x00001cf8ULL").getClong());
    assertEquals(7416, numinfo("016370ULL").getClong());
    assertEquals(7416, numinfo("7416ULL").getClong());
    /**/
    assertEquals(3055, numinfo("3055").getClong());
    assertEquals(3055, numinfo("0b00000000000000000000101111101111L").getClong());
    assertEquals(3055, numinfo("0x00000befll").getClong());
    assertEquals(3055, numinfo("05757LU").getClong());
    assertEquals(3055, numinfo("3055ul").getClong());
    /**/
    assertEquals(13165, numinfo("13165").getClong());
    assertEquals(13165, numinfo("0b00000000000000000011001101101101ll").getClong());
    assertEquals(13165, numinfo("0x0000336dull").getClong());
    assertEquals(13165, numinfo("031555L").getClong());
    assertEquals(13165, numinfo("13165L").getClong());
    /**/
    assertEquals(23506, numinfo("23506").getClong());
    assertEquals(23506, numinfo("0b00000000000000000101101111010010L").getClong());
    assertEquals(23506, numinfo("0x00005bd2ull").getClong());
    assertEquals(23506, numinfo("055722LLU").getClong());
    assertEquals(23506, numinfo("23506U").getClong());
    /**/
    assertEquals(7430, numinfo("7430").getClong());
    assertEquals(7430, numinfo("0b00000000000000000001110100000110l").getClong());
    assertEquals(7430, numinfo("0x00001d06lu").getClong());
    assertEquals(7430, numinfo("016406UL").getClong());
    assertEquals(7430, numinfo("7430LL").getClong());
    /**/
    assertEquals(23489, numinfo("23489").getClong());
    assertEquals(23489, numinfo("0b00000000000000000101101111000001ul").getClong());
    assertEquals(23489, numinfo("0x00005bc1L").getClong());
    assertEquals(23489, numinfo("055701lu").getClong());
    assertEquals(23489, numinfo("23489U").getClong());
    /**/
    assertEquals(19946, numinfo("19946").getClong());
    assertEquals(19946, numinfo("0b00000000000000000100110111101010LU").getClong());
    assertEquals(19946, numinfo("0x00004deall").getClong());
    assertEquals(19946, numinfo("046752UL").getClong());
    assertEquals(19946, numinfo("19946ll").getClong());
    /**/
    assertEquals(5704, numinfo("5704").getClong());
    assertEquals(5704, numinfo("0b00000000000000000001011001001000lu").getClong());
    assertEquals(5704, numinfo("0x00001648LU").getClong());
    assertEquals(5704, numinfo("013110u").getClong());
    assertEquals(5704, numinfo("5704lu").getClong());
    /**/
    assertEquals(31588, numinfo("31588").getClong());
    assertEquals(31588, numinfo("0b00000000000000000111101101100100ULL").getClong());
    assertEquals(31588, numinfo("0x00007b64U").getClong());
    assertEquals(31588, numinfo("075544ull").getClong());
    assertEquals(31588, numinfo("31588U").getClong());
    /**/
    assertEquals(2010, numinfo("2010").getClong());
    assertEquals(2010, numinfo("0b00000000000000000000011111011010LU").getClong());
    assertEquals(2010, numinfo("0x000007dallu").getClong());
    assertEquals(2010, numinfo("03732ll").getClong());
    assertEquals(2010, numinfo("2010LLU").getClong());
    /**/
    assertEquals(16264, numinfo("16264").getClong());
    assertEquals(16264, numinfo("0b00000000000000000011111110001000ll").getClong());
    assertEquals(16264, numinfo("0x00003f88ull").getClong());
    assertEquals(16264, numinfo("037610lu").getClong());
    assertEquals(16264, numinfo("16264ll").getClong());

    /**/
    assertEquals(13386, numinfo("13386").getClong());
    assertEquals(13386, numinfo("0b00000000000000000011010001001010l").getClong());
    assertEquals(13386, numinfo("0x0000344al").getClong());
    assertEquals(13386, numinfo("032112ll").getClong());
    assertEquals(13386, numinfo("13386l").getClong());
    /**/
    assertEquals(18166, numinfo("18166").getClong());
    assertEquals(18166, numinfo("0b00000000000000000100011011110110ul").getClong());
    assertEquals(18166, numinfo("0x000046f6LL").getClong());
    assertEquals(18166, numinfo("043366LL").getClong());
    assertEquals(18166, numinfo("18166U").getClong());
    /**/
    assertEquals(27813, numinfo("27813").getClong());
    assertEquals(27813, numinfo("0b00000000000000000110110010100101LLU").getClong());
    assertEquals(27813, numinfo("0x00006ca5u").getClong());
    assertEquals(27813, numinfo("066245UL").getClong());
    assertEquals(27813, numinfo("27813ul").getClong());
    /**/
    assertEquals(7477, numinfo("7477").getClong());
    assertEquals(7477, numinfo("0b00000000000000000001110100110101LU").getClong());
    assertEquals(7477, numinfo("0x00001d35L").getClong());
    assertEquals(7477, numinfo("016465u").getClong());
    assertEquals(7477, numinfo("7477u").getClong());
    /**/
    assertEquals(25856, numinfo("25856").getClong());
    assertEquals(25856, numinfo("0b00000000000000000110010100000000ll").getClong());
    assertEquals(25856, numinfo("0x00006500lu").getClong());
    assertEquals(25856, numinfo("062400LLU").getClong());
    assertEquals(25856, numinfo("25856UL").getClong());
    /**/
    assertEquals(30341, numinfo("30341").getClong());
    assertEquals(30341, numinfo("0b00000000000000000111011010000101ULL").getClong());
    assertEquals(30341, numinfo("0x00007685UL").getClong());
    assertEquals(30341, numinfo("073205LLU").getClong());
    assertEquals(30341, numinfo("30341LLU").getClong());
    /**/
    assertEquals(7628, numinfo("7628").getClong());
    assertEquals(7628, numinfo("0b00000000000000000001110111001100LL").getClong());
    assertEquals(7628, numinfo("0x00001dccl").getClong());
    assertEquals(7628, numinfo("016714l").getClong());
    assertEquals(7628, numinfo("7628L").getClong());
    /**/
    assertEquals(27656, numinfo("27656").getClong());
    assertEquals(27656, numinfo("0b00000000000000000110110000001000LU").getClong());
    assertEquals(27656, numinfo("0x00006c08U").getClong());
    assertEquals(27656, numinfo("066010L").getClong());
    assertEquals(27656, numinfo("27656LU").getClong());
    /**/
    assertEquals(14754, numinfo("14754").getClong());
    assertEquals(14754, numinfo("0b00000000000000000011100110100010LLU").getClong());
    assertEquals(14754, numinfo("0x000039a2U").getClong());
    assertEquals(14754, numinfo("034642llu").getClong());
    assertEquals(14754, numinfo("14754llu").getClong());
    /**/
    assertEquals(3411, numinfo("3411").getClong());
    assertEquals(3411, numinfo("0b00000000000000000000110101010011lu").getClong());
    assertEquals(3411, numinfo("0x00000d53l").getClong());
    assertEquals(3411, numinfo("06523lu").getClong());
    assertEquals(3411, numinfo("3411ll").getClong());
    /**/
    assertEquals(9250, numinfo("9250").getClong());
    assertEquals(9250, numinfo("0b00000000000000000010010000100010llu").getClong());
    assertEquals(9250, numinfo("0x00002422L").getClong());
    assertEquals(9250, numinfo("022042llu").getClong());
    assertEquals(9250, numinfo("9250LL").getClong());
    /**/
    assertEquals(14645, numinfo("14645").getClong());
    assertEquals(14645, numinfo("0b00000000000000000011100100110101u").getClong());
    assertEquals(14645, numinfo("0x00003935U").getClong());
    assertEquals(14645, numinfo("034465U").getClong());
    assertEquals(14645, numinfo("14645llu").getClong());
    /**/
    assertEquals(29538, numinfo("29538").getClong());
    assertEquals(29538, numinfo("0b00000000000000000111001101100010LU").getClong());
    assertEquals(29538, numinfo("0x00007362UL").getClong());
    assertEquals(29538, numinfo("071542ull").getClong());
    assertEquals(29538, numinfo("29538l").getClong());
    /**/
    assertEquals(6388, numinfo("6388").getClong());
    assertEquals(6388, numinfo("0b00000000000000000001100011110100l").getClong());
    assertEquals(6388, numinfo("0x000018f4UL").getClong());
    assertEquals(6388, numinfo("014364ull").getClong());
    assertEquals(6388, numinfo("6388llu").getClong());
    /**/
    assertEquals(5617, numinfo("5617").getClong());
    assertEquals(5617, numinfo("0b00000000000000000001010111110001LLU").getClong());
    assertEquals(5617, numinfo("0x000015f1LL").getClong());
    assertEquals(5617, numinfo("012761lu").getClong());
    assertEquals(5617, numinfo("5617l").getClong());
    /**/
    assertEquals(8019, numinfo("8019").getClong());
    assertEquals(8019, numinfo("0b00000000000000000001111101010011l").getClong());
    assertEquals(8019, numinfo("0x00001f53ull").getClong());
    assertEquals(8019, numinfo("017523LU").getClong());
    assertEquals(8019, numinfo("8019ul").getClong());
    /**/
    assertEquals(12179, numinfo("12179").getClong());
    assertEquals(12179, numinfo("0b00000000000000000010111110010011ULL").getClong());
    assertEquals(12179, numinfo("0x00002f93LU").getClong());
    assertEquals(12179, numinfo("027623lu").getClong());
    assertEquals(12179, numinfo("12179ULL").getClong());
    /**/
    assertEquals(6597, numinfo("6597").getClong());
    assertEquals(6597, numinfo("0b00000000000000000001100111000101ULL").getClong());
    assertEquals(6597, numinfo("0x000019c5llu").getClong());
    assertEquals(6597, numinfo("014705LU").getClong());
    assertEquals(6597, numinfo("6597ULL").getClong());
    /**/
    assertEquals(19926, numinfo("19926").getClong());
    assertEquals(19926, numinfo("0b00000000000000000100110111010110ull").getClong());
    assertEquals(19926, numinfo("0x00004dd6lu").getClong());
    assertEquals(19926, numinfo("046726llu").getClong());
    assertEquals(19926, numinfo("19926LL").getClong());
    /**/
    assertEquals(13623, numinfo("13623").getClong());
    assertEquals(13623, numinfo("0b00000000000000000011010100110111ull").getClong());
    assertEquals(13623, numinfo("0x00003537u").getClong());
    assertEquals(13623, numinfo("032467U").getClong());
    assertEquals(13623, numinfo("13623L").getClong());
    /**/
    assertEquals(3636, numinfo("3636").getClong());
    assertEquals(3636, numinfo("0b00000000000000000000111000110100ll").getClong());
    assertEquals(3636, numinfo("0x00000e34ull").getClong());
    assertEquals(3636, numinfo("07064LLU").getClong());
    assertEquals(3636, numinfo("3636ll").getClong());
    /**/
    assertEquals(25274, numinfo("25274").getClong());
    assertEquals(25274, numinfo("0b00000000000000000110001010111010LL").getClong());
    assertEquals(25274, numinfo("0x000062baLL").getClong());
    assertEquals(25274, numinfo("061272LLU").getClong());
    assertEquals(25274, numinfo("25274L").getClong());
    /**/
    assertEquals(9066, numinfo("9066").getClong());
    assertEquals(9066, numinfo("0b00000000000000000010001101101010ull").getClong());
    assertEquals(9066, numinfo("0x0000236alu").getClong());
    assertEquals(9066, numinfo("021552L").getClong());
    assertEquals(9066, numinfo("9066LU").getClong());
    /**/
    assertEquals(9561, numinfo("9561").getClong());
    assertEquals(9561, numinfo("0b00000000000000000010010101011001l").getClong());
    assertEquals(9561, numinfo("0x00002559LU").getClong());
    assertEquals(9561, numinfo("022531l").getClong());
    assertEquals(9561, numinfo("9561L").getClong());
    /**/
    assertEquals(19239, numinfo("19239").getClong());
    assertEquals(19239, numinfo("0b00000000000000000100101100100111l").getClong());
    assertEquals(19239, numinfo("0x00004b27llu").getClong());
    assertEquals(19239, numinfo("045447LLU").getClong());
    assertEquals(19239, numinfo("19239LLU").getClong());
    /**/
    assertEquals(17939, numinfo("17939").getClong());
    assertEquals(17939, numinfo("0b00000000000000000100011000010011L").getClong());
    assertEquals(17939, numinfo("0x00004613LL").getClong());
    assertEquals(17939, numinfo("043023U").getClong());
    assertEquals(17939, numinfo("17939U").getClong());
    /**/
    assertEquals(24385, numinfo("24385").getClong());
    assertEquals(24385, numinfo("0b00000000000000000101111101000001ull").getClong());
    assertEquals(24385, numinfo("0x00005f41LU").getClong());
    assertEquals(24385, numinfo("057501u").getClong());
    assertEquals(24385, numinfo("24385u").getClong());
    /**/
    assertEquals(107, numinfo("107").getClong());
    assertEquals(107, numinfo("0b00000000000000000000000001101011LLU").getClong());
    assertEquals(107, numinfo("0x0000006bll").getClong());
    assertEquals(107, numinfo("0153lu").getClong());
    assertEquals(107, numinfo("107L").getClong());
    /**/
    assertEquals(853, numinfo("853").getClong());
    assertEquals(853, numinfo("0b00000000000000000000001101010101UL").getClong());
    assertEquals(853, numinfo("0x00000355UL").getClong());
    assertEquals(853, numinfo("01525u").getClong());
    assertEquals(853, numinfo("853ul").getClong());
    /**/
    assertEquals(10574, numinfo("10574").getClong());
    assertEquals(10574, numinfo("0b00000000000000000010100101001110U").getClong());
    assertEquals(10574, numinfo("0x0000294el").getClong());
    assertEquals(10574, numinfo("024516UL").getClong());
    assertEquals(10574, numinfo("10574ul").getClong());
    /**/
    assertEquals(12058, numinfo("12058").getClong());
    assertEquals(12058, numinfo("0b00000000000000000010111100011010LL").getClong());
    assertEquals(12058, numinfo("0x00002f1aull").getClong());
    assertEquals(12058, numinfo("027432L").getClong());
    assertEquals(12058, numinfo("12058LL").getClong());
    /**/
    assertEquals(1558, numinfo("1558").getClong());
    assertEquals(1558, numinfo("0b00000000000000000000011000010110LU").getClong());
    assertEquals(1558, numinfo("0x00000616UL").getClong());
    assertEquals(1558, numinfo("03026U").getClong());
    assertEquals(1558, numinfo("1558ul").getClong());
  }
}
