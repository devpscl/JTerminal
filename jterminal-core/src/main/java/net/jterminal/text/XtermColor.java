package net.jterminal.text;

import java.awt.Color;
import net.jterminal.util.ColorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public enum XtermColor implements TerminalColor {

  COLOR_0(new Color(0, 0, 0)),
  COLOR_1(new Color(128, 0, 0)),
  COLOR_2(new Color(0, 128, 0)),
  COLOR_3(new Color(128, 128, 0)),
  COLOR_4(new Color(0, 0, 128)),
  COLOR_5(new Color(128, 0, 128)),
  COLOR_6(new Color(0, 128, 128)),
  COLOR_7(new Color(192, 192, 192)),
  COLOR_8(new Color(128, 128, 128)),
  COLOR_9(new Color(255, 0, 0)),
  COLOR_10(new Color(0, 255, 0)),
  COLOR_11(new Color(255, 255, 0)),
  COLOR_12(new Color(0, 0, 255)),
  COLOR_13(new Color(255, 0, 255)),
  COLOR_14(new Color(0, 255, 255)),
  COLOR_15(new Color(255, 255, 255)),
  COLOR_16(new Color(0, 0, 0)),
  COLOR_17(new Color(0, 0, 95)),
  COLOR_18(new Color(0, 0, 135)),
  COLOR_19(new Color(0, 0, 175)),
  COLOR_20(new Color(0, 0, 215)),
  COLOR_21(new Color(0, 0, 255)),
  COLOR_22(new Color(0, 95, 0)),
  COLOR_23(new Color(0, 95, 95)),
  COLOR_24(new Color(0, 95, 135)),
  COLOR_25(new Color(0, 95, 175)),
  COLOR_26(new Color(0, 95, 215)),
  COLOR_27(new Color(0, 95, 255)),
  COLOR_28(new Color(0, 135, 0)),
  COLOR_29(new Color(0, 135, 95)),
  COLOR_30(new Color(0, 135, 135)),
  COLOR_31(new Color(0, 135, 175)),
  COLOR_32(new Color(0, 135, 215)),
  COLOR_33(new Color(0, 135, 255)),
  COLOR_34(new Color(0, 175, 0)),
  COLOR_35(new Color(0, 175, 95)),
  COLOR_36(new Color(0, 175, 135)),
  COLOR_37(new Color(0, 175, 175)),
  COLOR_38(new Color(0, 175, 215)),
  COLOR_39(new Color(0, 175, 255)),
  COLOR_40(new Color(0, 215, 0)),
  COLOR_41(new Color(0, 215, 95)),
  COLOR_42(new Color(0, 215, 135)),
  COLOR_43(new Color(0, 215, 175)),
  COLOR_44(new Color(0, 215, 215)),
  COLOR_45(new Color(0, 215, 255)),
  COLOR_46(new Color(0, 255, 0)),
  COLOR_47(new Color(0, 255, 95)),
  COLOR_48(new Color(0, 255, 135)),
  COLOR_49(new Color(0, 255, 175)),
  COLOR_50(new Color(0, 255, 215)),
  COLOR_51(new Color(0, 255, 255)),
  COLOR_52(new Color(95, 0, 0)),
  COLOR_53(new Color(95, 0, 95)),
  COLOR_54(new Color(95, 0, 135)),
  COLOR_55(new Color(95, 0, 175)),
  COLOR_56(new Color(95, 0, 215)),
  COLOR_57(new Color(95, 0, 255)),
  COLOR_58(new Color(95, 95, 0)),
  COLOR_59(new Color(95, 95, 95)),
  COLOR_60(new Color(95, 95, 135)),
  COLOR_61(new Color(95, 95, 175)),
  COLOR_62(new Color(95, 95, 215)),
  COLOR_63(new Color(95, 95, 255)),
  COLOR_64(new Color(95, 135, 0)),
  COLOR_65(new Color(95, 135, 95)),
  COLOR_66(new Color(95, 135, 135)),
  COLOR_67(new Color(95, 135, 175)),
  COLOR_68(new Color(95, 135, 215)),
  COLOR_69(new Color(95, 135, 255)),
  COLOR_70(new Color(95, 175, 0)),
  COLOR_71(new Color(95, 175, 95)),
  COLOR_72(new Color(95, 175, 135)),
  COLOR_73(new Color(95, 175, 175)),
  COLOR_74(new Color(95, 175, 215)),
  COLOR_75(new Color(95, 175, 255)),
  COLOR_76(new Color(95, 215, 0)),
  COLOR_77(new Color(95, 215, 95)),
  COLOR_78(new Color(95, 215, 135)),
  COLOR_79(new Color(95, 215, 175)),
  COLOR_80(new Color(95, 215, 215)),
  COLOR_81(new Color(95, 215, 255)),
  COLOR_82(new Color(95, 255, 0)),
  COLOR_83(new Color(95, 255, 95)),
  COLOR_84(new Color(95, 255, 135)),
  COLOR_85(new Color(95, 255, 175)),
  COLOR_86(new Color(95, 255, 215)),
  COLOR_87(new Color(95, 255, 255)),
  COLOR_88(new Color(135, 0, 0)),
  COLOR_89(new Color(135, 0, 95)),
  COLOR_90(new Color(135, 0, 135)),
  COLOR_91(new Color(135, 0, 175)),
  COLOR_92(new Color(135, 0, 215)),
  COLOR_93(new Color(135, 0, 255)),
  COLOR_94(new Color(135, 95, 0)),
  COLOR_95(new Color(135, 95, 95)),
  COLOR_96(new Color(135, 95, 135)),
  COLOR_97(new Color(135, 95, 175)),
  COLOR_98(new Color(135, 95, 215)),
  COLOR_99(new Color(135, 95, 255)),
  COLOR_100(new Color(135, 135, 0)),
  COLOR_101(new Color(135, 135, 95)),
  COLOR_102(new Color(135, 135, 135)),
  COLOR_103(new Color(135, 135, 175)),
  COLOR_104(new Color(135, 135, 215)),
  COLOR_105(new Color(135, 135, 255)),
  COLOR_106(new Color(135, 175, 0)),
  COLOR_107(new Color(135, 175, 95)),
  COLOR_108(new Color(135, 175, 135)),
  COLOR_109(new Color(135, 175, 175)),
  COLOR_110(new Color(135, 175, 215)),
  COLOR_111(new Color(135, 175, 255)),
  COLOR_112(new Color(135, 215, 0)),
  COLOR_113(new Color(135, 215, 95)),
  COLOR_114(new Color(135, 215, 135)),
  COLOR_115(new Color(135, 215, 175)),
  COLOR_116(new Color(135, 215, 215)),
  COLOR_117(new Color(135, 215, 255)),
  COLOR_118(new Color(135, 255, 0)),
  COLOR_119(new Color(135, 255, 95)),
  COLOR_120(new Color(135, 255, 135)),
  COLOR_121(new Color(135, 255, 175)),
  COLOR_122(new Color(135, 255, 215)),
  COLOR_123(new Color(135, 255, 255)),
  COLOR_124(new Color(175, 0, 0)),
  COLOR_125(new Color(175, 0, 95)),
  COLOR_126(new Color(175, 0, 135)),
  COLOR_127(new Color(175, 0, 175)),
  COLOR_128(new Color(175, 0, 215)),
  COLOR_129(new Color(175, 0, 255)),
  COLOR_130(new Color(175, 95, 0)),
  COLOR_131(new Color(175, 95, 95)),
  COLOR_132(new Color(175, 95, 135)),
  COLOR_133(new Color(175, 95, 175)),
  COLOR_134(new Color(175, 95, 215)),
  COLOR_135(new Color(175, 95, 255)),
  COLOR_136(new Color(175, 135, 0)),
  COLOR_137(new Color(175, 135, 95)),
  COLOR_138(new Color(175, 135, 135)),
  COLOR_139(new Color(175, 135, 175)),
  COLOR_140(new Color(175, 135, 215)),
  COLOR_141(new Color(175, 135, 255)),
  COLOR_142(new Color(175, 175, 0)),
  COLOR_143(new Color(175, 175, 95)),
  COLOR_144(new Color(175, 175, 135)),
  COLOR_145(new Color(175, 175, 175)),
  COLOR_146(new Color(175, 175, 215)),
  COLOR_147(new Color(175, 175, 255)),
  COLOR_148(new Color(175, 215, 0)),
  COLOR_149(new Color(175, 215, 95)),
  COLOR_150(new Color(175, 215, 135)),
  COLOR_151(new Color(175, 215, 175)),
  COLOR_152(new Color(175, 215, 215)),
  COLOR_153(new Color(175, 215, 255)),
  COLOR_154(new Color(175, 255, 0)),
  COLOR_155(new Color(175, 255, 95)),
  COLOR_156(new Color(175, 255, 135)),
  COLOR_157(new Color(175, 255, 175)),
  COLOR_158(new Color(175, 255, 215)),
  COLOR_159(new Color(175, 255, 255)),
  COLOR_160(new Color(215, 0, 0)),
  COLOR_161(new Color(215, 0, 95)),
  COLOR_162(new Color(215, 0, 135)),
  COLOR_163(new Color(215, 0, 175)),
  COLOR_164(new Color(215, 0, 215)),
  COLOR_165(new Color(215, 0, 255)),
  COLOR_166(new Color(215, 95, 0)),
  COLOR_167(new Color(215, 95, 95)),
  COLOR_168(new Color(215, 95, 135)),
  COLOR_169(new Color(215, 95, 175)),
  COLOR_170(new Color(215, 95, 215)),
  COLOR_171(new Color(215, 95, 255)),
  COLOR_172(new Color(215, 135, 0)),
  COLOR_173(new Color(215, 135, 95)),
  COLOR_174(new Color(215, 135, 135)),
  COLOR_175(new Color(215, 135, 175)),
  COLOR_176(new Color(215, 135, 215)),
  COLOR_177(new Color(215, 135, 255)),
  COLOR_178(new Color(215, 175, 0)),
  COLOR_179(new Color(215, 175, 95)),
  COLOR_180(new Color(215, 175, 135)),
  COLOR_181(new Color(215, 175, 175)),
  COLOR_182(new Color(215, 175, 215)),
  COLOR_183(new Color(215, 175, 255)),
  COLOR_184(new Color(215, 215, 0)),
  COLOR_185(new Color(215, 215, 95)),
  COLOR_186(new Color(215, 215, 135)),
  COLOR_187(new Color(215, 215, 175)),
  COLOR_188(new Color(215, 215, 215)),
  COLOR_189(new Color(215, 215, 255)),
  COLOR_190(new Color(215, 255, 0)),
  COLOR_191(new Color(215, 255, 95)),
  COLOR_192(new Color(215, 255, 135)),
  COLOR_193(new Color(215, 255, 175)),
  COLOR_194(new Color(215, 255, 215)),
  COLOR_195(new Color(215, 255, 255)),
  COLOR_196(new Color(255, 0, 0)),
  COLOR_197(new Color(255, 0, 95)),
  COLOR_198(new Color(255, 0, 135)),
  COLOR_199(new Color(255, 0, 175)),
  COLOR_200(new Color(255, 0, 215)),
  COLOR_201(new Color(255, 0, 255)),
  COLOR_202(new Color(255, 95, 0)),
  COLOR_203(new Color(255, 95, 95)),
  COLOR_204(new Color(255, 95, 135)),
  COLOR_205(new Color(255, 95, 175)),
  COLOR_206(new Color(255, 95, 215)),
  COLOR_207(new Color(255, 95, 255)),
  COLOR_208(new Color(255, 135, 0)),
  COLOR_209(new Color(255, 135, 95)),
  COLOR_210(new Color(255, 135, 135)),
  COLOR_211(new Color(255, 135, 175)),
  COLOR_212(new Color(255, 135, 215)),
  COLOR_213(new Color(255, 135, 255)),
  COLOR_214(new Color(255, 175, 0)),
  COLOR_215(new Color(255, 175, 95)),
  COLOR_216(new Color(255, 175, 135)),
  COLOR_217(new Color(255, 175, 175)),
  COLOR_218(new Color(255, 175, 215)),
  COLOR_219(new Color(255, 175, 255)),
  COLOR_220(new Color(255, 215, 0)),
  COLOR_221(new Color(255, 215, 95)),
  COLOR_222(new Color(255, 215, 135)),
  COLOR_223(new Color(255, 215, 175)),
  COLOR_224(new Color(255, 215, 215)),
  COLOR_225(new Color(255, 215, 255)),
  COLOR_226(new Color(255, 255, 0)),
  COLOR_227(new Color(255, 255, 95)),
  COLOR_228(new Color(255, 255, 135)),
  COLOR_229(new Color(255, 255, 175)),
  COLOR_230(new Color(255, 255, 215)),
  COLOR_231(new Color(255, 255, 255)),
  COLOR_232(new Color(8, 8, 8)),
  COLOR_233(new Color(18, 18, 18)),
  COLOR_234(new Color(28, 28, 28)),
  COLOR_235(new Color(38, 38, 38)),
  COLOR_236(new Color(48, 48, 48)),
  COLOR_237(new Color(58, 58, 58)),
  COLOR_238(new Color(68, 68, 68)),
  COLOR_239(new Color(78, 78, 78)),
  COLOR_240(new Color(88, 88, 88)),
  COLOR_241(new Color(98, 98, 98)),
  COLOR_242(new Color(108, 108, 108)),
  COLOR_243(new Color(118, 118, 118)),
  COLOR_244(new Color(128, 128, 128)),
  COLOR_245(new Color(138, 138, 138)),
  COLOR_246(new Color(148, 148, 148)),
  COLOR_247(new Color(158, 158, 158)),
  COLOR_248(new Color(168, 168, 168)),
  COLOR_249(new Color(178, 178, 178)),
  COLOR_250(new Color(188, 188, 188)),
  COLOR_251(new Color(198, 198, 198)),
  COLOR_252(new Color(208, 208, 208)),
  COLOR_253(new Color(218, 218, 218)),
  COLOR_254(new Color(228, 228, 228)),
  COLOR_255(new Color(238, 238, 238));

  public static final XtermColor[] LIGHT_LEVEL_COLORS = new XtermColor[]{COLOR_232, COLOR_233, COLOR_234, COLOR_235,
      COLOR_236, COLOR_237, COLOR_238, COLOR_239, COLOR_240, COLOR_241, COLOR_242,
      COLOR_243, COLOR_244, COLOR_245, COLOR_246, COLOR_247, COLOR_248, COLOR_249,
      COLOR_250, COLOR_251, COLOR_252, COLOR_253, COLOR_254, COLOR_255, COLOR_231};

  private final Color color;
  private final String hexCode;

  XtermColor(@NotNull Color color) {
    this.color = color;
    this.hexCode = String.format("#%06x", 0xFFFFFF & color.getRGB());
  }

  public @NotNull String getHexCode() {
    return hexCode;
  }

  public static @NotNull XtermColor getColor(int id) {
    if(id < 0 || id > 255) {
      throw new IllegalArgumentException("Invalid color: " + id);
    }
    return values()[id];
  }

  public static @NotNull XtermColor getNearestTo(@NotNull Color color) {
    XtermColor[] colors = values();
    double nearestDistance = Double.MAX_VALUE;
    XtermColor nearestColor = colors[255];
    for (XtermColor value : values()) {
      double distance = ColorUtil.getDistance(color, value.toColor());
      if(distance < nearestDistance) {
        nearestColor = value;
        nearestDistance = distance;
      }
      if(distance == 0) {
        break;
      }
    }
    return nearestColor;
  }

  @Override
  public @NotNull String getBackgroundAnsiCode() {
    return "\u001b[48;5;" + ordinal() + "m";
  }

  @Override
  public @NotNull String getForegroundAnsiCode() {
    return "\u001b[38;5;" + ordinal() + "m";
  }

  @Override
  public @NotNull TerminalColor asUniversalColor() {
    return this;
  }

  @Override
  public @NotNull Color toColor() {
    return color;
  }

  @Override
  public boolean isDefault() {
    return false;
  }

  @Override
  public @NotNull String toString() {
    return color.toString();
  }

  public static @NotNull XtermColor getLightColor(@Range(from = 0, to = 1) double level) {
    if(level < 0.0D || level > 1.0D) {
      throw new IllegalArgumentException("Level is out of range (0-1): " + level);
    }
    int length = LIGHT_LEVEL_COLORS.length;
    int index = (int) ((length-1) * level);
    return LIGHT_LEVEL_COLORS[index];
  }

}
