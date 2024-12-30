package net.jterminal.ui.util;

import org.jetbrains.annotations.Nullable;

public class BoxCharacter {

  //top 0-3, right 0-3, bottom 0-3, left 0-3
  public static final char[][][][] CHARS = new char[][][][] {
      { //top0
          { //right0
              //bottom0
              {' ', '╴', '╸', '═'},
              //bottom1
              {'╷', '┐', '┑', '╕'},
              //bottom2
              {'╻', '┒', '┓', '╕'},
              //bottom3
              {'║', '╖', '╖', '╗'}
          },
          { //right1
              //bottom0
              {'╶', '─', '╾', '═'},
              //bottom1
              {'┌', '┬', '┭', '╤'},
              //bottom2
              {'┎', '┰', '┱', '╤'},
              //bottom3
              {'╓', '╥', '╥', '╦'}
          },
          { //right2
              //bottom0
              {'╺', '╼', '━', '═'},
              //bottom1
              {'┍', '┮', '┯', '╤'},
              //bottom2
              {'┏', '┲', '┳', '╦'},
              //bottom3
              {'╓', '╥', '╥', '╦'}
          },
          { //right3
              //bottom0
              {'═', '═', '═', '═'},
              //bottom1
              {'╒', '╦', '╦', '╤'},
              //bottom2
              {'╒', '╦', '╦', '╤'},
              //bottom3
              {'╔', '╦', '╦', '╦'}
          }
      },
      { //top1
          { //right0
              //bottom0
              {'╵', '┘', '┙', '╛'},
              //bottom1
              {'│', '┤', '┥', '╡'},
              //bottom2
              {'╽', '┧', '┪', '╡'},
              //bottom3
              {'║', '╢', '╢', '╣'}
          },
          { //right1
              //bottom0
              {'└', '┴', '┵', '╩'},
              //bottom1
              {'├', '┼', '┽', '╪'},
              //bottom2
              {'┟', '╁', '╅', '╪'},
              //bottom3
              {'╟', '╫', '╫', '╬'}
          },
          { //right2
              //bottom0
              {'┕', '┶', '┷', '╧'},
              //bottom1
              {'┝', '┾', '┿', '╪'},
              //bottom2
              {'┢', '╆', '╈', '╪'},
              //bottom3
              {'╟', '╫', '╫', '╬'}
          },
          { //right3
              //bottom0
              {'╘', '╧', '╧', '╧'},
              //bottom1
              {'╞', '╪', '╪', '╪'},
              //bottom2
              {'╞', '╪', '╪', '╪'},
              //bottom3
              {'╬', '╬', '╬', '╬'}
          }
      },
      { //top2
          { //right0
              //bottom0
              {'╹', '┚', '┛', '╛'},
              //bottom1
              {'╿', '┦', '┩', '╡'},
              //bottom2
              {'┃', '┨', '┫', '╡'},
              //bottom3
              {'║', '╢', '╢', '╣'}
          },
          { //right1
              //bottom0
              {'┖', '┸', '┹', '╧'},
              //bottom1
              {'┞', '╀', '╃', '╪'},
              //bottom2
              {'┠', '╂', '╉', '╪'},
              //bottom3
              {'╟', '╫', '╫', '╬'}
          },
          { //right2
              //bottom0
              {'┗', '┺', '┻', '╧'},
              //bottom1
              {'┡', '╄', '╇', '╪'},
              //bottom2
              {'┣', '╊', '╋', '╪'},
              //bottom3
              {'╟', '╫', '╫', '╬'}
          },
          { //right3
              //bottom0
              {'╘', '╧', '╧', '╧'},
              //bottom1
              {'╞', '╪', '╪', '╪'},
              //bottom2
              {'╞', '╪', '╪', '╪'},
              //bottom3
              {'╠', '╬', '╬', '╬'}
          }
      },
      { //top3
          { //right0
              //bottom0
              {'║', '╜', '╜', '╝'},
              //bottom1
              {'║', '╢', '╢', '╣'},
              //bottom2
              {'║', '╢', '╢', '╣'},
              //bottom3
              {'║', '╢', '╢', '╣'}
          },
          { //right1
              //bottom0
              {'╙', '╨', '╨', '╩'},
              //bottom1
              {'╟', '╫', '╫', '╬'},
              //bottom2
              {'╟', '╫', '╫', '╬'},
              //bottom3
              {'╟', '╫', '╫', '╬'}
          },
          { //right2
              //bottom0
              {'╙', '╨', '╨', '╩'},
              //bottom1
              {'╟', '╫', '╫', '╬'},
              //bottom2
              {'╟', '╫', '╫', '╬'},
              //bottom3
              {'╟', '╫', '╫', '╬'}
          },
          { //right3
              //bottom0
              {'╚', '╩', '╩', '╩'},
              //bottom1
              {'╠', '╬', '╬', '╬'},
              //bottom2
              {'╠', '╬', '╬', '╬'},
              //bottom3
              {'╠', '╬', '╬', '╬'}
          }
      }
  };

  public enum Type {
    NORMAL,
    BRIGHT,
    DOUBLE
  }

  private static int indexFrom(@Nullable Type type) {
    if(type == null) {
      return 0;
    }
    return type.ordinal() + 1;
  }

  public static char createChar(@Nullable Type top,
      @Nullable Type right, @Nullable Type bottom, @Nullable Type left) {
    return CHARS[indexFrom(top)][indexFrom(right)][indexFrom(bottom)][indexFrom(left)];
  }

  public static char createChar(byte[] data) {
    return CHARS[data[0]][data[1]][data[2]][data[3]];
  }

}
