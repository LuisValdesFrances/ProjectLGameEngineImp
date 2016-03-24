package com.luis.projectlgameengineimp;


import com.luis.lgameengine.gameutils.MenuManager;
import com.luis.lgameengine.implementation.input.MultiTouchHandler2;

public class UserInput {
	
	//Teclas
	public static boolean isKeyLeft;
	public static boolean isKeyRight;
	public static boolean isKeyUp;
	public static boolean isKeyDown;
	public static boolean isKeyFire;
	public static boolean isSoftkeyLeft;
	public static boolean isSoftkeyRight;
	
	public static final int KEYCODE_UP = 19;
	public static final int KEYCODE_DOWN = 20;
	public static final int KEYCODE_LEFT = 21;
	public static final int KEYCODE_RIGHT = 22;
	public static final int KEYCODE_FIRE = 23;
	public static final int KEYCODE_NUM2 = 50;
	public static final int KEYCODE_NUM8 = 56;
	public static final int KEYCODE_NUM9 = 57;
	public static final int KEYCODE_NUM4 = 52;
	public static final int KEYCODE_NUM6 = 54;
	public static final int KEYCODE_NUM5 = 12;
	public static final int KEYCODE_SK_LEFT = -6;
	public static final int KEYCODE_SK_RIGHT = -7;
	public static final int KEYCODE_CLEAR = -8;
	public static final int KEYCODE_CALL = 5;
	public static final int KEYCODE_ENDCALL = 6;
	public static final int KEYCODE_ENTER = 66;
	
	//Controls for pads
	public static final int KEYCODE_JXD_DOWN = 97;
	public static final int KEYCODE_JXD_RIGHT = 96;
	public static final int KEYCODE_JXD_UP = 99;
	public static final int KEYCODE_JXD_LEFT = 100;
	
	
	public static void resetKeys(){
    	isKeyLeft=false;
    	isKeyRight=false;
    	isKeyUp=false;
    	isKeyDown=false;
    	isKeyFire=false;
    	isSoftkeyLeft=false;
    	isSoftkeyRight=false;
    }
	
	
	
   //Controlador de foco
    public static boolean isTouchSoftRight(int _iPadingX, int _iPadingY) {
    	int upBanner = Main.IS_MOVE_SOFT_BANNER?(GfxManager.vImgSoftkeys.getHeight() >> 1):0;
		return compareTouch(Define.SIZEX - (GfxManager.vImgSoftkeys.getWidth()>>1), 
				Define.SIZEY - (GfxManager.vImgSoftkeys.getHeight()>>1)-upBanner, 
				Define.SIZEX, Define.SIZEY-upBanner, 0);
	}
	//Lazador de estado
	public static boolean goToSoftRight(int _iPadingX, int _iPadingY) {
		int upBanner = Main.IS_MOVE_SOFT_BANNER?(GfxManager.vImgSoftkeys.getHeight() >> 1):0;
		return compareTouch(Define.SIZEX - (GfxManager.vImgSoftkeys.getWidth()>>1), 
				Define.SIZEY - (GfxManager.vImgSoftkeys.getHeight()>>1)-upBanner, 
				Define.SIZEX, Define.SIZEY-upBanner, 0);
	}
	
	//Controlador de foco
    public static boolean isTouchSoftLeft(int _iPadingX, int _iPadingY) {
    	int upBanner = Main.IS_MOVE_SOFT_BANNER?(GfxManager.vImgSoftkeys.getHeight() >> 1):0;
		return compareTouch(0, 
				Define.SIZEY - (GfxManager.vImgSoftkeys.getHeight()>>1)-upBanner, 
				GfxManager.vImgSoftkeys.getWidth()>>1, Define.SIZEY-upBanner, 0);
	}
	//Lazador de estado
	public static boolean goToSoftLeft(int _iPadingX, int _iPadingY) {
		int upBanner = Main.IS_MOVE_SOFT_BANNER?(GfxManager.vImgSoftkeys.getHeight() >> 1):0;
		return compareTouch(0, 
				Define.SIZEY - (GfxManager.vImgSoftkeys.getHeight()>>1)-upBanner, 
				GfxManager.vImgSoftkeys.getWidth()>>1, Define.SIZEY-upBanner, 0);
	}
    
    public static void putTouchDistance(int _iPoint){
    	MultiTouchHandler2.touchDistanceX[_iPoint] = MultiTouchHandler2.touchX[_iPoint] - MultiTouchHandler2.touchOriginX[_iPoint];
    	MultiTouchHandler2.touchDistanceY[_iPoint] = MultiTouchHandler2.touchY[_iPoint] - MultiTouchHandler2.touchOriginY[_iPoint];
    }
    
