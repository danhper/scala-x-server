package com.tuvistavie.xserver.protocol;

public enum Atom {
  NIL                  (0),
  PRIMARY              (1),
  SECONDARY            (2),
  ARC                  (3),
  ATOM_OBJ             (4),
  BITMAP               (5),
  CARDINAL             (6),
  COLORMAP             (7),
  CURSOR               (8),
  CUT_BUFFER_0         (9),
  CUT_BUFFER_1         (10),
  CUT_BUFFER_2         (11),
  CUT_BUFFER_3         (12),
  CUT_BUFFER_4         (13),
  CUT_BUFFER_5         (14),
  CUT_BUFFER_6         (15),
  CUT_BUFFER_7         (16),
  DRAWABLE             (17),
  FONT                 (18),
  INTEGER              (19),
  PIXMAP               (20),
  POINT                (21),
  RECTANGLE            (22),
  RESOURCE_MANAGER     (23),
  RGB_COLORMAP         (24),
  RGB_BEST_MAP         (25),
  RGB_BLUE_MAP         (26),
  RGB_DEFAULT_MAP      (27),
  RGB_GRAY_MAP         (28),
  RGB_GREEN_MAP        (29),
  RGB_RED_MAP          (30),
  STRING               (31),
  VISUALID             (32),
  WINDOW               (33),
  WM_COMMAND           (34),
  WM_HINTS             (35),
  WM_CLIENTMACHINE     (36),
  WM_ICONNAME          (37),
  WM_ICONSIZE          (38),
  WM_NAME              (39),
  WM_NORMALHINTS       (40),
  WM_SIZEHINTS         (41),
  WM_ZOOMHINTS         (42),
  MIN_SPACE            (43),
  NORM_SPACE           (44),
  MAX_SPACE            (45),
  END_SPACE            (46),
  SUPERSCRIPT_X        (47),
  SUPERSCRIPT_Y        (48),
  SUBSCRIPT_X          (49),
  SUBSCRIPT_Y          (50),
  UNDERLINE_POSITION   (51),
  UNDERLINE_THICKNESS  (52),
  STRIKEOUT_ASCENT     (53),
  STRIKEOUT_DESCENT    (54),
  ITALIC_ANGLE         (55),
  X_HEIGHT             (56),
  QUAD_WIDTH           (57),
  WEIGHT               (58),
  POINT_SIZE           (59),
  RESOLUTION           (60),
  COPYRIGHT            (61),
  NOTICE               (62),
  FONT_NAME            (63),
  FAMILY_NAME          (64),
  FULL_NAME            (65),
  CAP_HEIGHT           (66),
  WM_CLASS             (67),
  WM_TRANSIENT_FOR     (68);

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
    return Atom.NIL;
  }

}
