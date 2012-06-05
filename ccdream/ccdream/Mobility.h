//
//  Mobility.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//
//  这个类是用来描述游戏中所有可移动物体的移动力的，在不同的地图
//  上不同的移动消耗点数
//  请参考tmx/tpye属性
//  数值越大表示在该类地形上需要消耗的移动点，代表移动能力约差，-1代表无法动
//
//
#import <Foundation/Foundation.h>
#import "cocos2d.h"

@interface Mobility : NSObject {
    int _mapType1;
    int _mapType2;
}

@property (nonatomic, assign) int mapType1;
@property (nonatomic, assign) int mapType2;

+(id) mobilityWithDefault;

@end
