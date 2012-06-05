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

@interface MapLayer : CCLayer<CharacterSelectDelegate,PositionTouchDelegate>
{
    Character* _char1;
    CCTMXTiledMap* _tileMap;
    Character* _selectedCharacter;
    CCArray* _movementArray;
    CCArray* _shadowSpriteArray;
}

@property (nonatomic,retain) Character* char1;
@property (nonatomic,retain) Character* selectedCharacter;
@property (nonatomic,retain) CCTMXTiledMap* tileMap;
@property (nonatomic,retain) CCArray* movementArray;
@property (nonatomic,retain) CCArray* shadowSpriteArray;
+(CCScene *) scene;
- (void)selectCharacter:(Character*) character;
@end
