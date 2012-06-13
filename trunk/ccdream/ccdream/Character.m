//
//  Character.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Character.h"
#import "ImageUtil.h"

@implementation Character

@synthesize airMobility = _airMobility;
@synthesize landMobility = _landMobility;
@synthesize characterSprite = _characterSprite;
@synthesize sourceCharacterImage = _sourceCharacterImage;
@synthesize grayCharacterImage = _grayCharacterImage;
@synthesize sourcePosition = _sourcePosition;
@synthesize canMountHorse = _canMountHorse;
@synthesize mountHorse = _mountHorse;
@synthesize selected = _selected;
@synthesize finished = _finished;


+(id)characterWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile position:(Position*) position {
        Character* character = [[[Character alloc] init] autorelease];
        character.airMobility = [Mobility mobilityWithDefault];
        character.landMobility = [Mobility mobilityWithDefault];
        character.sourceCharacterImage = [UIImage imageNamed:spriteFile];
        character.grayCharacterImage = [ImageUtil convertToGrayScale:character.sourceCharacterImage];
        CCTexture2D *texture = [[[CCTexture2D alloc] initWithImage:character.sourceCharacterImage] autorelease];
        character.characterSprite = [CCSprite spriteWithTexture:texture];
        character.characterSprite.scale=0.5;
        character.sourcePosition = position;
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
    [_sourceCharacterImage release];
    _sourceCharacterImage = nil;
    [_grayCharacterImage release];
    _grayCharacterImage = nil;
	[super dealloc];
}


@end
