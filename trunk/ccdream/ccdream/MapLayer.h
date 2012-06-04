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

@interface MapLayer : CCLayer<CharacterSelectDelegate>
{
    Character* _char1;
    CCTMXTiledMap* _tileMap;
}

@property (nonatomic,retain) Character* char1;
@property (nonatomic,retain) CCTMXTiledMap* tileMap;

+(CCScene *) scene;
- (void)selectCharacter:(Character*) character;
@end
