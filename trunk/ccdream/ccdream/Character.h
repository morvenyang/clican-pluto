//
//  Character.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//  这个类是用来描述游戏人物的模型
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Mobility.h"

@interface Character : NSObject {
    Mobility* _landMobility;
    Mobility* _airMobility;
}

@property (nonatomic,retain) Mobility* landMobility;
@property (nonatomic,retain) Mobility* airMobility;

@end
