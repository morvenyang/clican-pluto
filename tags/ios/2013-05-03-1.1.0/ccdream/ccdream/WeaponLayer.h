//
//  WeaponLayer.h
//  ccdream
//
//  Created by wei zhang on 12-6-13.
//  Copyright 2012年 Clican. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Position.h"
#import "Character.h"
#import "WeaponMenuItem.h"

@interface WeaponLayer : CCLayer<PositionTouchDelegate> {
    CCMenu* _weaponMenu;
    CCArray* _weapons;
    CCArray* _targetShadowSpriteArray;
    CCArray* _targetCharacterArray;;
}

@property (nonatomic,retain) CCMenu* weaponMenu;
@property (nonatomic,retain) CCArray* weapons;
@property (nonatomic,retain) CCArray* targetShadowSpriteArray;
@property (nonatomic,retain) CCArray* targetCharacterArray;

+(WeaponLayer*) sharedWeaponLayer;

-(void) showAtPosition:(Position*) position charPosi:(Position*) charPosi character:(Character*) character;
-(void) hide;
@end
