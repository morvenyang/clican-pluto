//
//  Character.m
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "Character.h"
#import "ImageUtil.h"
#import "Weapon.h"
#import "Constants.h"
#import "PositionUtil.h"

@implementation Character

@synthesize airMobility = _airMobility;
@synthesize landMobility = _landMobility;
@synthesize characterSprite = _characterSprite;
@synthesize sourceCharacterImage = _sourceCharacterImage;
@synthesize grayCharacterImage = _grayCharacterImage;
@synthesize sourcePosition = _sourcePosition;
@synthesize targetPosition = _targetPosition;
@synthesize weapens = _weapens;
@synthesize attackRange = _attackRange;
@synthesize canMountHorse = _canMountHorse;
@synthesize mountHorse = _mountHorse;
@synthesize selected = _selected;
@synthesize finished = _finished;
@synthesize player = _player;


-(id) init{
    self = [super init];
    if(self){
        self.weapens = [CCArray array];
        self.attackRange = [[[NSMutableSet alloc] init] autorelease];
    }
    return self;
}
+(id)characterWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile position:(Position*) position {
        Character* character = [[[Character alloc] init] autorelease];
        character.airMobility = [Mobility mobilityWithDefault];
        character.landMobility = [Mobility mobilityWithDefault];
        character.sourceCharacterImage = [UIImage imageNamed:spriteFile];
        character.grayCharacterImage = [ImageUtil convertToGrayScale:character.sourceCharacterImage];
        CCTexture2D *texture = [[[CCTexture2D alloc] initWithImage:character.sourceCharacterImage] autorelease];
        character.characterSprite = [CCSprite spriteWithTexture:texture];
        character.characterSprite.scale=0.8;
        character.sourcePosition = position;
        character.characterSprite.position = [position toCenterCGPoint];
    Weapon* w1 = [Constants getWeapon:@"1"];
    Weapon* w2 = [Constants getWeapon:@"4"];
    [character.weapens addObject:w1];
    [character.weapens addObject:w2];
    for (Weapon* w in character.weapens) {
        for(int i=w.minRange;i<=w.maxRange;i++){
            NSNumber* range = [NSNumber numberWithInt:i];
            [character.attackRange addObject:range];
        }
    }
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
    [_sourcePosition release];
    _sourcePosition = nil;
    [_targetPosition release];
    _targetPosition = nil;
    [_weapens release];
    _weapens = nil;
    [_attackRange release];
    _attackRange = nil;
	[super dealloc];
}


@end
