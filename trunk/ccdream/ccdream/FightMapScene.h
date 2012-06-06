//
//  FightMapScene.h
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//
//  战斗地图的场景
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "MapLayer.h"
@interface FightMapScene : CCScene {
    
    MapLayer* _mapLayer;
    FightMenuLayer* _fightMenuLayer;
}

@property (nonatomic,retain) MapLayer* mapLayer;
@property (nonatomic,retain) FightMenuLayer* fightMenuLayer;

+(FightMapScene*) scene;
@end
