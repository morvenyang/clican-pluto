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
+(CCArray*) calcMoveOrbitarrayFromPosition:(Position*) charPosi movement:(int) movement mobility:(Mobility*) mobility mapTypeMetrix:(NSDictionary*) mapTypeMetrix maxPosition:(Position*) maxPosition;

//把touch对象转换为位置对象
+(CGPoint) locationFromTouch:(UITouch*)touch;

//判断是不是点击了该节点
+(bool) isTouch:(UITouch*)touch forNode:(CCNode*) node;

//判断是不是这个节点在该位置上
+(bool) isPosition:(Position*)position forNode:(CCNode*) node;

//判断位置数组是否包含某个位置
+(bool) containsPosition:(Position*)position forArray:(CCArray*) array;
@end
