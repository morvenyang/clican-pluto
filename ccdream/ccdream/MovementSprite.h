//
//  MovementSprite.h
//  ccdream
//
//  Created by wei zhang on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//  用来渲染Character可移动范围阴影的对象
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Position.h"

@interface MovementSprite : CCSpriteBatchNode {
    
}

+(id) initWithPosiArray:(CCArray*) posiArray;
@end
