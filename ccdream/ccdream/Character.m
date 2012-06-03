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
        self.characterSprite.position = CGPointMake(0, 0);
        [parentNode addChild:self.characterSprite];
    }
    [[CCScheduler sharedScheduler] scheduleUpdateForTarget:self priority:0 paused:NO];
    return self;
}

- (BOOL)ccTouchBegan:(UITouch *)touch withEvent:(UIEvent *)even{
    CCLOG(@"Character is touched");
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
