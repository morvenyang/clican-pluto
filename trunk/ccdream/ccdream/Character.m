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
        character.characterSelectDelegateArray = [CCArray array];
        [parentNode addChild:character.characterSprite];
        [[GlobalEventHandler sharedHandler] addPositionTouchDelegate:character];
        return character;
}

-(void) addCharacterSelectDelegate: (id) characterSelectDelegate
{
    [self.characterSelectDelegateArray addObject:characterSelectDelegate];
}

- (BOOL)touchBegan:(Position *)posi withEvent:(UIEvent *)event {
    if([PositionUtil isPosition:posi forNode:self.characterSprite]){
        CCLOG(@"Character is touched");
        for(int i=0;i<[self.characterSelectDelegateArray count];i++){
            id<CharacterSelectDelegate> delegate = [self.characterSelectDelegateArray objectAtIndex:i];
            if ([delegate respondsToSelector:@selector(selectCharacter:)]){
                 [delegate selectCharacter:self];
            }
        }
        return YES;
    }else {
        CCLOG(@"Map is touched");
    }
    
    return NO;
}



- (void)dealloc {
    [[GlobalEventHandler sharedHandler] removePositionTouchDelegate:self];
    [_airMobility release];
    _airMobility = nil;
	[_landMobility release];
    _landMobility = nil;
    [_characterSprite release];
    _characterSprite = nil;
    [_characterSelectDelegateArray release];
    _characterSelectDelegateArray = nil;
	[super dealloc];
}


@end
