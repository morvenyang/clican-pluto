//
//  AITarget.h
//  ccdream
//
//  Created by wei zhang on 12-6-16.
//  Copyright 2012年 Clican. All rights reserved.
//
//  保存对于AI的攻击目标的信息
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Character.h"
#import "MoveOrbit.h"

@interface AITarget : NSObject {
    Character* _targetCharacter;
    Weapon* _usingWeapon;
    MoveOrbit* _moveOrbit;
    
}

@property (nonatomic,retain) Character* targetCharacter;
@property (nonatomic,retain) Weapon* usingWeapon;
@property (nonatomic,retain) MoveOrbit* moveOrbit;

+(AITarget*) targetCharacter:(Character*) targetCharacter usingWeapon:(Weapon*) usingWeapon moveOrbit:(MoveOrbit*) moveOrbit;
@end
