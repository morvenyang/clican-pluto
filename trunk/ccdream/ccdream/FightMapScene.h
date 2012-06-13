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
#import "WeaponLayer.h"
@interface FightMapScene : CCScene {
    
    MapLayer* _mapLayer;
    FightMenuLayer* _fightMenuLayer;
    WeaponLayer* _weaponLayer;
}

@property (nonatomic,retain) MapLayer* mapLayer;
@property (nonatomic,retain) FightMenuLayer* fightMenuLayer;
@property (nonatomic,retain) WeaponLayer* weaponLayer;

+(FightMapScene*) scene;
@end
