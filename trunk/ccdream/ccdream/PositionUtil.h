//
//  PositionUtil.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 HP. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Character.h"
#import "Position.h"
#import "Mobility.h"
#import "MoveOrbit.h"

@interface PositionUtil : NSObject {
    
}
//根据当前位置计算可移动单元格
+(CCArray*) calcMoveOrbitarrayFromPosition:(Position*) charPosi movement:(int) movement mobility:(Mobility*) mobility mapTypeMetrix:(NSArray*) mapTypeMetrix maxPosition:(Position*) maxPosition;

//把touch对象转换为位置对象
+(CGPoint) locationFromTouch:(UITouch*)touch;

//判断是不是点击了该节点
+(bool) isTouch:(UITouch*)touch forNode:(CCNode*) node;

@end
