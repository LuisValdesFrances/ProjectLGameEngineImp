package com.luis.projectlgameengineimp.objects;

/**
 * 
 * @author Luis Valdes Frances
 * 
 * Todos los objetos que extiendan estan clases estarán expuestas a la fisica en 2D en cuanto a efectos de la gravedad
 * y colisiones. 
 * El punto de anclaje con el que se realizan las colisiones es abajo-centro
 *
 */
public class RigidBody {
	
	public static boolean isBoundColToScreen = true;
	public static boolean isFastColision = true;
	
	public static float transformUnityValue(float tileSize, float value){
		return (tileSize * value) / 0.96f;
	}
	
	public float weight;

    /*
    Antiguas fuerzas
    */
    private float speedX;
    private float speedY;

    public float getSpeedX()
    {
        return speedX;
    }
    public float getSpeedY()
    {
        return speedY;
    }

    public void setSpeedX(float newSpeedX)
    {
        this.speedX = newSpeedX;
    }
    public void setSpeedY(float newSpeedY)
    {
        this.speedY = newSpeedY;
    }

    float posX;
    float posY;
    float width;
    float height;
    private float newPosX;
    private float newPosY;
    public static float gravityForce;

    private float moveX;
    private float moveY;

    public float GetMoveX()
    {
        return moveX;
    }
    public void SetMoveX(float newMoveX)//TC
    {
        this.moveX = newMoveX;
    }
    public float GetMoveY()
    {
        return moveY;
    }
    public void SetMoveY(float newMoveY)
    {
        this.moveY = newMoveY;
    }
   

    /*
    Debug
    */
    private boolean isColUp;
    private boolean isColBotton;
    private boolean isColRight;
    private boolean isColLeft;


    /*
    Colision side
    */
    //Dimension 0
    protected static int COL_RIGHT = 0;
    protected static int COL_LEFT = 1;
    protected static int COL_TOP = 2;
    protected static int COL_BOTTON = 3;
    //Dimension 1
    protected static int COL_X = 0;
    protected static int COL_Y = 1;
    protected static int COL_WIDTH = 2;
    protected static int COL_HEIGHT = 3;

    private float[][] colisionDataPosition = new float[COL_BOTTON + 1][COL_HEIGHT + 1];
    /*
    */
    protected void init(float posX, float posY, float width, float height)
    {
        ResetDataColisions();

        this.speedX = 0f;
        this.speedY = 0f;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.newPosX = posX;
        this.newPosY = posY;
    }

    protected void runPhysics (float deltaTime, int[][] _iTilesMatrixID, float _fTileW, float _fTileH, int _iScreenW, int _iScreenH)
    {

        //Aplico la gravedad
        RunGravity(deltaTime, _fTileW, _fTileH);

        //Aplico las distintas fuerzas
        RunForceX(deltaTime);

        //Resolve colisions
        CheckColision(_iTilesMatrixID, _fTileW, _fTileH, _iScreenW, _iScreenH);
        
        //Aplico las consecuencias de colisionar con los tiles
        AppliColisions();

        //Si se le esta aplicando velocidad, la decremento
        DecrementSpeed();
    }

    protected void RunGravity(float deltaTime, float _fTileW, float _fTileH)
    {
        {
            float cAcceleration = gravityForce * weight;
            /*
            La aceleracion es la modificacion de la velocidad (una cantidad), asi que tambien debe de multiplicarse
            por el delta time, ya que si el movil hace menos pasadas de frames, esta modificacion, aplicara menos
            veces y dismuira mas lentamente.
            */
            speedY += (cAcceleration * deltaTime);

            float movement = speedY * deltaTime;

            /*
            Limitador para que la aceleracion no crezca mas de un cierto valor
            */
            if (movement > _fTileH / 4f)
            {
                movement = _fTileH / 4f;
            }
            //

            newPosY = posY + movement;
        }
    }

    private void RunForceX(float deltaTime)
    {
        if (speedX != 0f)
        {
            float cAcceleration = (gravityForce/2f) * weight;

            float rest = (cAcceleration * deltaTime); ;
            if (!isColisionBotton())
            {
                rest = rest / 2f;
            }

            if (speedX > 0)
            {
                speedX -= rest;
                if (speedX < 0) speedX = 0;
            }
            else
            {
                speedX += rest;
                if (speedX > 0) speedX = 0;
            }

            float mov = speedX * deltaTime;
            newPosX = posX + mov;
        }
    }

