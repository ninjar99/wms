package util;

import java.awt.*;
import javax.swing.*;
import java.awt.color.*;

public class ColorTranslate {
  ICC_Profile ICC_pf;
  ICC_ColorSpace ICC_ClSpace;
//���±����洢CMYK��ɫֵ��ȡֵΪ0��100
  int C = 9;
  int M = 9;
  int Y = 9;
  int K = 9;

//��ʼ��ICC_Profile��ICC_ColorSpace�����
  public ColorTranslate() {
    String Filename = "CMYK.pf";
    GetICCFrompfFile(Filename);
  }

  void GetICCFrompfFile(String Filename) {
    try {
      ICC_pf = ICC_Profile.getInstance(Filename);
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(null,
                                    "Can''t create ICC_Profile");
    }
    ICC_ClSpace = new ICC_ColorSpace(ICC_pf);
  }

  //��RGBɫ�ʿռ�任��CMYK
  public float[] RGBtoCMYK(Color RGBColor) {
    float[] CMYKfloat = ICC_ClSpace.fromRGB
        (RGBColor.getRGBComponents(null));
    C = (int) (CMYKfloat[0] * 100);
    M = (int) (CMYKfloat[1] * 100);
    Y = (int) (CMYKfloat[2] * 100);
    K = (int) (CMYKfloat[3] * 100);
    return CMYKfloat;
  }

//��CMYKɫ�ʿռ�任��RGB
  public Color CMYKtoRGB(float[] CMYKfloat) {
    Color RGBColor = new Color(ICC_ClSpace,
                               CMYKfloat, 1.0f);
    return RGBColor;
  }

  public Color CMYKtoRGB() {
    float[] CMYKfloat = new float[4];
    CMYKfloat[0] = 0.01f * (float) C;
    CMYKfloat[1] = 0.01f * (float) M;
    CMYKfloat[2] = 0.01f * (float) Y;
    CMYKfloat[3] = 0.01f * (float) K;
    Color RGBColor = new Color(ICC_ClSpace, CMYKfloat, 1.0f);
    return RGBColor;
  }
}
