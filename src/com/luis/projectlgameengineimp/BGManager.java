package com.luis.projectlgameengineimp;

import com.luis.lgameengine.implementation.graphics.Graphics;

public class BGManager {
	private float[] fPosX;
	private float fCamX;
	
	private float[] fModY;
	
	public BGManager(){
		fPosX = new float[GfxManager.vImgBGs.length];
		fModY = new float[GfxManager.vImgBGs.length];
		
		for(int i = 0; i < GfxManager.vImgBGs.length; i++){
			fPosX[i] = 0;
			fCamX = ModeGame.gameCamera.getPosX();
		}
	}
	
	public void update(){
		//Pos x
		float movCam = fCamX - ModeGame.gameCamera.getPosX();
		fCamX = ModeGame.gameCamera.getPosX();
		if(ModeGame.gameCamera.getPosX() > Define.SIZEX2 && ModeGame.gameCamera.getPosX() < ModeGame.worldWidth - Define.SIZEX2)
		{
			for(int i = GfxManager.vImgBGs.length -1; i > -1; i--){
				float div = 2f * (i + 1);
				int idx = (GfxManager.vImgBGs.length -1) - i;
				fPosX[idx] += movCam / div;
				
				if(fPosX[idx] < -GfxManager.vImgBGs[idx].getWidth()){
					fPosX[idx] = 0f;//GfxManager.vImgBGs[idx].getWidth() - fPosX[idx];
					
				}else if(fPosX[idx] > GfxManager.vImgBGs[idx].getWidth()){
					fPosX[idx] = 0f;//GfxManager.vImgBGs[idx].getWidth() - fPosX[idx];
				}
			}
		}
		//Pos y
		float rY = ModeGame.worldHeight - Define.SIZEY;//Es el rango de desplazamiento de la camara en ele eje y
		float camRY = (ModeGame.worldHeight - ModeGame.gameCamera.getPosY())- Define.SIZEY2;//Recorrido de la camara
		for(int i = GfxManager.vImgBGs.length -1; i > -1; i--){
			float div = (i + 1);
			int idx = (GfxManager.vImgBGs.length -1) - i;
			/*
			 * rango de desplazamiento de la camara = altura de la capa 1 (La que hace el scroll completo)
			 * desplazamiento de camara             = x
			 * 
			 */
			float modY = (camRY * GfxManager.vImgBGs[GfxManager.vImgBGs.length -1].getHeight()) / rY;
			fModY[idx] = modY / div;
		}
		
	}
	
	public void draw(Graphics _g){
		for(int i = 0; i < fPosX.length; i++){
			int posX = (int)fPosX[i] + Define.SIZEX2;
			_g.drawImage(GfxManager.vImgBGs[i], posX, Define.SIZEY + (int)fModY[i], Graphics.BOTTOM | Graphics.HCENTER);
			_g.drawImage(GfxManager.vImgBGs[i],
					posX + (fPosX[i] > 0 ? -GfxManager.vImgBGs[i].getWidth() : GfxManager.vImgBGs[i].getWidth()), 
					Define.SIZEY + (int)fModY[i], 
					Graphics.BOTTOM | Graphics.HCENTER);
			
		}
	}

}
