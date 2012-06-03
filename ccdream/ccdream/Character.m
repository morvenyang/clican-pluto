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

-(id)initWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile{
    self = [super init];
    if(self !=nil){
        self.airMobility = [Mobility initWithDefault];
        self.landMobility = [Mobility initWithDefault];
        self.characterSprite = [CCSprite spriteWithFile:spriteFile];
        self.characterSprite.scale=0.5; 
        self.characterSprite.position = CGPointMake(16, 16);
 
        [parentNode addChild:self.characterSprite];
        [[CCTouchDispatcher sharedDispatcher] addTargetedDelegate:self priority:-1 swallowsTouches:YES];
    }
    return self;
}


- (BOOL)ccTouchBegan:(UITouch *)touch withEvent:(UIEvent *)even{
    if([PositionUtil isTouch:touch forNode:self.characterSprite]){
        CCLOG(@"Character is touched");
        return NO;
    }else {
        CCLOG(@"Map is touched");
    }
    
    return YES;
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
