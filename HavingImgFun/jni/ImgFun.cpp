#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/opencv.hpp>
using namespace cv;
using namespace std;

#define M 3
struct PD{
	int id;
	int prePr,prePl,prePu,prePd;
	int	sufPr,sufPl,sufPu,sufPd;
};

struct FP{
	int id;
	int x,y;
	int type;
};

struct FPType{
	int type;
};

void initPD(int num,vector<PD>& pd)
{
	int  m = num;
	PD temp;
	for(int i=0; i < m;i++)
	{
		temp.id = i;
		temp.prePr = 0; temp.prePl = 0; temp.prePu = 0; temp.prePd = 0;
		temp.sufPr = 0; temp.sufPl = 0; temp.sufPu = 0; temp.sufPd = 0;
		pd.push_back(temp);
	}
};

void initFP(FP& fP){
	FP temp;
	temp.id = 0;
	temp.x = 0; temp.y = 0;
	temp.type = 0;
}

void initFPType(FPType& fT){
	fT.type = 0;
}

extern "C" {
JNIEXPORT jlong JNICALL Java_com_example_havingimgfun_LibImgFun_ImgFun(
		JNIEnv* env, jobject obj, jlong bitmapPtr);
JNIEXPORT jlong JNICALL Java_com_example_havingimgfun_LibImgFun_ImgFun(
		JNIEnv* env, jobject obj, jlong bitmapPtr) {

		Mat* srcimage = (Mat*) bitmapPtr;
		Mat* resultImage = new Mat();





		/***************以下是功能部分************************************/

			cvtColor(*srcimage,*resultImage,CV_BGR2GRAY);
			vector< vector<Point> >contours;
			findContours(*resultImage,contours,CV_RETR_EXTERNAL,CV_CHAIN_APPROX_NONE,Point(0,0));

			Point pt;
			pt.x = 0;
			pt.y = 0;

			vector<Point>toDrawFeaturePoint;
			vector< vector<FP> >fP;

			//每个点的特征矢量

			vector<PD>pDi;
			vector< vector<PD> >pD;
			for(int i=0; i< contours.size(); i++)
			{
				int num = contours[i].size();
				initPD(num,pDi);
				pD.push_back(pDi);
			}

		    int thisPtx = 0;
			int thisPty = 0;
			int sufPtx = 0;
			int sufPty = 0;
			int l = 0;//指代轮廓中的点
			int lb = 0;//指代l后面的点
			for(int i = 0;i < contours.size(); i++)
			{
				vector<FP>featurePoint;//存储特征点
				int count = contours[i].size();

				/*********************排除噪点******************************/
				if(count > 50)
				{
					/*********************前N个点***************************/
					for(int j = 0; j < count   ; j++)
					{
						for(int k = j-10; k <= j - 1 ;k++)
						{
							l = k;
							if(k<0)
							    l = count + k;
							lb = l+1;
							if(lb >= count)
								 lb = lb - count;
							Point Pt(contours[i][l]);
							thisPtx = Pt.x;
							thisPty = Pt.y;
							Point sufPt(contours[i][lb]);
							sufPtx = sufPt.x;
							sufPty = sufPt.y;
							if(sufPtx == thisPtx+1 && sufPty == thisPty)
								pD[i][j].prePr++;
							if(sufPtx == thisPtx && sufPty == thisPty+1)
								pD[i][j].prePu++;
							if(sufPtx == thisPtx-1 && sufPty == thisPty)
								pD[i][j].prePl++;
							if(sufPtx == thisPtx && sufPty == thisPty-1)
								pD[i][j].prePd++;
						}
					}

					/*******************************************************/
					/*********************后N个点***************************/
					for(int j = 0; j < count ; j++)
					{
						for(int k = j; k < j+10 ;k++)
						{
							l = k;
							lb = l+1;
							if(k >= count )
								l = k -count;
							if(lb >= count)
								 lb = lb - count;
							Point Pt(contours[i][l]);
							thisPtx = Pt.x;
							thisPty = Pt.y;
							Point sufPt(contours[i][lb]);
							sufPtx = sufPt.x;
							sufPty = sufPt.y;
							if(sufPtx == thisPtx+1 && sufPty == thisPty)
								pD[i][j].sufPr++;
							if(sufPtx == thisPtx && sufPty == thisPty+1)
								pD[i][j].sufPu++;
							if(sufPtx == thisPtx-1 && sufPty == thisPty)
								pD[i][j].sufPl++;
							if(sufPtx == thisPtx && sufPty == thisPty-1)
								pD[i][j].sufPd++;
						}
					}
					/*******************************************************/
					int fpNum = 0;
					FP fp;
					int fpType = 0;

					for(int j=0; j < count; j++)
				    {
						initFP(fp);
						/************************第1类拐点********************************/
						if(pD[i][j].prePl > M && pD[i][j].sufPu > M)
						{
							if(fpType != 1){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 1;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 1;
							}
						}

						/************************第2类拐点********************************/
						if(pD[i][j].prePu > M && pD[i][j].sufPr > M)
						{
							if(fpType != 2){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 2;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 2;
							}
						}

						/************************第3类拐点********************************/
						if(pD[i][j].prePr > M && pD[i][j].sufPd > M)
						{
							if(fpType != 3){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 3;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 3;
							}
						}

						/************************第4类拐点********************************/
						if(pD[i][j].prePd > M && pD[i][j].sufPl > M)
						{
							if(fpType != 4){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 4;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 4;
							}
						}
						/************************第5类拐点********************************/
						if(pD[i][j].prePu > M && pD[i][j].sufPl > M)
						{
							if(fpType != 5){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 5;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 5;
							}
						}
						/************************第6类拐点********************************/
						if(pD[i][j].prePr > M && pD[i][j].sufPu > M)
						{
							if(fpType != 6){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 6;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 6;
							}
						}
						/************************第7类拐点********************************/
						if(pD[i][j].prePd > M && pD[i][j].sufPr > M)
						{
							if(fpType != 7){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 7;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 7;
							}

						}
						/************************第8类拐点********************************/
						if(pD[i][j].prePl > M && pD[i][j].sufPd > M)
						{
							if(fpType != 8){
								fp.id = fpNum;
								fp.x  = contours[i][j].x;
								fp.y  = contours[i][j].y;
								fp.type = 8;
								featurePoint.push_back(fp);
								toDrawFeaturePoint.push_back(contours[i][j]);
								fpNum++;
								fpType = 8;
							}
						}


			      }

				}
				    fP.push_back(featurePoint);


				/************************************************************/

			}

					Scalar color = Scalar(100, 0, 255);
					for(int j = 0;j<toDrawFeaturePoint.size(); j++){
						circle(*resultImage,toDrawFeaturePoint[j],2, color, 3 );
						//printf( "to(%d,%d)\n",toDrawFeaturePoint[j].x,toDrawFeaturePoint[j].y);
					}
					//printf("fPsize = %d\n",fP.size());

					FPType fT;
					vector<FPType>fPType;
					int tar[]={1,2,6,2,3,4,8,4};
					for(int j =0; j<fP.size(); j++){
						initFPType(fT);

						for(int l = 0;l<fP[j].size();l++){

							fT.type = fP[j][l].type;
							fPType.push_back(fT);
						  }

							if(fPType.size()>=8){
								int _num_len = fPType.size();
								int _tar_len = 8;
								int l = 0;int k = 0;
								int start = 1;
								int count = 0;
								while((start != 0) && k < _tar_len){
									if(l >= _num_len)
										l = l - _num_len;
									if(fPType[l].type == tar[k]){
										l++;
										k++;
									}
									else {
										l++;
										count++;
										k = 0;
										if(count >= _num_len)
											start = count - _num_len;
										else
											start = count;
									}
								}
								//if(k == _tar_len)
									//cout<<"An occlusion has been detected!"<<endl;
								//else
									//cout<<"No occlusion!"<<endl;
							 }
							for(int l=0;l<fPType.size();l++)
								fPType[l].type = 0;

						}


			drawContours(*resultImage,contours,-1,Scalar(255),1,CV_AA);


			return (jlong)resultImage;



	}
}

