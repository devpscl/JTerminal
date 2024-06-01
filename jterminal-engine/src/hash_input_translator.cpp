#include "../include/terminput.h"
//#############################################
//
//  Auto-Generated by keygen.py at 2024-06-01 15:29
//  
//#############################################


bool jterminal::translateInput(jterminal::InputEvent *event, const uint64_t& hash) {
  switch(hash) {
    case 97:
      event->keyboard.key = KEY_A;
      event->keyboard.state = KS_NONE;
      break;
    case 98:
      event->keyboard.key = KEY_B;
      event->keyboard.state = KS_NONE;
      break;
    case 99:
      event->keyboard.key = KEY_C;
      event->keyboard.state = KS_NONE;
      break;
    case 100:
      event->keyboard.key = KEY_D;
      event->keyboard.state = KS_NONE;
      break;
    case 101:
      event->keyboard.key = KEY_E;
      event->keyboard.state = KS_NONE;
      break;
    case 102:
      event->keyboard.key = KEY_F;
      event->keyboard.state = KS_NONE;
      break;
    case 103:
      event->keyboard.key = KEY_G;
      event->keyboard.state = KS_NONE;
      break;
    case 104:
      event->keyboard.key = KEY_H;
      event->keyboard.state = KS_NONE;
      break;
    case 105:
      event->keyboard.key = KEY_I;
      event->keyboard.state = KS_NONE;
      break;
    case 106:
      event->keyboard.key = KEY_J;
      event->keyboard.state = KS_NONE;
      break;
    case 107:
      event->keyboard.key = KEY_K;
      event->keyboard.state = KS_NONE;
      break;
    case 108:
      event->keyboard.key = KEY_L;
      event->keyboard.state = KS_NONE;
      break;
    case 109:
      event->keyboard.key = KEY_M;
      event->keyboard.state = KS_NONE;
      break;
    case 110:
      event->keyboard.key = KEY_N;
      event->keyboard.state = KS_NONE;
      break;
    case 111:
      event->keyboard.key = KEY_O;
      event->keyboard.state = KS_NONE;
      break;
    case 112:
      event->keyboard.key = KEY_P;
      event->keyboard.state = KS_NONE;
      break;
    case 113:
      event->keyboard.key = KEY_Q;
      event->keyboard.state = KS_NONE;
      break;
    case 114:
      event->keyboard.key = KEY_R;
      event->keyboard.state = KS_NONE;
      break;
    case 115:
      event->keyboard.key = KEY_S;
      event->keyboard.state = KS_NONE;
      break;
    case 116:
      event->keyboard.key = KEY_T;
      event->keyboard.state = KS_NONE;
      break;
    case 117:
      event->keyboard.key = KEY_U;
      event->keyboard.state = KS_NONE;
      break;
    case 118:
      event->keyboard.key = KEY_V;
      event->keyboard.state = KS_NONE;
      break;
    case 119:
      event->keyboard.key = KEY_W;
      event->keyboard.state = KS_NONE;
      break;
    case 120:
      event->keyboard.key = KEY_X;
      event->keyboard.state = KS_NONE;
      break;
    case 121:
      event->keyboard.key = KEY_Y;
      event->keyboard.state = KS_NONE;
      break;
    case 122:
      event->keyboard.key = KEY_Z;
      event->keyboard.state = KS_NONE;
      break;
    case 48:
      event->keyboard.key = KEY_0;
      event->keyboard.state = KS_NONE;
      break;
    case 49:
      event->keyboard.key = KEY_1;
      event->keyboard.state = KS_NONE;
      break;
    case 50:
      event->keyboard.key = KEY_2;
      event->keyboard.state = KS_NONE;
      break;
    case 51:
      event->keyboard.key = KEY_3;
      event->keyboard.state = KS_NONE;
      break;
    case 52:
      event->keyboard.key = KEY_4;
      event->keyboard.state = KS_NONE;
      break;
    case 53:
      event->keyboard.key = KEY_5;
      event->keyboard.state = KS_NONE;
      break;
    case 54:
      event->keyboard.key = KEY_6;
      event->keyboard.state = KS_NONE;
      break;
    case 55:
      event->keyboard.key = KEY_7;
      event->keyboard.state = KS_NONE;
      break;
    case 56:
      event->keyboard.key = KEY_8;
      event->keyboard.state = KS_NONE;
      break;
    case 57:
      event->keyboard.key = KEY_9;
      event->keyboard.state = KS_NONE;
      break;
    case 28836:
      event->keyboard.key = KEY_ARROW_LEFT;
      event->keyboard.state = KS_NONE;
      break;
    case 858545564:
      event->keyboard.key = KEY_ARROW_LEFT;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545657:
    case 28216:
      event->keyboard.key = KEY_ARROW_LEFT;
      event->keyboard.state = KS_CONTROL;
      break;
    case 28833:
      event->keyboard.key = KEY_ARROW_UP;
      event->keyboard.state = KS_NONE;
      break;
    case 858545561:
      event->keyboard.key = KEY_ARROW_UP;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545654:
    case 28213:
      event->keyboard.key = KEY_ARROW_UP;
      event->keyboard.state = KS_CONTROL;
      break;
    case 28834:
      event->keyboard.key = KEY_ARROW_DOWN;
      event->keyboard.state = KS_NONE;
      break;
    case 858545562:
      event->keyboard.key = KEY_ARROW_DOWN;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545655:
    case 28214:
      event->keyboard.key = KEY_ARROW_DOWN;
      event->keyboard.state = KS_CONTROL;
      break;
    case 28835:
      event->keyboard.key = KEY_ARROW_RIGHT;
      event->keyboard.state = KS_NONE;
      break;
    case 858545563:
      event->keyboard.key = KEY_ARROW_RIGHT;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545656:
    case 28215:
      event->keyboard.key = KEY_ARROW_RIGHT;
      event->keyboard.state = KS_CONTROL;
      break;
    case 127:
      event->keyboard.key = KEY_BACKSPACE;
      event->keyboard.state = KS_NONE;
      break;
    case 9:
      event->keyboard.key = KEY_TAB;
      event->keyboard.state = KS_NONE;
      break;
    case 13:
      event->keyboard.key = KEY_ENTER;
      event->keyboard.state = KS_NONE;
      break;
    case 27:
    case 10:
      event->keyboard.key = KEY_ESCAPE;
      event->keyboard.state = KS_NONE;
      break;
    case 32:
      event->keyboard.key = KEY_SPACE;
      event->keyboard.state = KS_NONE;
      break;
    case 28476:
    case 27694782:
      event->keyboard.key = KEY_F1;
      event->keyboard.state = KS_NONE;
      break;
    case 28477:
    case 27694813:
      event->keyboard.key = KEY_F2;
      event->keyboard.state = KS_NONE;
      break;
    case 28478:
    case 27694844:
      event->keyboard.key = KEY_F3;
      event->keyboard.state = KS_NONE;
      break;
    case 28479:
    case 27694875:
      event->keyboard.key = KEY_F4;
      event->keyboard.state = KS_NONE;
      break;
    case 27694906:
      event->keyboard.key = KEY_F5;
      event->keyboard.state = KS_NONE;
      break;
    case 27694968:
      event->keyboard.key = KEY_F6;
      event->keyboard.state = KS_NONE;
      break;
    case 27694999:
      event->keyboard.key = KEY_F7;
      event->keyboard.state = KS_NONE;
      break;
    case 27695030:
      event->keyboard.key = KEY_F8;
      event->keyboard.state = KS_NONE;
      break;
    case 27695712:
      event->keyboard.key = KEY_F9;
      event->keyboard.state = KS_NONE;
      break;
    case 27695743:
      event->keyboard.key = KEY_F10;
      event->keyboard.state = KS_NONE;
      break;
    case 27695805:
      event->keyboard.key = KEY_F11;
      event->keyboard.state = KS_NONE;
      break;
    case 27695836:
      event->keyboard.key = KEY_F12;
      event->keyboard.state = KS_NONE;
      break;
    case 858545576:
      event->keyboard.key = KEY_F1;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545577:
      event->keyboard.key = KEY_F2;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545578:
      event->keyboard.key = KEY_F3;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545579:
      event->keyboard.key = KEY_F4;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26614741955:
      event->keyboard.key = KEY_F5;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26614801537:
      event->keyboard.key = KEY_F6;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26614831328:
      event->keyboard.key = KEY_F7;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26614861119:
      event->keyboard.key = KEY_F8;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26615516521:
      event->keyboard.key = KEY_F9;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26615546312:
      event->keyboard.key = KEY_F10;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26615605894:
      event->keyboard.key = KEY_F11;
      event->keyboard.state = KS_SHIFT;
      break;
    case 26615635685:
      event->keyboard.key = KEY_F12;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545669:
      event->keyboard.key = KEY_F1;
      event->keyboard.state = KS_CONTROL;
      break;
    case 858545670:
      event->keyboard.key = KEY_F2;
      event->keyboard.state = KS_CONTROL;
      break;
    case 858545671:
      event->keyboard.key = KEY_F3;
      event->keyboard.state = KS_CONTROL;
      break;
    case 858545672:
      event->keyboard.key = KEY_F4;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26614742048:
      event->keyboard.key = KEY_F5;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26614801630:
      event->keyboard.key = KEY_F6;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26614831421:
      event->keyboard.key = KEY_F7;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26614861212:
      event->keyboard.key = KEY_F8;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26615516614:
      event->keyboard.key = KEY_F9;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26615546405:
      event->keyboard.key = KEY_F10;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26615605987:
      event->keyboard.key = KEY_F11;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26615635778:
      event->keyboard.key = KEY_F12;
      event->keyboard.state = KS_CONTROL;
      break;
    case 65:
      event->keyboard.key = KEY_A;
      event->keyboard.state = KS_SHIFT;
      break;
    case 66:
      event->keyboard.key = KEY_B;
      event->keyboard.state = KS_SHIFT;
      break;
    case 67:
      event->keyboard.key = KEY_C;
      event->keyboard.state = KS_SHIFT;
      break;
    case 68:
      event->keyboard.key = KEY_D;
      event->keyboard.state = KS_SHIFT;
      break;
    case 69:
      event->keyboard.key = KEY_E;
      event->keyboard.state = KS_SHIFT;
      break;
    case 70:
      event->keyboard.key = KEY_F;
      event->keyboard.state = KS_SHIFT;
      break;
    case 71:
      event->keyboard.key = KEY_G;
      event->keyboard.state = KS_SHIFT;
      break;
    case 72:
      event->keyboard.key = KEY_H;
      event->keyboard.state = KS_SHIFT;
      break;
    case 73:
      event->keyboard.key = KEY_I;
      event->keyboard.state = KS_SHIFT;
      break;
    case 74:
      event->keyboard.key = KEY_J;
      event->keyboard.state = KS_SHIFT;
      break;
    case 75:
      event->keyboard.key = KEY_K;
      event->keyboard.state = KS_SHIFT;
      break;
    case 76:
      event->keyboard.key = KEY_L;
      event->keyboard.state = KS_SHIFT;
      break;
    case 77:
      event->keyboard.key = KEY_M;
      event->keyboard.state = KS_SHIFT;
      break;
    case 78:
      event->keyboard.key = KEY_N;
      event->keyboard.state = KS_SHIFT;
      break;
    case 79:
      event->keyboard.key = KEY_O;
      event->keyboard.state = KS_SHIFT;
      break;
    case 80:
      event->keyboard.key = KEY_P;
      event->keyboard.state = KS_SHIFT;
      break;
    case 81:
      event->keyboard.key = KEY_Q;
      event->keyboard.state = KS_SHIFT;
      break;
    case 82:
      event->keyboard.key = KEY_R;
      event->keyboard.state = KS_SHIFT;
      break;
    case 83:
      event->keyboard.key = KEY_S;
      event->keyboard.state = KS_SHIFT;
      break;
    case 84:
      event->keyboard.key = KEY_T;
      event->keyboard.state = KS_SHIFT;
      break;
    case 85:
      event->keyboard.key = KEY_U;
      event->keyboard.state = KS_SHIFT;
      break;
    case 86:
      event->keyboard.key = KEY_V;
      event->keyboard.state = KS_SHIFT;
      break;
    case 87:
      event->keyboard.key = KEY_W;
      event->keyboard.state = KS_SHIFT;
      break;
    case 88:
      event->keyboard.key = KEY_X;
      event->keyboard.state = KS_SHIFT;
      break;
    case 89:
      event->keyboard.key = KEY_Y;
      event->keyboard.state = KS_SHIFT;
      break;
    case 90:
      event->keyboard.key = KEY_Z;
      event->keyboard.state = KS_SHIFT;
      break;
    case 1:
      event->keyboard.key = KEY_A;
      event->keyboard.state = KS_CONTROL;
      break;
    case 2:
      event->keyboard.key = KEY_B;
      event->keyboard.state = KS_CONTROL;
      break;
    case 3:
      event->keyboard.key = KEY_C;
      event->keyboard.state = KS_CONTROL;
      break;
    case 4:
      event->keyboard.key = KEY_D;
      event->keyboard.state = KS_CONTROL;
      break;
    case 5:
      event->keyboard.key = KEY_E;
      event->keyboard.state = KS_CONTROL;
      break;
    case 6:
      event->keyboard.key = KEY_F;
      event->keyboard.state = KS_CONTROL;
      break;
    case 7:
      event->keyboard.key = KEY_G;
      event->keyboard.state = KS_CONTROL;
      break;
    case 8:
      event->keyboard.key = KEY_H;
      event->keyboard.state = KS_CONTROL;
      break;
    case 11:
      event->keyboard.key = KEY_K;
      event->keyboard.state = KS_CONTROL;
      break;
    case 12:
      event->keyboard.key = KEY_L;
      event->keyboard.state = KS_CONTROL;
      break;
    case 14:
      event->keyboard.key = KEY_N;
      event->keyboard.state = KS_CONTROL;
      break;
    case 15:
      event->keyboard.key = KEY_O;
      event->keyboard.state = KS_CONTROL;
      break;
    case 16:
      event->keyboard.key = KEY_P;
      event->keyboard.state = KS_CONTROL;
      break;
    case 17:
      event->keyboard.key = KEY_Q;
      event->keyboard.state = KS_CONTROL;
      break;
    case 18:
      event->keyboard.key = KEY_R;
      event->keyboard.state = KS_CONTROL;
      break;
    case 19:
      event->keyboard.key = KEY_S;
      event->keyboard.state = KS_CONTROL;
      break;
    case 20:
      event->keyboard.key = KEY_T;
      event->keyboard.state = KS_CONTROL;
      break;
    case 21:
      event->keyboard.key = KEY_U;
      event->keyboard.state = KS_CONTROL;
      break;
    case 22:
      event->keyboard.key = KEY_V;
      event->keyboard.state = KS_CONTROL;
      break;
    case 23:
      event->keyboard.key = KEY_W;
      event->keyboard.state = KS_CONTROL;
      break;
    case 24:
      event->keyboard.key = KEY_X;
      event->keyboard.state = KS_CONTROL;
      break;
    case 25:
      event->keyboard.key = KEY_Y;
      event->keyboard.state = KS_CONTROL;
      break;
    case 26:
      event->keyboard.key = KEY_Z;
      event->keyboard.state = KS_CONTROL;
      break;
    case 893577:
      event->keyboard.key = KEY_PAGE_UP;
      event->keyboard.state = KS_NONE;
      break;
    case 858664786:
      event->keyboard.key = KEY_PAGE_UP;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858664879:
      event->keyboard.key = KEY_PAGE_UP;
      event->keyboard.state = KS_CONTROL;
      break;
    case 893608:
      event->keyboard.key = KEY_PAGE_DOWN;
      event->keyboard.state = KS_NONE;
      break;
    case 858694577:
      event->keyboard.key = KEY_PAGE_DOWN;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858694670:
      event->keyboard.key = KEY_PAGE_DOWN;
      event->keyboard.state = KS_CONTROL;
      break;
    case 28840:
    case 893453:
      event->keyboard.key = KEY_HOME;
      event->keyboard.state = KS_NONE;
      break;
    case 858545568:
      event->keyboard.key = KEY_HOME;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545661:
      event->keyboard.key = KEY_HOME;
      event->keyboard.state = KS_CONTROL;
      break;
    case 28838:
    case 893546:
      event->keyboard.key = KEY_END;
      event->keyboard.state = KS_NONE;
      break;
    case 858545566:
      event->keyboard.key = KEY_END;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858545659:
      event->keyboard.key = KEY_END;
      event->keyboard.state = KS_CONTROL;
      break;
    case 893484:
      event->keyboard.key = KEY_PASTE;
      event->keyboard.state = KS_NONE;
      break;
    case 858575413:
      event->keyboard.key = KEY_PASTE;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858575506:
      event->keyboard.key = KEY_PASTE;
      event->keyboard.state = KS_CONTROL;
      break;
    case 893515:
      event->keyboard.key = KEY_DELETE;
      event->keyboard.state = KS_NONE;
      break;
    case 858605204:
      event->keyboard.key = KEY_DELETE;
      event->keyboard.state = KS_SHIFT;
      break;
    case 858605297:
      event->keyboard.key = KEY_DELETE;
      event->keyboard.state = KS_CONTROL;
      break;

    default:
      return false;
  }
  return true;
}
