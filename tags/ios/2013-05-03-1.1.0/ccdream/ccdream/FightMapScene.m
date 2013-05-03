//
//  FightMapScene.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "FightMapScene.h"


@implementation FightMapScene

@synthesize mapLayer = _mapLayer;
@synthesize fightMenuLayer = _fightMenuLayer;
@synthesize weaponLayer = _weaponLayer;

+(FightMapScene*) scene{
    FightMapScene* scene = [FightMapScene node];
    MapLayer* mapLayer = [MapLayer sharedMapLayer];
    FightMenuLayer* fightMenuLayer = [FightMenuLayer sharedFightMenuLayer];
    WeaponLayer* weaponLayer = [WeaponLayer sharedWeaponLayer];
    scene.mapLayer = mapLayer;
    scene.fightMenuLayer = fightMenuLayer;
    scene.fightMenuLayer.visible = NO;
    scene.weaponLayer = weaponLayer;
    scene.weaponLayer.visible = NO;
    [scene addChild:mapLayer];
    [scene addChild:fightMenuLayer];
    [scene addChild:weaponLayer];
    return scene;
}

- (void)dealloc {
    [_mapLayer release];
    _mapLayer = nil;
    [_fightMenuLayer release];
    _fightMenuLayer = nil;
    [_weaponLayer release];
    _weaponLayer = nil;
    [super dealloc];
}

@end
