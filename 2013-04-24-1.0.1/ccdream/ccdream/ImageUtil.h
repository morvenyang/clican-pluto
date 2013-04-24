//
//  ImageUtil.h
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"

@interface ImageUtil : NSObject {
    
}

+ (UIImage *)convertToGrayScale:(UIImage*) source;

+ (UIImage *)convertToRedScale:(UIImage*) source;
@end
