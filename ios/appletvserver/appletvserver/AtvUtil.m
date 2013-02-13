//
//  AtvUtil.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "AtvUtil.h"

@implementation AtvUtil

+(void)markReflect:(CALayer*)mainLayer image:(UIImage*)img {
    CALayer* imgLayer=[CALayer layer];
	imgLayer.frame=CGRectMake(0, 0, 93,124);
   
	imgLayer.contents=(id)img.CGImage;
    imgLayer.masksToBounds =YES;
    imgLayer.cornerRadius= 8;
	[mainLayer addSublayer:imgLayer];
    
	CALayer* reflectionLayer=[CALayer layer];
	reflectionLayer.contents=imgLayer.contents;
	reflectionLayer.opacity = 0.3;
	reflectionLayer.frame=CGRectMake(0, 124, 93, 40);
    
    reflectionLayer.masksToBounds =YES;
    reflectionLayer.cornerRadius= 8;
    reflectionLayer.transform = CATransform3DMakeScale(1.0, -1.0, 1.0); 
	reflectionLayer.sublayerTransform = reflectionLayer.transform;
	[mainLayer addSublayer:reflectionLayer];
	
    
	CALayer* shadowLayer = [CALayer layer];
    
	shadowLayer.contents=(id)img.CGImage;
	shadowLayer.frame=reflectionLayer.bounds;
    shadowLayer.masksToBounds =YES;
    shadowLayer.cornerRadius= 8;
	reflectionLayer.mask=shadowLayer;
    
}

@end
