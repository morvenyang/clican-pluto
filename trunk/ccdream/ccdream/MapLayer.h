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

@interface MapLayer : CCLayer<CharacterSelectDelegate,PositionTouchDelegate>
{
    Character* _char1;
    CCArray* _playerCharacterArray;
    CCArray* _enemyCharacterArray;
    CCTMXTiledMap* _tiledMap;
    Character* _selectedCharacter;
    CCArray* _movementArray;
    CCArray* _shadowSpriteArray;
    Position* _maxPosi;
    NSMutableDictionary* _mapGridAttributeMap;
    MapAttribute* _mapAttribute;
}

@property (nonatomic,retain) Character* char1;
@property (nonatomic,retain) CCArray* playerCharacterArray;
@property (nonatomic,retain) CCArray* enemyCharacterArray;
@property (nonatomic,retain) Character* selectedCharacter;
@property (nonatomic,retain) CCTMXTiledMap* tiledMap;
@property (nonatomic,retain) CCArray* movementArray;
@property (nonatomic,retain) CCArray* shadowSpriteArray;
@property (nonatomic,retain) Position* maxPosi;
@property (nonatomic,retain) NSMutableDictionary* mapGridAttributeMap;
@property (nonatomic,retain) MapAttribute* mapAttribute;
+(CCScene *) scene;
- (void)selectCharacter:(Character*) character;
@end