    public static boolean compareTouch(int _iX0, int _iY0, int _iX1, int _iY1, int _iPoint) {
        if ((MultiTouchHandler2.touchX[_iPoint] > _iX0
                && MultiTouchHandler2.touchX[_iPoint] < _iX1)
                && (MultiTouchHandler2.touchY[_iPoint] > _iY0
                && MultiTouchHandler2.touchY[_iPoint] < _iY1)) {
        	return true;
        } else {
            return false;
        }
    }

    public static final int SIDE_BUTTON_Y_LEFT = 0;
    public static final int SIDE_BUTTON_Y_RIGHT = 1;
    //Controles de Menu:
    public static boolean getOptionMenuTouched_X(int pos_button, int side_option) {

        // Obtenemos la posición y dependiendo de donde se haya ubicaod el
        // botón:
        int posY = (((Define.SIZEY - (GfxManager.vImgSoftkeys.getHeight() >> 1)) >> 1))
                - (GfxManager.vImgMenuButtons.getHeight() >> 2);// Centro

        switch (pos_button) {
            case 0:// Arriba
                posY -= MenuManager.iSepBottonsX;
                break;
            case 2:// Abajo
                posY += MenuManager.iSepBottonsX;
                break;

        }

        // Obtenemos la posición x dependiendo de si se va a "escuchar" la pestaña izq o la pestaña der.
        int posX = 0;

        switch (side_option) {
            case SIDE_BUTTON_Y_LEFT:
                posX = (Define.SIZEX2)
                        - (GfxManager.vImgMenuButtons.getWidth() >> 1)
                        - GfxManager.vImgMenuArrows.getWidth();

                break;
            case SIDE_BUTTON_Y_RIGHT:
                posX = (Define.SIZEX2);
                break;

        }
        if (compareTouch(posX, posY,
        		(posX + (GfxManager.vImgMenuButtons.getWidth() >> 1)) + GfxManager.vImgMenuArrows.getWidth(), 
        		posY + (GfxManager.vImgMenuButtons.getHeight() >> 1), 0) 
        		&& MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_UP) {

            //SndManager.playFX(SndManager.FX_BLOCK);
        	MultiTouchHandler2.resetTouch();
            return true;
        } else {
            return false;
        }
    }
    public static int iFirstValidTouch=-1;

    public static int getOptionMenuTouched_Y(int number_options, int iCurrentOption) {

        int selectOption = iCurrentOption;

        if (MenuManager.iListPosY != null) {
            if (MenuManager.iListPosY.length == number_options) {
                for (byte i = 0; i < number_options; i++) {
                    if (compareTouch(
                            (Define.SIZEX >> 1)- (GfxManager.vImgMenuButtons.getWidth() >> 1),
                            MenuManager.iListPosY[i]- (GfxManager.vImgMenuButtons.getHeight() >> 2),
                            (Define.SIZEX2 - (GfxManager.vImgMenuButtons.getWidth() >> 1))+ GfxManager.vImgMenuButtons.getWidth(),
                            (MenuManager.iListPosY[i]) + (GfxManager.vImgMenuButtons.getHeight() >> 1), 0)) {
                        selectOption = i;
                        if (MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_DOWN) {
                            iFirstValidTouch = i;
                        }
                        break;
                    }
                }
            }
        }
        //if (iCurrentOption != selectOption) {
        //SndManager.playFX(SndManager.FX_BAR);
        //}
        return selectOption;
    }

    // Esto sirve para que en los menus de tipo vertical(y) cuando se llame a
    // pointerReleased,
    // se entre en la opcion seleccionada SIEMPE Y CUANDO EL DEDO ESTE ENCIMA
    // de la opción.
    public static boolean getOkTouched_Y(int confirmedOption) {
        boolean t = false;

        if (MenuManager.iListPosY != null) {

            if (compareTouch(
                    Define.SIZEX2 - (GfxManager.vImgMenuButtons.getWidth() >> 1),
                    MenuManager.iListPosY[confirmedOption] - GfxManager.vImgMenuButtons.getHeight() >> 2,
                    (Define.SIZEX2 - (GfxManager.vImgMenuButtons.getWidth() >> 1)) + GfxManager.vImgMenuButtons.getWidth(),
                    (MenuManager.iListPosY[confirmedOption]) + (GfxManager.vImgMenuButtons.getHeight() >> 1), 0)
                    && MultiTouchHandler2.touchAction[0] == MultiTouchHandler2.ACTION_UP
                    && iFirstValidTouch == confirmedOption) {
                t = true;

            }
        }
        //if(t)SndManager.playFX(SndManager.FX_CONFIRM);

        return t;
    }
}
