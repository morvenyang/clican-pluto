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
+(FightMapScene*) scene{
    FightMapScene* scene = [FightMapScene node];
    MapLayer* mapLayer = [MapLayer node];
    FightMenuLayer* fightMenuLayer = [FightMenuLayer sharedFightMenuLayer];
    scene.mapLayer = mapLayer;
    scene.fightMenuLayer = fightMenuLayer;
    scene.fightMenuLayer.visible = NO;
    [scene addChild:mapLayer];
    [scene addChild:fightMenuLayer];
    return scene;
}

- (void)dealloc {
    [_mapLayer release];
    _mapLayer = nil;
    [super dealloc];
}

@end
