//
//  Character.h
//  ccdream
//
//  Created by zhang wei on 12-6-3.
//  Copyright 2012年 Clican. All rights reserved.
//
//  这个类是用来描述游戏人物的模型
#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "Mobility.h"
#import "PositionUtil.h"
#import "MovementSprite.h"
#import "GlobalEventHandler.h"

@protocol CharacterSelectDelegate;

@interface Character : NSObject<PositionTouchDelegate> {
    Mobility* _landMobility;
    Mobility* _airMobility;
    CCSprite* _characterSprite;
    CCArray* _characterSelectDelegateArray;
}

@property (nonatomic,retain) Mobility* landMobility;
@property (nonatomic,retain) Mobility* airMobility;
@property (nonatomic,retain) CCSprite* characterSprite;
@property (nonatomic,retain) CCArray* characterSelectDelegateArray;

-(id)characterWithParentNode:(CCNode*) parentNode spriteFile:(NSString*) spriteFile;

-(void) addCharacterSelectDelegate: (id) characterSelectDelegate;
@end

@protocol CharacterSelectDelegate <NSObject>

- (void)selectCharacter:(Character*) character;

@end