    void ResetDataColisions()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                colisionDataPosition[i][j] = -1;
            }
        }
        isColUp = false;
        isColBotton = false;
        isColRight = false;
        isColLeft = false;
    }
    
    
    public boolean isColisionBotton(float objX1, float objY1, float objW1, float objH1,
    								float objX2, float objY2, float objW2, float objH2){
    		if(isColisionBottonX(objX1, objW1, objX2, objW2) && isColisionBottonY(objY1, objH1, objY2, objH2)){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public boolean isColisionBottonX(
            float objX1, float objW1,
            float objX2, float objW2){
        if (objX1 + (objW1 / 2f) >= objX2 - (objW2 / 2f) && objX1 - (objW1 / 2f)  <= objX2 + (objW2 / 2f)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isColisionBottonY(
            float objY1, float objH1,
            float objY2, float objH2){
        if (objY1 >= objY2 - objH2 && objY1 - objH1 <= objY2){
            return true;
        }
        else
        {
        	return false;
        }
    }
    
    private int[] indexX = new int[2];
    private int[] indexY = new int[2];
    private void PutIndexX(float tileW, int numColums, float screenX){
	    if(isBoundColToScreen)
	    {
	    	
	        float minX = getPosX() - screenX / 2f;
	        float maxX = getPosX() + screenX / 2f;
	        float levelWidth = numColums * tileW;
	        indexX[0] = (int)((minX * numColums) / levelWidth);
	        indexX[1] = (int)((maxX * numColums) / levelWidth);
	
	        if (indexX[0] < 0) indexX[0] = 0;
	        else if (indexX[1] > numColums) indexX[1] = numColums;
	    }else{
	    	indexX[0] = 0;
	    	indexX[1] = numColums;
	    }
    }

    private void PutIndexY(float tileH, int numFiles, float screenY){
    	if(isBoundColToScreen)
	    {
	        float minY = getPosY() - screenY / 2f;
	        float maxY = getPosY() + screenY / 2f;
	        float levelHeight = numFiles * tileH;
	        indexY[0] = (int)((minY * numFiles) / levelHeight);
	        indexY[1] = (int)((maxY * numFiles) / levelHeight);
	
	        if (indexY[0] < 0) indexY[0] = 0;
	        else if (indexY[1] > numFiles) indexY[1] = numFiles;
	    }else{
	    	indexY[0] = 0;
	    	indexY[1] = numFiles;
	    }
    }

    void CheckColision(int[][] _iTilesMatrixID, float _fTileW, float _fTileH, int _iScreenW, int _iScreenH)
    {
        PutIndexX(_fTileW, _iTilesMatrixID[0].length, _iScreenW);
        PutIndexY(_fTileH, _iTilesMatrixID.length, _iScreenH);
        ResetDataColisions();
        boolean colX = false;
        boolean colY = false;
        
        float distX = newPosX - getPosX();
        float distY = newPosY - getPosY();
        
        for (int f = indexY[0]; f < indexY[1] && (!colX || !colY); f++)
        {
            for (int c = indexX[0]; c < indexX[1] && (!colX || !colY); c++)
            {
            	if(_iTilesMatrixID[f][c] > 0)
                {
            		float tX = c * _fTileW;
                	float tY = f * _fTileH;
                    //String t = _iTilesMatrixID[f][c].GetTag();
                    {
                    	while(
                    			isColisionBottonY(getPosY(), getHeight(), tY, _fTileH)
                    			&&
                    			isColisionBottonX(getPosX() + distX, getWidth(), tX, _fTileW)
                    			&& distX != 0
                    			){
                    		//Solo se llama 1 vez dentro del bucle while
                    		if(!colX){
                    			colX = true;
                    			speedX = 0f;
                    			if(distX > 0){
                    				isColRight = true;
                    			}else{
                    				isColLeft = true;
                    			}
                    		}
                    		distX = distX / 2f;
                    	}
                    	newPosX = getPosX() + distX;
                    	
                    	
                    	while(
                    			isColisionBottonY(getPosY() + distY, getHeight(), tY, _fTileH)
                    			&&
                    			isColisionBottonX(getPosX() + distX, getWidth(), tX, _fTileW)
                    			&& distY != 0
                    			){
                    		//Solo se llama 1 vez dentro del bucle while
                    		if(!colY){
                    			colY = true;
                    			speedY = 0f;
                    			if(distY > 0){
                    				isColBotton = true;
                    			}else{
                    				isColUp = true;
                    			}
                    		}
                    		distY = distY / 2f;
                    	}
                    	newPosY = getPosY() + distY;
                    	
                    }
                    {
                    }
                }
            }
        }
    }
    
    protected void AppliColisions(){
        
        posX = newPosX + moveX;
        posY = newPosY + moveY;
        newPosX = posX;
        newPosY = posY;
    }

    public void DecrementSpeed()
    {
        moveX = 0;
        moveY = 0;
        /*
        if (speedX > 0f)
        {
            speedX -= ((speedX / 32f) + 0.0005f);
            if(speedX < 0f)
            {
                speedX = 0f;
            }
        }
        else if (speedX < 0f)
        {
            speedX -= ((speedX / 32f) - 0.0005f);
            if (speedX > 0f)
            {
                speedX = 0f;
            }
        }
        */
    }

    public boolean isColisionRight()
    {
        if (isColRight)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean isColisionLeft()
    {
        if (isColLeft)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean isColisionTop()
    {
        if (isColUp)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean isColisionBotton()
    {
        if(isColBotton)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public float getPosX()
    {
        return posX;
    }
    public void setPosX(float newPosX)
    {
        this.posX = newPosX;
        this.newPosX = this.posX;
    }

    public void movePosX(float newPosX)
    {
        this.newPosX = newPosX;
    }

    public void movePosY(float newPosY)
    {
        this.newPosY = newPosY;
    }

    public float getPosY()
    {
        return posY;
    }

    public void setPosY(float newPosY)
    {
        this.posY = newPosY;
        this.newPosY = this.posY;
    }
    public float getWidth()
    {
        return width;
    }
    public float getHeight()
    {
        return height;
    }

}
