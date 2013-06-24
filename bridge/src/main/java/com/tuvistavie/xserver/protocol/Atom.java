package com.tuvistavie.xserver.backend.protocol;

public enum Atom {
  AnyProperty        (0),
  Primary            (1),
  Secondary          (2),
  Arc                (3),
  AtomObj            (4),
  Bitmap             (5),
  Cardinal           (6),
  Colormap           (7),
  Cursor             (8),
  CutBuffer0         (9),
  CutBuffer1         (10),
  CutBuffer2         (11),
  CutBuffer3         (12),
  CutBuffer4         (13),
  CutBuffer5         (14),
  CutBuffer6         (15),
  CutBuffer7         (16),
  Drawable           (17),
  Font               (18),
  Integer            (19),
  Pixmap             (20),
  Point              (21),
  Rectangle          (22),
  ResourceManager    (23),
  RgbColorMap        (24),
  RgbBestMap         (25),
  RgbBlueMap         (26),
  RgbDefaultMap      (27),
  RgbGrayMap         (28),
  RgbGreenMap        (29),
  RgbRedMap          (30),
  String             (31),
  Visualid           (32),
  Window             (33),
  WmCommand          (34),
  WmHints            (35),
  WmClientMachine    (36),
  WmIconName         (37),
  WmIconSize         (38),
  WmName             (39),
  WmNormalHints      (40),
  WmSizeHints        (41),
  WmZoomHints        (42),
  MinSpace           (43),
  NormSpace          (44),
  MaxSpace           (45),
  EndSpace           (46),
  SuperscriptX       (47),
  SuperscriptY       (48),
  SubscriptX         (49),
  SubscriptY         (50),
  UnderlinePosition  (51),
  UnderlineThickness (52),
  StrikeoutAscent    (53),
  StrikeoutDescent   (54),
  ItalicAngle        (55),
  XHeight            (56),
  QuadWidth          (57),
  Weight             (58),
  PointSize          (59),
  Resolution         (60),
  Copyright          (61),
  Notice             (62),
  FontName           (63),
  FamilyName         (64),
  FullName           (65),
  CapHeight          (66),
  WmClass            (67),
  WmTransientFor     (68);

  private int id;

  private static final Atom[] valuesCopy = values();

  Atom(int id) {
    this.id = id;
  }

  public int id() {
    return this.id;
  }

  public static Atom fromValue(int id) {
    for(Atom atom : valuesCopy) {
      if(atom.id() == id) {
        return atom;
      }
    }
    return Atom.AnyProperty;
  }

}
