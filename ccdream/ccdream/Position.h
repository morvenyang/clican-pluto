//
//  Position.h
//  ccdream
//
//  Created by wei zhang on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//  用于保存可移动物体的位置
#import <Foundation/Foundation.h>
#import "cocos2d.h"


@interface Position : NSObject {
    int _x;
    int _y;
}

@property (nonatomic,assign) int x;
@property (nonatomic,assign) int y;


+(id)initWithX:(int) x Y:(int) y;
@end
