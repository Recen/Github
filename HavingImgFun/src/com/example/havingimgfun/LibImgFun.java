package com.example.havingimgfun;

import org.opencv.core.Mat;

public class LibImgFun {  
static {   
        System.loadLibrary("ImgFun");   
       }   
      /** 
            * @param width the current view width 
            * @param height the current view height 
*/ 
    public static native long ImgFun(long input);  
}