//
//  Character.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Character.h"

@implementation Character

@synthesize airMobility = _airMobility;
@synthesize landMobility = _landMobility;
@synthesize characterSprite = _characterSprite;
@synthesize selected = _selected;



+(id)characterWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile position:(Position*) position {
        Character* character = [[[Character alloc] init] autorelease];
        character.airMobility = [Mobility mobilityWithDefault];
        character.landMobility = [Mobility mobilityWithDefault];
        character.characterSprite = [CCSprite spriteWithFile:spriteFile];
        character.characterSprite.scale=0.5; 
        character.characterSprite.position = [position toCenterCGPoint];
        CCLOG(@"position=%@",position);
        CCLOG(@"p.x=%f,p.y=%f",character.characterSprite.position.x,character.characterSprite.position.y);
        [parentNode addChild:character.characterSprite];
        return character;
}





- (void)dealloc {
    [_airMobility release];
    _airMobility = nil;
	[_landMobility release];
    _landMobility = nil;
    [_characterSprite release];
    _characterSprite = nil;
	[super dealloc];
}


@end
