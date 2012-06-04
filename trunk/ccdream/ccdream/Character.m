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
@synthesize characterSelectDelegateArray = _characterSelectDelegateArray;

-(id)initWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile{
    self = [super init];
    if(self !=nil){
        self.airMobility = [Mobility initWithDefault];
        self.landMobility = [Mobility initWithDefault];
        self.characterSprite = [CCSprite spriteWithFile:spriteFile];
        self.characterSprite.scale=0.5; 
        self.characterSprite.position = CGPointMake(16, 16);
        self.characterSelectDelegateArray = [CCArray array];
        [parentNode addChild:self.characterSprite];
        [[CCTouchDispatcher sharedDispatcher] addTargetedDelegate:self priority:-1 swallowsTouches:YES];
    }
    return self;
}

-(void) addCharacterSelectDelegate: (id) characterSelectDelegate
{
    [self.characterSelectDelegateArray addObject:characterSelectDelegate];
}
- (BOOL)ccTouchBegan:(UITouch *)touch withEvent:(UIEvent *)even{
    if([PositionUtil isTouch:touch forNode:self.characterSprite]){
        CCLOG(@"Character is touched");
        for(int i=0;i<[self.characterSelectDelegateArray count];i++){
            id<CharacterSelectDelegate> delegate = [self.characterSelectDelegateArray objectAtIndex:i];
            if ([delegate respondsToSelector:@selector(selectCharacter:)]){
                 [delegate selectCharacter:self];
            }
        }
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
