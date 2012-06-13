//
//  MapLayer.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Character.h"
#import "MapAttribute.h"
#import "MapGridAttribute.h"
#import "FightMenuLayer.h"
#import "Session.h"

@interface MapLayer : CCLayer<PositionTouchDelegate>
{
    CCArray* _playerCharacterArray;
    CCArray* _enemyCharacterArray;
    CCTMXTiledMap* _tiledMap;
    CCArray* _movementArray;
    CCArray* _shadowSpriteArray;
    Position* _maxPosi;
    NSMutableDictionary* _mapGridAttributeMap;
    MapAttribute* _mapAttribute;
    Session* _fightMapSession;
}

@property (nonatomic,retain) CCArray* playerCharacterArray;
@property (nonatomic,retain) CCArray* enemyCharacterArray;
@property (nonatomic,retain) CCTMXTiledMap* tiledMap;
@property (nonatomic,retain) CCArray* movementArray;
@property (nonatomic,retain) CCArray* shadowSpriteArray;
@property (nonatomic,retain) Position* maxPosi;
@property (nonatomic,retain) NSMutableDictionary* mapGridAttributeMap;
@property (nonatomic,retain) MapAttribute* mapAttribute;
@property (nonatomic,retain) Session* fightMapSession;

+(MapLayer*) sharedMapLayer;

- (void)selectCharacter:(Character*) character;
@end
