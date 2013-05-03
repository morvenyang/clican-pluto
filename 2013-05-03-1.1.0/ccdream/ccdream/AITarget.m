//
//  AITarget.m
//  ccdream
//
//  Created by wei zhang on 12-6-16.
//  Copyright 2012年 Clican. All rights reserved.
//


#import "AITarget.h"


@implementation AITarget

@synthesize targetCharacter = _targetCharacter;
@synthesize usingWeapon = _usingWeapon;
@synthesize moveOrbit = _moveOrbit;


+(AITarget*) targetCharacter:(Character*) targetCharacter usingWeapon:(Weapon*) usingWeapon moveOrbit:(MoveOrbit*) moveOrbit{
    AITarget* aiTarget = [[[AITarget alloc] init] autorelease];
    aiTarget.targetCharacter = targetCharacter;
    aiTarget.usingWeapon = usingWeapon;
    aiTarget.moveOrbit = moveOrbit;
    return aiTarget;
}

-(NSString*) description{
    return [@"" stringByAppendingFormat:@"targetCharacter:%@,usingWeapon:%@,moveOrbit:%@",_targetCharacter,_usingWeapon, _moveOrbit];
}

- (void)dealloc {
    [_targetCharacter release];
    _targetCharacter = nil;
	[_usingWeapon release];
    _usingWeapon = nil;
    [_moveOrbit release];
    _moveOrbit = nil;
    [super dealloc];
}
@end
